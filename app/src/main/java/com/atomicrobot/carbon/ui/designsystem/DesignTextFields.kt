package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import org.koin.androidx.compose.getViewModel

//region TextField Composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignTextFieldsScreen(
    modifier: Modifier = Modifier,
    designSystemVM: DesignSystemViewModel = getViewModel(),
    onNavIconClicked: () -> Unit
) {
    val screenState: DesignSystemViewModel.ScreenState by designSystemVM.uiState.collectAsState()
    CarbonAndroidTheme(
        darkTheme = screenState.darkMode,
        fontScale = screenState.fontScale.scale,
    ) {
        Scaffold(
            topBar = {
                DesignScreenAppBar(
                    title = stringResource(id = DesignSystemScreens.TextFields.title),
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
            modifier = modifier,
        ) {
            LazyColumn(
                Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item(key = "Text (Outlined)") {
                    OutlinedTextRow("Text (Outlined)", enabled = true)
                }

                item(key = "Text (Outlined/Disabled)") {
                    OutlinedTextRow("Text (Outlined/Disabled)", enabled = false)
                }

                item(key = "Multiline (Outlined)") {
                    OutlinedTextRow("Multiline (Outlined)", enabled = true, multiLine = true)
                }

                item(key = "Multiline (Outlined/Disabled)") {
                    OutlinedTextRow(
                        "Multiline (Outlined/Disabled)",
                        enabled = false,
                        multiLine = true
                    )
                }

                item(key = "Text (Underlined)") {
                    UnderlinedTextRow("Text (Underlined)", enabled = true)
                }

                item(key = "Text (Underlined/Disabled)") {
                    UnderlinedTextRow("Text (Underlined/Disabled)", enabled = false)
                }

                item(key = "Multiline (Underlined)") {
                    UnderlinedTextRow("Multiline (Underlined)", enabled = true, multiLine = true)
                }

                item(key = "Multiline (Underlined/Disabled)") {
                    UnderlinedTextRow(
                        "Multiline (Underlined/Disabled)",
                        enabled = false,
                        multiLine = true
                    )
                }

                item(key = "Password (Underlined)") {
                    UnderlinedPasswordRow()
                }

                item(key = "Password (Outlined)") {
                    OutlinedPasswordRow()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextRow(
    labelText: String,
    enabled: Boolean = true,
    multiLine: Boolean = false,
) {
    var text by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = if(multiLine) Modifier
            .fillMaxWidth()
            .height(112.dp)
        else Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = !multiLine,
        label = { Text(labelText) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnderlinedTextRow(
    labelText: String,
    enabled: Boolean = true,
    multiLine: Boolean = false,
) {
    var text by rememberSaveable { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = if(multiLine) Modifier
            .fillMaxWidth()
            .height(112.dp)
        else Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        label = { Text(labelText) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnderlinedPasswordRow() {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = password,
        onValueChange = { password = it },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        enabled = true,
        singleLine = true,
        label = {
            Text("Password")
        },
        trailingIcon = {
            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(
                    imageVector = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Outlined.VisibilityOff,
                    contentDescription =  if (passwordVisible) "Hide password"
                    else "Show password"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedPasswordRow() {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        enabled = true,
        singleLine = true,
        label = {
            Text("Password")
        },
        trailingIcon = {
            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(
                    imageVector = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Outlined.VisibilityOff,
                    contentDescription =  if (passwordVisible) "Hide password"
                    else "Show password"
                )
            }
        }
    )
}

//endregion

//region TextField Composable Previews
@Preview
@Composable
fun OutlinedTextRowPreview() = CarbonAndroidTheme {
    OutlinedTextRow("Outlined")
}

@Preview
@Composable
fun UnderlinedTextRowPreview() = CarbonAndroidTheme {
    UnderlinedTextRow("Underlined")
}

@Preview
@Composable
fun OutlinedPasswordRowPreview() = CarbonAndroidTheme {
    OutlinedPasswordRow()
}

@Preview
@Composable
fun UnderlinedPasswordRowPreview() = CarbonAndroidTheme {
    UnderlinedPasswordRow()
}
//endregion