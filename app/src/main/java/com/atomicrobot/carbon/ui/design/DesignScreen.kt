package com.atomicrobot.carbon.ui.design

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atomicrobot.carbon.navigation.DesignScreens

var designOptions = listOf<DesignScreens>(DesignScreens.Lumen)

@Preview
@Composable
fun DesignScreen(onDesignScreenSelected: (DesignScreens) -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top)
    ) {
        designOptions.forEach {
            DesignRow(
                Modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp),
                it,
                onDesignScreenSelected
            )
        }
    }
}

@Composable
fun DesignRow(
    modifier: Modifier = Modifier,
    option: DesignScreens = DesignScreens.Lumen,
    onDesignScreenSelected: (DesignScreens) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            .clickable { onDesignScreenSelected(option) }
    ) {
        Text(
            text = option.title,
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)
        )
    }
}
