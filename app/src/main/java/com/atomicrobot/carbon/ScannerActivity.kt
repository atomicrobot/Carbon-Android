package com.atomicrobot.carbon

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.atomicrobot.carbon.ui.scanner.ScannerScreen
import com.atomicrobot.carbon.ui.theme.ScannerTheme
import com.atomicrobot.carbon.util.LocalActivity
import com.google.mlkit.vision.barcode.common.Barcode

class ScannerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ScannerTheme {
                // Wrap the composable in a LocalActivity provider so our composable 'environment'
                // has access to Activity context/scope which is required for requesting permissions
                CompositionLocalProvider(LocalActivity provides this) {
//                    ScannerScreen()
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
                            this,
                            "Barcode clicked: ${it.displayValue}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
