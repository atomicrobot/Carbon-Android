package com.atomicrobot.carbon.ui.scanner

import android.Manifest
import android.app.Application
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.util.Common
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class ScannerViewModel(private val app: Application)
    : ViewModel(),
        ImageAnalysis.Analyzer,
        OnSuccessListener<List<Barcode>>,
        OnFailureListener {

     private var options: BarcodeScannerOptions = BarcodeScannerOptions.Builder()
         .setBarcodeFormats(Barcode.FORMAT_EAN_13,
                 Barcode.FORMAT_UPC_E,
                 Barcode.FORMAT_QR_CODE)
         .build()

    private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(options)

    private val hasCameraPermission: Boolean
        get() = Common.hasPermission(app, Manifest.permission.CAMERA)

    private val _cameraPermissionState: MutableStateFlow<Boolean> =
        MutableStateFlow(hasCameraPermission)

    private val _cameraSelectorState: MutableStateFlow<Int> =
            MutableStateFlow(CameraSelector.LENS_FACING_BACK)

    private val _barcodeOverlayState: MutableStateFlow<ScannerImageOverlayState> =
            MutableStateFlow(ScannerImageOverlayState())

    val cameraPermissionState = _cameraPermissionState.asStateFlow()

    val cameraSelectorState = _cameraSelectorState.asStateFlow()

    val barcodeOverlayState = _barcodeOverlayState.asStateFlow()

    private val isImageFlipped = _cameraSelectorState.value == CameraSelector.LENS_FACING_FRONT

    fun setCameraPermissionState(granted: Boolean) {
        viewModelScope.launch {
            _cameraPermissionState.value = granted
        }
    }

    fun toggleSelectedCamera() {
        viewModelScope.launch {
            _cameraSelectorState.value =
                    if(_cameraSelectorState.value == CameraSelector.LENS_FACING_BACK) {
                        CameraSelector.LENS_FACING_FRONT
                    } else {
                        CameraSelector.LENS_FACING_BACK
                    }
        }
    }

    override fun analyze(image: ImageProxy) {
        image.image?.let {
            val rotationDegrees = image.imageInfo.rotationDegrees
            val imageWidth: Int
            val imageHeight: Int
            if(rotationDegrees == 0 || rotationDegrees == 180) {
                imageWidth = image.width
                imageHeight = image.height
            } else {
                imageWidth = image.height
                imageHeight = image.width
            }
            // If the image state has changed, update the barcode overlay
            if(_barcodeOverlayState.value.sourceImageWidth != imageWidth ||
                    _barcodeOverlayState.value.sourceImageHeight != imageHeight ||
                    _barcodeOverlayState.value.isFlipped != isImageFlipped) {
                _barcodeOverlayState.value = _barcodeOverlayState.value.copy(
                        sourceImageWidth = imageWidth,
                        sourceImageHeight = imageHeight,
                        isFlipped = isImageFlipped)
            }
            // Pass the image to the barcode scanner then assign a completion listener that will
            // close the image proxy to free-up the analysis pipeline
            barcodeScanner.process(InputImage.fromMediaImage(it, image.imageInfo.rotationDegrees))
                    .addOnSuccessListener(TaskExecutors.MAIN_THREAD, this)
                    .addOnFailureListener(this)
                    .addOnCompleteListener {
                        image.close()
                    }
        }
    }

    override fun onSuccess(barcodes: List<Barcode>) {
        _barcodeOverlayState.value =
                _barcodeOverlayState.value.copy(barcode = barcodes.firstOrNull())
    }

    override fun onFailure(exception: Exception) {
        Log.d("DBUG", "Failed trying to detect barcodes: ${exception.message}")
        _barcodeOverlayState.value = _barcodeOverlayState.value.copy(barcode = null)
    }
}

data class ScannerImageOverlayState(
        val sourceImageWidth: Int = 0,
        val sourceImageHeight: Int = 0,
        val isFlipped: Boolean = false,
        val barcode: Barcode? = null
) {
    val hasBarcodes: Boolean = barcode != null
}
