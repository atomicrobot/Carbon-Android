package com.atomicrobot.carbon.ui.scanner

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.ui.compose.PermissionRationaleResult
import com.atomicrobot.carbon.ui.compose.PermissionRequestResult
import com.atomicrobot.carbon.ui.compose.RequestPermission
import org.koin.androidx.compose.getViewModel

@Composable
fun ScannerScreen(scaffoldState: ScaffoldState = rememberScaffoldState()) {
    val viewModel: ScannerViewModel = getViewModel()
    val cameraPermRationale = stringResource(id = R.string.camera_perm_rationale)
    val cameraPermissionState by viewModel.cameraPermissionState.collectAsState()
    val cameraSelectorState by viewModel.cameraSelectorState.collectAsState()

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
    CameraContent(cameraPermissionState, cameraSelectorState) {
        viewModel.toggleSelectedCamera()
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun CameraContent(
    cameraPermissionGranted: Boolean = false,
    selectedCamera: Int = CameraSelector.LENS_FACING_BACK,
    onToggleCamera: () -> Unit = { }
) {
    Box {
        when (cameraPermissionGranted) {
            true -> CameraPreview(Modifier.fillMaxSize())
            false -> NoCameraPermissionPreview()
        }
        OutlinedButton(
            onClick = onToggleCamera,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.BottomCenter)
                .padding(10.dp),
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
                var imageVec = if (selectedCamera == CameraSelector.LENS_FACING_BACK) {
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
}

@Composable
fun CameraPreview(modifier: Modifier = Modifier) {
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
                                // Attach use cases to the camera with the same lifecycle owner
                                bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    // Attach the PreviewView's surface to the camera provider so we
                                    // get a live preview of the camera-feed
                                    Preview
                                        .Builder()
                                        .build()
                                        .apply { setSurfaceProvider(surfaceProvider) }
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
fun NoCameraPermissionPreview() {
    Surface(color = Color.DarkGray) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
