package com.atomicrobot.carbon.ui.scanner

import android.Manifest
import android.graphics.Rect
import android.graphics.RectF
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.compose.PermissionRationaleResult
import com.atomicrobot.carbon.ui.compose.PermissionRequestResult
import com.atomicrobot.carbon.ui.compose.RequestPermission
import org.koin.androidx.compose.getViewModel
import kotlin.math.max
import kotlin.math.min

@Composable
fun ScannerScreen(scaffoldState: ScaffoldState = rememberScaffoldState()) {
    val viewModel: ScannerViewModel = getViewModel()
    val cameraPermRationale = stringResource(id = R.string.camera_perm_rationale)
    val cameraPermissionState by viewModel.cameraPermissionState.collectAsState()
    val cameraSelectorState by viewModel.cameraSelectorState.collectAsState()
    val barcodeOverlayState by viewModel.barcodeOverlayState.collectAsState()

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
            viewModel.setCameraPermissionState(it == PermissionRequestResult.Granted)
        }
    )
    CameraContent(cameraPermissionState, cameraSelectorState, barcodeOverlayState, viewModel) {
        viewModel.toggleSelectedCamera()
    }
}

@androidx.compose.ui.tooling.preview.Preview(showSystemUi = true)
@Composable
private fun CameraContent(
        cameraPermissionGranted: Boolean = false,
        selectedCamera: Int = CameraSelector.LENS_FACING_BACK,
        barcodeOverlayState: ScannerImageOverlayState = ScannerImageOverlayState(),
        imageAnalyzer: ImageAnalysis.Analyzer? = null,
        onToggleCamera: () -> Unit = { }
) {
    Box {
        CameraPreview(
                Modifier.fillMaxSize(),
                cameraPermissionGranted,
                imageAnalyzer)
        ScannerOverlay(Modifier.fillMaxSize(), barcodeOverlayState)
        CameraButton(
                Modifier.align(Alignment.BottomCenter),
                cameraPermissionGranted,
                selectedCamera,
                onToggleCamera)
    }
}

@Composable
fun CameraPreview(modifier: Modifier = Modifier,
                  cameraPermissionGranted: Boolean = false,
                  imageAnalyzer: ImageAnalysis.Analyzer?) {
    when (cameraPermissionGranted) {
        true -> LivePreview(modifier, imageAnalyzer!!)
        false -> NoCameraPermissionPreview(modifier)
    }
}

@Composable
fun LivePreview(modifier: Modifier = Modifier,
                imageAnalyzer: ImageAnalysis.Analyzer) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    AndroidView({ ctx ->
        // Embed the Camera PreviewView as the content
        PreviewView(ctx)
                .apply {
                    // Add a listener that will get invoked on the main executor when the Camera
                    // Provider is available
                    cameraProviderFuture.addListener(
                            {
                                cameraProviderFuture.get()
                                        .apply {
                                            unbindAll()
                                            // Require the back-facing lens
                                            val cameraSelector = CameraSelector.Builder()
                                                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                                    .build()

                                            // Create a use-case for previewing the camera feed
                                            val previewUseCase = Preview
                                                    .Builder()
                                                    .build()
                                                    .apply { setSurfaceProvider(surfaceProvider) }
                                            val usesCases: MutableList<UseCase> = mutableListOf(previewUseCase)
                                            // Create a use-case for for analyzing the camera feed
                                            val inferenceUseCase = ImageAnalysis.Builder()
                                                    .build().apply {
                                                        setAnalyzer(
                                                                ContextCompat.getMainExecutor(ctx),
                                                                imageAnalyzer)
                                                    }
                                            usesCases.add(inferenceUseCase)
                                            // Attach use cases to the camera with the same lifecycle owner
                                            bindToLifecycle(
                                                    lifecycleOwner,
                                                    cameraSelector,
                                                    *usesCases.toTypedArray()
                                            )
                                        }
                            },
                            ContextCompat.getMainExecutor(ctx)
                    )
                }
    }, modifier)
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun NoCameraPermissionPreview(modifier: Modifier = Modifier) {
    Surface(color = Color.DarkGray) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Column {
                Image(
                    imageVector = Icons.Rounded.PhotoCamera,
                    contentDescription = "Camera",
                    modifier = Modifier
                            .size(88.dp)
                            .align(Alignment.CenterHorizontally),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = "Camera permission not granted",
                    color = Color.White,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Composable
fun ScannerOverlay(
        modifier: Modifier = Modifier,
        barcodeOverlayState: ScannerImageOverlayState = ScannerImageOverlayState()) {

    val textPaint = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 60f
            color = android.graphics.Color.BLACK
        }
    }

    Canvas(modifier = modifier) {
        if(barcodeOverlayState.hasBarcodes) {
            val viewAspectRatio: Float = size.width / size.height
            val imageAspectRatio: Float = (barcodeOverlayState.sourceImageWidth.toFloat() / barcodeOverlayState.sourceImageHeight)

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

            fun translateX(x: Float): Float = if(barcodeOverlayState.isFlipped)
                    size.width - (scale(x) - postScaleWidthOffset)
                else
                    scale(x) - postScaleWidthOffset

            fun translateY(y: Float): Float = scale(y) - postScaleHeightOffset

            /**
             * Returns a new RectF based on source but correct for the coordinate space of the
             * drawing context
             */
            fun adjustBounds(source: Rect): RectF {
                return RectF(source).apply {
                    val x0 = translateX(left)
                    val x1 = translateX(right)
                    left = min(x0, x1)
                    right = max(x0, x1)
                    top = translateY(top)
                    bottom = translateY(bottom)
                }
            }
            val boundingBox = adjustBounds(barcodeOverlayState.barcode?.boundingBox!!)
            drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(boundingBox.left, boundingBox.top),
                    size = Size(boundingBox.width(), boundingBox.height()),
                    style = Stroke(width = 2.dp.toPx()))


            val textBounds = Rect()
            val bCodeText = barcodeOverlayState.barcode.displayValue!!
            textPaint.getTextBounds(bCodeText, 0, bCodeText.length, textBounds)
            val textHeight = textBounds.height().toFloat()
            val textWidth = textPaint.measureText(bCodeText) + 4.dp.toPx()

            val labelRectF = if((boundingBox.top - textHeight) < 0) {
                // Draw the label on the bottom of the bounding box
                RectF().apply {
                    left = boundingBox.left
                    top = boundingBox.bottom
                    right = left + textWidth
                    bottom = top + textHeight + 8.dp.toPx()
                }
            }
            else {
                RectF().apply {
                    left = boundingBox.left
                    top = (boundingBox.top - 8.dp.toPx() - textHeight)
                    right = left + textWidth
                    bottom = boundingBox.top
                }
            }
            drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(labelRectF.left, labelRectF.top),
                    size = Size(labelRectF.width(), labelRectF.height()))

            drawContext
                    .canvas
                    .nativeCanvas
                    .drawText(barcodeOverlayState.barcode.displayValue!!,
                            boundingBox.left,
                            labelRectF.centerY() + (textHeight / 2f),
                            textPaint)
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraButton(
        modifier: Modifier = Modifier,
        cameraPermissionGranted: Boolean = false,
        selectedCamera: Int = CameraSelector.LENS_FACING_BACK,
        onToggleCamera: () -> Unit = { }) {
    OutlinedButton(
            onClick = onToggleCamera,
            modifier = modifier
                    .size(100.dp)
                    .padding(vertical = 10.dp),
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
            val imageVec = if (selectedCamera == CameraSelector.LENS_FACING_BACK) {
                Icons.Rounded.PhotoCameraBack
            } else {
                Icons.Rounded.PhotoCameraFront
            }
            Icon(
                    imageVector = imageVec,
                    modifier = Modifier.padding(16.dp),
                    contentDescription = "Snap a pic of a QrCode",
                    tint = Color.Black
            )
        }
    }
}