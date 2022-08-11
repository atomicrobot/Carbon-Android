package com.atomicrobot.carbon.ui.compose

import android.graphics.Point
import android.graphics.Rect
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.navigation.AppScreens
import com.atomicrobot.carbon.ui.main.dummyCommits
import com.atomicrobot.carbon.ui.scanner.BarcodeAnalysisState
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.common.internal.BarcodeSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CommitPreviewProvider : PreviewParameterProvider<Commit> {
    override val values: Sequence<Commit>
        get() = dummyCommits.asSequence()
}

class AppScreenPreviewProvider : PreviewParameterProvider<AppScreens> {
    override val values: Sequence<AppScreens>
        get() = listOf(AppScreens.Home, AppScreens.Settings, AppScreens.Scanner).asSequence()
}

class BarcodeAnalysisStatePreviewProvider :
        PreviewParameterProvider<StateFlow<BarcodeAnalysisState>> {
    override val values: Sequence<StateFlow<BarcodeAnalysisState>>
        get() = listOf(
                MutableStateFlow(
                        BarcodeAnalysisState(
                                barcode = Barcode(
                                        BarcodeSourceSourceImp(
                                                "example@email.com")))),
                MutableStateFlow(
                        BarcodeAnalysisState(
                                barcode = Barcode(
                                        BarcodeSourceSourceImp(
                                                "example12344@email.com")))))
                .asSequence()
}

class BarcodeSourceSourceImp(
        private val barcodeDisplayValue: String,
        private val barcodeFormat: Int = Barcode.FORMAT_CODE_128): BarcodeSource {

    override fun getFormat(): Int = barcodeFormat

    override fun getValueType(): Int {
        TODO("Not yet implemented")
    }

    override fun getBoundingBox(): Rect? = Rect(0,0,0,0)

    override fun getCalendarEvent(): Barcode.CalendarEvent? {
        TODO("Not yet implemented")
    }

    override fun getContactInfo(): Barcode.ContactInfo? {
        TODO("Not yet implemented")
    }

    override fun getDriverLicense(): Barcode.DriverLicense? {
        TODO("Not yet implemented")
    }

    override fun getEmail(): Barcode.Email? {
        TODO("Not yet implemented")
    }

    override fun getGeoPoint(): Barcode.GeoPoint? {
        TODO("Not yet implemented")
    }

    override fun getPhone(): Barcode.Phone? {
        TODO("Not yet implemented")
    }

    override fun getSms(): Barcode.Sms? {
        TODO("Not yet implemented")
    }

    override fun getUrl(): Barcode.UrlBookmark? {
        TODO("Not yet implemented")
    }

    override fun getWifi(): Barcode.WiFi? {
        TODO("Not yet implemented")
    }

    override fun getDisplayValue(): String = barcodeDisplayValue

    override fun getRawValue(): String? {
        TODO("Not yet implemented")
    }

    override fun getRawBytes(): ByteArray? {
        TODO("Not yet implemented")
    }

    override fun getCornerPoints(): Array<Point>? = emptyArray<Point>()

}