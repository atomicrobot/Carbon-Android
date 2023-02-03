package com.atomicrobot.carbon.ui.designSystems

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun getProgressComposables(): List<@Composable () -> Unit> {

    return listOf { ProgressComposableContentItem(section = ProgressType.CIRCULAR_PROGRESS) }
}

@Composable
fun ProgressComposableContentItem(
    section: ProgressType
) {

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = section.sectionTitle,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        CircularProgressIndicator()
    }
}

enum class ProgressType(
    val sectionTitle: String
) {
    CIRCULAR_PROGRESS(
        sectionTitle = "Circular Progress"
    )
}