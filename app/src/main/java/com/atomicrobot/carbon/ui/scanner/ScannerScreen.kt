package com.atomicrobot.carbon.ui.scanner

import android.Manifest
import android.graphics.Rect
import android.graphics.RectF
import android.view.WindowManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoCameraBack
import androidx.compose.material.icons.rounded.PhotoCameraFront
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.compose.LocalActivity
import com.atomicrobot.carbon.ui.compose.PermissionRationaleResult
import com.atomicrobot.carbon.ui.compose.PermissionRequestResult
import com.atomicrobot.carbon.ui.compose.RequestPermission
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import kotlin.math.max
import kotlin.math.min

@Composable
fun ScannerScreen(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onBarcodeSelected: (Barcode) -> Unit = {}
) {
    val cameraPermRationale = stringResource(id = R.string.camera_perm_rationale)
    val viewModel: ScannerViewModel = getViewModel()
    RequestPermission(
        permission = Manifest.permission.CAMERA,
        onShowRationale =
        {
            // Show the permission rationale to the user as a snackbar message
            val result: SnackbarResult = scaffoldState
                .snackbarHostState
                .showSnackbar(cameraPermRationale, "Grant", SnackbarDuration.Short)
            return@RequestPermission if (result == SnackbarResult.ActionPerformed) {
                PermissionRationaleResult.ActionPerformed
            } else {
                PermissionRationaleResult.Dismissed
            }
        },
        onPermissionResult = {
            when (it) {
                PermissionRequestResult.Granted ->
                    viewModel.setCameraPermissionState(it == PermissionRequestResult.Granted)
                else -> {}
            }
        }
    )
    CameraContent(viewModel, onBarcodeSelected)
}
@Composable
fun CameraContent(
    viewModel: ScannerViewModel = getViewModel(),
    onBarcodeSelected: (Barcode) -> Unit = {}
) {
    val cameraPermissionState by viewModel.cameraPermissionState.collectAsState()
    // Keep the screen active
    KeepScreenOn(cameraPermissionState)

    val selectedCamera by viewModel.cameraSelectorState.collectAsState()
    // ConstraintLayout in Jetpack Compose YAY!!!
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (preview, barcodeOverlay, barcodeChip, shutter) = createRefs()
        CameraPreview(
            Modifier
                .fillMaxSize()
                .constrainAs(preview) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            viewModel.cameraProviderLiveData,
            cameraPermissionState,
            selectedCamera,
            viewModel
        )
        // Overlay that will highlight detected barcodes
        ScannerOverlay(
            Modifier
                .fillMaxSize()
                .constrainAs(barcodeOverlay) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            viewModel.barcodeOverlayState
        )
        // Clickable Chip that allows operator to try and open the barcode
        ScannerChip(
            // Make sure the chip is always above the camera button
            modifier = Modifier.constrainAs(barcodeChip) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(shutter.top, 16.dp)
            },
            viewModel.barcodeOverlayState,
            onBarcodeSelected
        )
        // Button to allow the operator to toggle back and forth between front and rear cameras
        CameraButton(
            Modifier.constrainAs(shutter) {
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            },
            cameraPermissionState,
            selectedCamera,
            viewModel::toggleSelectedCamera
        )
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraProviderLiveData: LiveData<ProcessCameraProvider>,
    cameraPermissionGranted: Boolean,
    cameraSelector: CameraSelector,
    imageAnalyzer: ImageAnalysis.Analyzer = EmptyImageAnalyzer()
) {
    when (cameraPermissionGranted) {
        true -> LivePreview(modifier, cameraProviderLiveData, cameraSelector, imageAnalyzer)
        false -> NoCameraPermissionPreview(modifier)
    }
}

@Composable
fun LivePreview(
    modifier: Modifier = Modifier,
    cameraProviderLiveData: LiveData<ProcessCameraProvider>,
    cameraSelector: CameraSelector,
    imageAnalyzer: ImageAnalysis.Analyzer = EmptyImageAnalyzer()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView({ ctx ->
        // Embed the Camera PreviewView as the content
        PreviewView(ctx)
    }, modifier) {
    cameraProviderLiveData.observe(lifecycleOwner) { provider ->
        provider.unbindAll()
        // Create a use-case for previewing the camera feed
        val previewUseCase = Preview
            .Builder()
            .build()
            .apply { setSurfaceProvider(it.surfaceProvider) }
        val usesCases: MutableList<UseCase> = mutableListOf(previewUseCase)
        // Create a use-case for for analyzing the camera feed
        val inferenceUseCase = ImageAnalysis.Builder()
            .build().apply {
                setAnalyzer(
                    ContextCompat.getMainExecutor(it.context),
                    imageAnalyzer
                )
            }
        usesCases.add(inferenceUseCase)

        // Attach use cases to the camera with the same lifecycle owner
        provider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            *usesCases.toTypedArray()
        )
    }
}
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun NoCameraPermissionPreview(modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Color.DarkGray) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = Icons.Rounded.PhotoCamera,
                contentDescription = "Camera",
                modifier = Modifier
                    .size(88.dp)
                    .align(Alignment.CenterHorizontally),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(R.string.camera_perm_denied),
                color = Color.White,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun ScannerChip(
    modifier: Modifier = Modifier,
    barcodeOverlayStateFlow: StateFlow<ScannerViewModel.BarcodeAnalysisState>,
    onBarcodeSelected: (Barcode) -> Unit = {}
) {
    val barcodeOverlayState by barcodeOverlayStateFlow.collectAsState()

    // Hide the chip if there are no barcodes
    AnimatedVisibility(
        visible = barcodeOverlayState.hasBarcodes,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 8.dp)
                .clip(shape = CircleShape)
                .background(color = Color.DarkGray, shape = CircleShape)
                .clickable { onBarcodeSelected(barcodeOverlayState.barcode!!) }
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.QrCode,
                contentDescription = "",
                modifier = Modifier.padding(end = 8.dp),
                tint = Color.White
            )
            Text(
                text = barcodeOverlayState.barcode?.displayValue ?: "",
                color = Color.White,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ScannerOverlay(
    modifier: Modifier = Modifier,
    barcodeOverlayStateFlow: StateFlow<ScannerViewModel.BarcodeAnalysisState>
) {
    val barcodeOverlayState by barcodeOverlayStateFlow.collectAsState()

    val textPaint = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 60f
            color = android.graphics.Color.WHITE
        }
    }

    val textBoundsRect = remember { Rect() }
    val textDrawRect = remember { RectF() }
    val adjustedBoundingRect = remember { RectF() }

    Canvas(modifier = modifier) {
        if (barcodeOverlayState.hasBarcodes) {
            /*
             * The logic for converting the barcode bounding box into the Compose coordinate space
             * was based on the example found in the MLKit Vision sample found at this link:
             * https://github.com/googlesamples/mlkit
             */
            val viewAspectRatio: Float = size.width / size.height
            val imageAspectRatio: Float = (
                barcodeOverlayState.sourceImageWidth.toFloat() /
                    barcodeOverlayState.sourceImageHeight
                )

            var postScaleWidthOffset = 0f
            var postScaleHeightOffset = 0f

            val scaleFactor: Float
            if (viewAspectRatio > imageAspectRatio) {
                // The image needs to be vertically cropped to be displayed in this view.
                scaleFactor = size.width / barcodeOverlayState.sourceImageWidth
                postScaleHeightOffset = (size.width / imageAspectRatio - size.height) / 2
            } else {
                // The image needs to be horizontally cropped to be displayed in this view.
                scaleFactor = size.height / barcodeOverlayState.sourceImageHeight
                postScaleWidthOffset = (size.height * imageAspectRatio - size.width) / 2
            }

            fun scale(pixel: Float) = scaleFactor * pixel

            fun translateX(x: Float): Float = if (barcodeOverlayState.isFlipped)
                size.width - (scale(x) - postScaleWidthOffset)
            else
                scale(x) - postScaleWidthOffset

            fun translateY(y: Float): Float = scale(y) - postScaleHeightOffset

            /**
             * Returns a new RectF based on source but correct for the coordinate space of the
             * drawing context
             */
            fun adjustBounds(source: Rect, outRect: RectF) {
                outRect.apply {
                    set(source)
                    val x0 = translateX(left)
                    val x1 = translateX(right)
                    left = min(x0, x1)
                    right = max(x0, x1)
                    top = translateY(top)
                    bottom = translateY(bottom)
                }
            }

            adjustBounds(barcodeOverlayState.barcode?.boundingBox!!, adjustedBoundingRect)
            // Draw barcode bounding box
            drawRoundRect(
                color = Color.DarkGray,
                topLeft = Offset(adjustedBoundingRect.left, adjustedBoundingRect.top),
                size = Size(adjustedBoundingRect.width(), adjustedBoundingRect.height()),
                style = Stroke(width = 4.dp.toPx())
            )

            val barcodeText = barcodeOverlayState.barcode!!.displayValue!!
            textPaint.getTextBounds(barcodeText, 0, barcodeText.length, textBoundsRect)
            val textHeight = textBoundsRect.height().toFloat()
            val textWidth = textPaint.measureText(barcodeText) + 4.dp.toPx()

            // Make sure the rect isn't to close to the top of the screen that would cause it to
            // clip
            if ((adjustedBoundingRect.top - textHeight - 16.dp.toPx()) < 0) {
                // Draw the label on the bottom of the bounding box
                textDrawRect.apply {
                    left = adjustedBoundingRect.left
                    right = left + textWidth + 8.dp.toPx()
                    top = adjustedBoundingRect.bottom
                    bottom = top + textHeight + 16.dp.toPx()
                }
            } else {
                textDrawRect.apply {
                    left = adjustedBoundingRect.left
                    right = left + textWidth + 16.dp.toPx()
                    bottom = adjustedBoundingRect.top
                    top = (bottom - 16.dp.toPx() - textHeight)
                }
            }
            /* Ignore drawing the text with the overlay.. ScannerChip will take care of this
            // Draw barcode label rect
            drawRoundRect(
                color = Color.DarkGray,
                topLeft = Offset(textDrawRect.left, textDrawRect.top),
                size = Size(textDrawRect.width(), textDrawRect.height())
            )
            // For some reason text drawing in compose requires explicit calls to the native canvas.
            drawContext
                .canvas
                .nativeCanvas
                .drawText(
                    barcodeOverlayState.barcode!!.displayValue!!,
                    adjustedBoundingRect.left + 8.dp.toPx(),
                    textDrawRect.centerY() + (textHeight / 2f),
                    textPaint
                )
             */
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraButton(
    modifier: Modifier = Modifier,
    cameraPermissionGranted: Boolean = false,
    cameraSelector: CameraSelector? = null,
    onToggleCamera: () -> Unit = { }
) {
    OutlinedButton(
        onClick = onToggleCamera,
        modifier = modifier
            .size(80.dp),
        enabled = cameraPermissionGranted,
        shape = CircleShape,
        border = BorderStroke(2.dp, Color.White),
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(5.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            color = Color.White
        ) {
            val imageVec = if (cameraSelector?.lensFacing == CameraSelector.LENS_FACING_BACK) {
                Icons.Rounded.PhotoCameraBack
            } else {
                Icons.Rounded.PhotoCameraFront
            }
            Icon(
                imageVector = imageVec,
                modifier = Modifier.padding(16.dp),
                contentDescription = stringResource(R.string.camera_cont_desc),
                tint = Color.Black
            )
        }
    }
}

@Composable
fun KeepScreenOn(cameraPermissionState: Boolean) {
    if (cameraPermissionState) {
        val activity = LocalActivity.current
        DisposableEffect(Unit) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            onDispose {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}

class EmptyImageAnalyzer : ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy) {
        TODO("Not yet implemented")
    }
}
