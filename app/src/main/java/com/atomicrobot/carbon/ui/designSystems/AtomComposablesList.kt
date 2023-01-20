package com.atomicrobot.carbon.ui.designSystems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atomicrobot.carbon.ui.theme.CarbonColors
import com.atomicrobot.carbon.ui.theme.Typography
import okhttp3.internal.toHexString
import timber.log.Timber
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.memberProperties

@Composable
fun getColorSchemeComposables(): List<@Composable () -> Unit> {

    val carbonColors = CarbonColors::class.companionObject?.memberProperties?.toList()

    return MaterialTheme.colorScheme::class.memberProperties.map { property ->
        Pair(property.getter.call(MaterialTheme.colorScheme),property.name)
    }.map {
        val themeColor = it.first as Color
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = themeColor
                        )
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    modifier = Modifier.weight(1f),
                    text = it.second,
                    style = Typography.bodyMedium
                )

                Divider(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 16.dp)
                        .width(1.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    carbonColors?.firstOrNull { property ->
                        (property.getter.call(CarbonColors.Companion) as Color).toArgb() == themeColor.toArgb()
                    }.let { localColor ->
                        Text(
                            text = localColor?.name ?: "Not Provided",
                            style = Typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "#${themeColor.toArgb().toHexString()}",
                        style = Typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun getTypographyComposables(): List<@Composable () -> Unit> {

    return MaterialTheme.typography::class.memberProperties.map {
        Pair(it.getter.call(Typography) as TextStyle,it.name)
    }.map{ stylePair ->
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.weight(1f),
                    text = stylePair.second,
                    style = stylePair.first
                )

                Divider(
                    modifier = Modifier
                        .height(96.dp)
                        .padding(horizontal = 16.dp)
                        .width(1.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${stylePair.first.fontFamily}",
                        style = Typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "fontSize: ${stylePair.first.fontSize}",
                        style = Typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${stylePair.first.fontWeight}",
                        style = Typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "lineHeight: ${stylePair.first.lineHeight}",
                        style = Typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun getFontComposables(): List<@Composable () -> Unit> {

    return FontFamily.Companion::class.memberProperties.map {
        Timber.d("PPPP, ${it.getter.call(FontFamily.Companion)}")
        Pair(it.getter.call(FontFamily.Companion) as FontFamily,it.name)
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
                    style = Typography.bodyMedium
                )
            }
        }
    }
}


