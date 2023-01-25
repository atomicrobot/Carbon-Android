package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.atomicrobot.carbon.util.splitCamelCase
import kotlin.reflect.full.memberProperties

//region Composables
@Composable
fun DesignTypographyScreen(
    modifier: Modifier = Modifier,
) {
    val types: List<Pair<String, TextStyle>> = androidx.compose.material3.Typography::class
        .memberProperties.map {
            val textStyleName = splitCamelCase(it.name)
            val textStyle = it.get(MaterialTheme.typography) as TextStyle
            Pair(textStyleName, textStyle)
        }
        .sortedWith(
            compareBy({ -it.second.fontSize.value }, { it.first })
        )
        .toList()

    LazyColumn(
        modifier = modifier
    ) {
        items(types) {
            TextStyleColumnItem(textStyleName = it.first, textStyle = it.second)
        }
    }
}

@Composable
fun TextStyleColumnItem(
    textStyleName: String,
    textStyle: TextStyle,
    infoTextStyle: TextStyle = MaterialTheme.typography.labelLarge
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 24.dp, bottom = 8.dp),
        ) {
            Text(
                text = textStyleName,
                style = textStyle
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val family = textStyle.fontFamily.toString()
                    .replace("FontFamily.", "")
                Text(
                    text = "Family: $family",
                    style = infoTextStyle
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Style: ${textStyle.fontStyle ?: "Null"}",
                    style = infoTextStyle
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val fntSize = if (textStyle.fontSize == TextUnit.Unspecified) "Unspecified"
                else textStyle.fontSize.toString()
                Text(
                    text = "Size: $fntSize",
                    style = infoTextStyle
                )

                Spacer(modifier = Modifier.width(16.dp))

                val lnHeight = if (textStyle.lineHeight == TextUnit.Unspecified) "Unspecified"
                else textStyle.lineHeight.toString()
                Text(
                    text = "Line Height: $lnHeight",
                    style = infoTextStyle
                )
            }
        }
    }
}
//endregion
//region Composable  Previews
@Preview("Label Small TextStyle")
@Composable
fun LabelSmallTextStyleColumnItemPreview() {
    CarbonAndroidTheme {
        TextStyleColumnItem(
            "labelSmall",
            MaterialTheme.typography.labelSmall
        )
    }
}

@Preview("Label Medium TextStyle")
@Composable
fun LabelMediumTextStyleColumnItemPreview() {
    CarbonAndroidTheme {
        TextStyleColumnItem(
            "labelMedium",
            MaterialTheme.typography.labelMedium
        )
    }
}

@Preview("Label Large TextStyle")
@Composable
fun LabelLargeTextStyleColumnItemPreview() {
    CarbonAndroidTheme {
        TextStyleColumnItem(
            "labelLarge",
            MaterialTheme.typography.labelLarge
        )
    }
}
//endregion
