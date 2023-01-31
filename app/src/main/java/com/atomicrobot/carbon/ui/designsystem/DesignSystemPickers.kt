package com.atomicrobot.carbon.ui.designsystem

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemPickersScreen(
    modifier: Modifier,
    designSystemVM: DesignSystemViewModel = getViewModel(),
    onNavIconClicked: () -> Unit,
    ) {
    val screenState: DesignSystemViewModel.ScreenState by designSystemVM.uiState.collectAsState()
    CarbonAndroidTheme(
        darkTheme = screenState.darkMode,
        fontScale = screenState.fontScale.scale,
    ) {
        Scaffold(
            topBar = {
                DesignScreenAppBar(
                    title = stringResource(id = DesignSystemScreens.Pickers.title),
                    screenState.darkMode,
                    selectedFontScale = screenState.fontScale,
                    onBackPressed = onNavIconClicked,
                    onFontScaleChanged = {
                        designSystemVM.updateFontScale(it)
                    },
                    onDarkModeChanged = {
                        designSystemVM.enabledDarkMode(it)
                    }
                )
            },
            modifier = Modifier,
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    val ctx = LocalContext.current
                    Button(
                        onClick = {
                            Toast
                                .makeText(
                                    ctx,
                                    "Material 3 Date Picker not yet implemented in Jetpack Compose",
                                    Toast.LENGTH_SHORT
                                ).show()
                        },
                        modifier = modifier,
                        enabled = true
                    ) { Text(text = "Date Picker") }
                }
                item {
                    val ctx = LocalContext.current
                    Button(
                        onClick = {
                            Toast
                                .makeText(
                                    ctx,
                                    "Material 3 Time Picker not yet implemented in Jetpack Compose",
                                    Toast.LENGTH_SHORT
                                ).show()
                        },
                        modifier = modifier,
                        enabled = true
                    ) { Text(text = "Time Picker") }
                }
            }
        }
    }
}