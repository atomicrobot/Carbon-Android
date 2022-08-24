package com.atomicrobot.carbon.ui.lumen.scenes

import android.app.Application
import androidx.lifecycle.ViewModel
import com.atomicrobot.carbon.data.lumen.Scene
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ScenesViewModel(private val app: Application) : ViewModel() {

    val scenes: Flow<List<Scene>> = flow { }
}