package com.atomicrobot.carbon.ui.deeplink

import android.app.Application
import android.graphics.Color
import android.os.Parcelable
import androidx.databinding.Bindable
import com.atomicrobot.carbon.BR
import com.atomicrobot.carbon.deeplink.DeepLinkInteractor
import com.atomicrobot.carbon.ui.BaseViewModel
import com.atomicrobot.carbon.ui.NavigationEvent
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

//class DeepLinkSampleViewModel @Inject constructor(
//    private val app: Application,
//    private val deepLinkInteractor: DeepLinkInteractor
//)
//    : BaseViewModel(app) {
//
//    @Parcelize
//    class State: Parcelable
//
//    sealed class ViewNavigation {
//        object FirstTime : ViewNavigation()
//    }
//
//    val navigationEvent = NavigationEvent<ViewNavigation>()
//
//    override fun setupViewModel() {
//        navigationEvent.postValue(ViewNavigation.FirstTime)
//        deepLinkTextColor = deepLinkInteractor.getDeepLinkTextColor()
//        deepLinkTextSize = deepLinkInteractor.getDeepLinkTextSize()
//    }
//
//    @Bindable
//    var deepLinkTextColor: Int = Color.BLACK
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.deepLinkTextColor)
//        }
//
//    @Bindable
//    var deepLinkTextSize: Float = 30f
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.deepLinkTextSize)
//        }
//
//    companion object {
//        private const val STATE_KEY = "DeepLinkSampleViewModelState"  // NON-NLS
//    }
//}