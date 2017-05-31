package com.mycompany.myapp.ui.main

import android.databinding.BaseObservable
import com.mycompany.myapp.data.api.github.model.Commit
import java.util.*

data class SampleFragmentState(val username: String = "madebyatomicrobot",
                               val repository: String = "android-starter-project",
                               val versionCode: String = "",
                               val versionFingerPrint: String = "Hello",
                               val errorMessage: String? = null,
                               val commits: List<Commit> = ArrayList()) : BaseObservable() {


}