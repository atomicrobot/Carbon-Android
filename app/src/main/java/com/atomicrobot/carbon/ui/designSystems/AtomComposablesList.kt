package com.atomicrobot.carbon.ui.designSystems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atomicrobot.carbon.ui.theme.CarbonPalette
import okhttp3.internal.toHexString
import timber.log.Timber
import kotlin.reflect.full.*

@Composable
fun getColorSchemeComposables(
    appliedColorMap: Map<String,String>
): List<@Composable () -> Unit> {

    val carbonColors = CarbonPalette.values().map {
        Pair(it.camelCase,it.color)
    }

    val themeColors = MaterialTheme.colorScheme::class.memberProperties.map {
        Pair(it.name, it.getter.call(MaterialTheme.colorScheme) as Color)
    }

    val paletteColors: @Composable () -> Unit = {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "PALETTE")

            Spacer(modifier = Modifier.height(4.dp))

            carbonColors.chunked(2).forEach { couple ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    ColorSwatchItem(
                        modifier = Modifier.weight(1f),
                        color = couple[0].second,
                        primaryName = couple[0].first,
                        hex = couple[0].second.toArgb().toHexString()
                    )
                    if (couple.size > 1) {
                        Spacer(modifier = Modifier.width(8.dp))

                        ColorSwatchItem(
                            modifier = Modifier.weight(1f),
                            color = couple[1].second,
                            primaryName = couple[1].first,
                            hex = couple[1].second.toArgb().toHexString()
                        )
                    }
                }
            }
        }
    }

    val spacer: @Composable () -> Unit = {
        Spacer(modifier = Modifier.height(32.dp))
    }

    val semanticColors: @Composable () -> Unit = {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "SEMANTIC")

            Spacer(modifier = Modifier.height(4.dp))

            themeColors.chunked(2).forEach { couple ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    ColorSwatchItem(
                        modifier = Modifier.weight(1f),
                        color = couple[0].second,
                        primaryName = couple[0].first,
                        hex = couple[0].second.toArgb().toHexString(),
                        secondaryName = appliedColorMap[couple[0].first] ?: "Default"
                    )

                    if (couple.size > 1) {
                        Spacer(modifier = Modifier.width(8.dp))

                        ColorSwatchItem(
                            modifier = Modifier.weight(1f),
                            color = couple[1].second,
                            primaryName = couple[1].first,
                            hex = couple[1].second.toArgb().toHexString(),
                            secondaryName = appliedColorMap[couple[1].first] ?: "Default"
                        )
                    }
                }
            }
        }
    }

    return listOf(paletteColors, spacer, semanticColors)
}

@Composable
fun ColorSwatchItem(
    modifier: Modifier = Modifier,
    color: Color,
    primaryName: String,
    hex: String,
    secondaryName: String? = null
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = primaryName,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "#$hex",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.W400,
                    color = Color.DarkGray
                )
            )
            secondaryName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.DarkGray
                    )
                )
            }
        }
    }

}

@Composable
fun getTypographyComposables(): List<@Composable () -> Unit> {

    return MaterialTheme.typography::class.memberProperties.map {
        Pair(it.getter.call(MaterialTheme.typography) as TextStyle, it.name)
    }.sortedByDescending { it.first.fontSize.value }.map { stylePair ->
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = stylePair.second,
                    style = stylePair.first
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "font family: ${stylePair.first.fontFamily.toString().split(".")[1]}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "font size: ${stylePair.first.fontSize}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = "weight: ${stylePair.first.fontWeight?.weight}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "line height: ${stylePair.first.lineHeight}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
            }
        }
    }
}

@Composable
fun getFontComposables(): List<@Composable () -> Unit> {

    return FontFamily.Companion::class.memberProperties.map {
        Timber.d("PPPP, ${it.getter.call(FontFamily.Companion)}")
        Pair(it.getter.call(FontFamily.Companion) as FontFamily, it.name)
    }.map {
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.weight(2f),
                    text = it.second,
                    style = TextStyle(
                        fontFamily = it.first,
                        fontSize = 22.sp
                    )
                )

                Divider(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 16.dp)
                        .width(1.dp)
                )

                Text(
                    modifier = Modifier.weight(1f),
                    text = "framework",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


