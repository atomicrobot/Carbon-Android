package com.atomicrobot.carbon.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Icon as MaterialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R


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
            enabled: Boolean = true
        ) {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled,
                modifier = modifier,
                content = {
                    text?.let {
                        Text(text = text)
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
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
                            Icon(painter = painter, contentDescription = contentDescription)
                        }
                        imageBitmap != null -> {
                            MaterialIcon(
                                bitmap = imageBitmap,
                                contentDescription = contentDescription
                            )
                        }
                        imageVector != null -> {
                            Icon(
                                imageVector = imageVector,
                                contentDescription = contentDescription
                            )
                        }
                    }
                }
            )
        }

        @Composable
        fun DropdownMenuButton(
            modifier: Modifier = Modifier,
            fontScale: Float,
            expanded: Boolean,
            onClick: () -> Unit
        ) {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RectangleShape,
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                    end = 0.dp
                ),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${fontScale}x",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            contentDescription = "dropdown menu",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    }

    object TextField {
        @OptIn(ExperimentalMaterial3Api::class)
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
                label = {
                    Text(
                        text = stringResource(id = labelResId),
                    )
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

@Preview
@Composable
fun TransparentTextFieldPreview() {
    AtomicRobotUI.TextField.TransparentTextField()
}
