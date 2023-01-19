package com.atomicrobot.carbon.ui.designSystems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.reflect.full.memberProperties

@Composable
fun getColorSchemeComposables(): List<@Composable () -> Unit> {
    return MaterialTheme.colorScheme::class.memberProperties.map { property ->
        Pair(property.getter.call(MaterialTheme.colorScheme),property.name)
    }.map {
        {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = it.first as Color
                            )
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = it.second
                    )
                }
            }
        }
    }
}
