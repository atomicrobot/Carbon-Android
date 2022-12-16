package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.material.Icon as MaterialIcon

/**
 * Reusable UI components for the Carbon-Android app
 */
object AtomicRobotUI {
    object Button {
        @Composable
        fun Outlined(
            text: String? = null,
            onClick: () -> Unit,
            enabled: Boolean = true,
            modifier: Modifier = Modifier
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
            painter: Painter? = null,
            imageBitmap: ImageBitmap? = null,
            imageVector: ImageVector? = null,
            contentDescription: String? = null,
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
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
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
            )
        }
    }
}

@Preview
@Composable
fun TransparentTextFieldPreview() {
    AtomicRobotUI.TextField.TransparentTextField()
}