package com.atomicrobot.carbon.ui.scanner

import android.Manifest
import android.app.Application
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atomicrobot.carbon.util.Common
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScannerViewModel(private val app: Application) : ViewModel(), ImageAnalysis.Analyzer {

    private val hasCameraPermission: Boolean
        get() = Common.hasPermission(app, Manifest.permission.CAMERA)

    private val _cameraPermissionState: MutableStateFlow<Boolean> =
        MutableStateFlow(hasCameraPermission)

    private val _cameraSelectorState: MutableStateFlow<Int> =
            MutableStateFlow(CameraSelector.LENS_FACING_BACK)

    val cameraPermissionState = _cameraPermissionState.asStateFlow()

    val cameraSelectorState = _cameraSelectorState.asStateFlow()

    fun setCameraPermissionState(granted: Boolean) {
        viewModelScope.launch {
            _cameraPermissionState.value = granted
        }
    }

    fun toggleSelectedCamera() { }

    override fun analyze(image: ImageProxy) {
        TODO("Not yet implemented")
    }
}
