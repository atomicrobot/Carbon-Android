package com.atomicrobot.carbon.util.progress

import android.content.Context
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.app.CoreApplication
import com.atomicrobot.carbon.app.MainApplication


class TestableProgressBar : ProgressBar {
    constructor (context: Context) : super(context) {
        killAnimatedProgressBar(context)
    }

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        killAnimatedProgressBar(context)
    }

    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        killAnimatedProgressBar(context)
    }

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        killAnimatedProgressBar(context)
    }

    private fun killAnimatedProgressBar(context: Context) {
        if ((context.applicationContext as CoreApplication).isTesting()) {
            indeterminateDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher)
        }
    }
}