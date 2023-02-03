package com.atomicrobot.carbon.ui.about

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.ui.components.NavigationTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutHtmlScreen(
    modifier: Modifier = Modifier,
    onNavIconClicked: () -> Unit,
) {
    val mUrl = "https://atomicrobot.com/about/"
    Scaffold(
        topBar = {
            NavigationTopBar(
                title = stringResource(id = CarbonScreens.AboutHtml.title),
                navigationIcon = Icons.Filled.ArrowBack,
                onNavigationIconClicked = onNavIconClicked
            )
        },
        modifier = modifier,
    ) {
        AndroidView(
            modifier = Modifier.padding(it),
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    loadUrl(mUrl)
                }
            },
            update = {
                it.loadUrl(mUrl)
            }
        )
    }
}
