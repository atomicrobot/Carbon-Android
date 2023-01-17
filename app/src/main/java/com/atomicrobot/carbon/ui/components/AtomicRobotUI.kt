package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import androidx.compose.material3.Icon as MaterialIcon

/**
 * Reusable UI components for the Carbon-Android app
 */
object AtomicRobotUI {
    object Button {
        @Composable
        fun Outlined(
            modifier: Modifier = Modifier,
            text: String? = null,
            onClick: () -> Unit,
            enabled: Boolean = true,
        ) {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled,
                modifier = modifier,
                content = {
                    text?.let {
                        Text(text = text)
                    }
                }
            )
        }

        /**
         *  SAMPLE CUSTOM ICON BUTTON IMPLEMENTATION
         */
        @Composable
        fun Icon(
            modifier: Modifier = Modifier,
            painter: Painter? = null,
            imageBitmap: ImageBitmap? = null,
            imageVector: ImageVector? = null,
            contentDescription: String? = null,
            onClick: () -> Unit,
            enabled: Boolean = true,
            interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        ) {
            IconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                interactionSource = interactionSource,
                content = {
                    when {
                        painter != null -> {
                            MaterialIcon(painter = painter, contentDescription = contentDescription)
                        }
                        imageBitmap != null -> {
                            MaterialIcon(
                                bitmap = imageBitmap,
                                contentDescription = contentDescription
                            )
                        }
                        imageVector != null -> {
                            MaterialIcon(
                                imageVector = imageVector,
                                contentDescription = contentDescription
                            )
                        }
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    object TextField {
        @Composable
        fun TransparentTextField(
            modifier: Modifier = Modifier,
            value: String = stringResource(id = R.string.txtField_placeholder),
            labelResId: Int = R.string.label_placeholder,
            onValueChanged: (String) -> Unit = { _ -> }
        ) {
            TextField(
                value = value,
                onValueChange = onValueChanged,
                label = { Text(text = stringResource(id = labelResId)) },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
            )
        }
    }
}

@Preview
@Composable
fun TransparentTextFieldPreview() {
    AtomicRobotUI.TextField.TransparentTextField()
}
