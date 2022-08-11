package com.atomicrobot.carbon.ui.scanner

import android.Manifest
import android.app.Application
import android.os.SystemClock
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
                 Barcode.FORMAT_CODE_128,
                 Barcode.FORMAT_QR_CODE)
         .build()

    private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(options)

    private val hasCameraPermission: Boolean
        get() = Common.hasPermission(app, Manifest.permission.CAMERA)

    private val _cameraPermissionState: MutableStateFlow<Boolean> =
        MutableStateFlow(hasCameraPermission)

    private val _cameraSelectorState: MutableStateFlow<Int> =
            MutableStateFlow(CameraSelector.LENS_FACING_BACK)

    private val _barcodeStateState: MutableStateFlow<BarcodeAnalysisState> =
            MutableStateFlow(BarcodeAnalysisState())

    val cameraPermissionState = _cameraPermissionState.asStateFlow()

    val cameraSelectorState = _cameraSelectorState.asStateFlow()

    val barcodeOverlayState = _barcodeStateState.asStateFlow()

    private val currentBarcode: Barcode?
        get() = _barcodeStateState.value.barcode

    private val isImageFlipped = _cameraSelectorState.value == CameraSelector.LENS_FACING_FRONT

    private var _elapsedMillis: Long = -1L

    private var _lastBarcode: Barcode? = null

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
            val imageWidth: Int
            val imageHeight: Int
            if(image.imageInfo.rotationDegrees == 0 || image.imageInfo.rotationDegrees == 180) {
                imageWidth = image.width
                imageHeight = image.height
            } else {
                imageWidth = image.height
                imageHeight = image.width
            }
            // If the image state has changed, update the barcode overlay
            if(_barcodeStateState.value.sourceImageWidth != imageWidth ||
                    _barcodeStateState.value.sourceImageHeight != imageHeight ||
                    _barcodeStateState.value.isFlipped != isImageFlipped) {
                _barcodeStateState.value = _barcodeStateState.value.copy(
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
        // Support for a single barcode is artificial and makes displaying overlay data easier
        val newBarcode = barcodes.firstOrNull()
         if(currentBarcode == null && newBarcode != null) {
            _barcodeStateState.value = _barcodeStateState.value.copy(barcode = newBarcode)
        }
        else if(newBarcode?.rawValue != currentBarcode?.rawValue) {
            val now = SystemClock.elapsedRealtime()
            if(_elapsedMillis == Long.MAX_VALUE) {
                _elapsedMillis = now
            }
            if((now - _elapsedMillis) >= MAX_BARCODE_DWELL_MS) {
                _elapsedMillis = Long.MAX_VALUE
                _barcodeStateState.value = _barcodeStateState.value.copy(barcode = newBarcode)
            }
        }
        else {
             _elapsedMillis = Long.MAX_VALUE
            // Keep updating the state to have an accurate bounding box
            _barcodeStateState.value = _barcodeStateState.value.copy(barcode = newBarcode)
        }
    }

    override fun onFailure(exception: Exception) {
        _barcodeStateState.value = _barcodeStateState.value.copy(barcode = null)
    }

    companion object {
        private const val MAX_BARCODE_DWELL_MS = 200
    }
}

data class BarcodeAnalysisState(
        val sourceImageWidth: Int = 0,
        val sourceImageHeight: Int = 0,
        val isFlipped: Boolean = false,
        val barcode: Barcode? = null
) {
    val hasBarcodes: Boolean = barcode != null
}
