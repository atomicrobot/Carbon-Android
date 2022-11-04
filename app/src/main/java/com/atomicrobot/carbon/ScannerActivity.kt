package com.atomicrobot.carbon

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.ui.deeplink.DeepLinkSampleScreen
import com.atomicrobot.carbon.ui.scanner.ScannerScreen
import com.atomicrobot.carbon.ui.theme.ScannerTheme
import com.atomicrobot.carbon.util.LocalActivity
import com.google.mlkit.vision.barcode.common.Barcode
import timber.log.Timber

class ScannerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ScannerTheme {
                // Wrap the composable in a LocalActivity provider so our composable 'environment'
                // has access to Activity context/scope which is required for requesting permissions
                CompositionLocalProvider(LocalActivity provides this) {
                    Scaffold { innerPadding ->
                        NavHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            startDestination = "Scanner"
                        ) {
                            scannerFlowGraph(navController, this@ScannerActivity)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Nested nav. graph dedicated to displaying 'main' app content.
 */
@Suppress("UNUSED_PARAMETER")
fun NavGraphBuilder.scannerFlowGraph(
    navController: NavHostController,
    activityContext: ComponentActivity
) {
    navigation(startDestination = CarbonScreens.Scanner.route, route = "Scanner") {
        composable(CarbonScreens.Scanner.route) {
            ScannerScreen {
                when (it.valueType) {
                    Barcode.TYPE_URL -> {
                        val uri = Uri.parse(it.url!!.url)
                        when {
                            (uri.scheme.equals("atomicrobot") || uri.host?.contains(".atomicrobot.com") == true) -> {
                                navController.navigate(uri)
                                return@ScannerScreen
                            }
                        }
                    }
                    else -> { /* Intentionally left blank */ }
                }
                Toast.makeText(
                    activityContext,
                    "Barcode clicked: ${it.displayValue}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        composable(
            route = CarbonScreens.DeepLink.routeWithArgs,
            arguments = CarbonScreens.DeepLink.arguments,
            deepLinks = CarbonScreens.DeepLink.deepLink
        ) {
            val textColor = it.arguments?.getString("textColor")
            var color = Color.BLACK
            try {
                color = Color.parseColor(textColor)
            } catch (exception: IllegalArgumentException) {
                Timber.e("Unsupported value for color")
            }
            val textSize = it.arguments?.getString("textSize")
            var size = 30f
            if (!textSize.isNullOrEmpty()) {
                try {
                    size = textSize.toFloat()
                } catch (exception: NumberFormatException) {
                    Timber.e("Unsupported value for size")
                }
            }
            DeepLinkSampleScreen(
                textColor = color,
                textSize = size
            )
        }
    }
}
