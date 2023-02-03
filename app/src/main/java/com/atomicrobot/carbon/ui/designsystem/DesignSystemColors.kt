package com.atomicrobot.carbon.ui.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.designsystem.theme.FinAllyColors
import com.atomicrobot.carbon.ui.designsystem.theme.FinAllyShapeTokens
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import org.koin.androidx.compose.getViewModel

//region Composables
//region Composable Extensions
fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    // Hack to make to create headers in a LazyGrid composable
    item(
        span = {
            GridItemSpan(this.maxLineSpan)
        },
        content = content
    )
}
//endregion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemColorsScreen(
    modifier: Modifier = Modifier,
    designSystemVM: DesignSystemViewModel = getViewModel(),
    onNavIconClicked: () -> Unit,
) {
    val screenState: DesignSystemViewModel.ScreenState by designSystemVM.uiState.collectAsState()
    CarbonAndroidTheme(
        darkTheme = screenState.darkMode,
        fontScale = screenState.fontScale.scale,
    ) {
        Scaffold(
            topBar = {
                DesignScreenAppBar(
                    title = stringResource(id = DesignSystemScreens.Colors.title),
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
            modifier = modifier
        ) { it ->
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                columns = GridCells.Fixed(2)
            ) {
                header {
                    Text(
                        // Text isn't a string resource because this is a placeholder until the
                        // design team comes up with a new color palette
                        text = "FinAlly Color Palette",
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                items(
                    items = designSystemVM.finAllySwatches,
                    key = {
                        // This only works if colors aren't repeated
                        it.second
                    }
                ) {
                    HorizontalColorSwatch(colorName = it.first, color = it.second)
                }
            }
        }
    }
}

@Composable
fun VerticalColorSwatch(colorName: String, color: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        val colr = remember(color) {
            Color(color)
        }
        Box(
            modifier = Modifier
                .background(
                    colr,
                    shape = FinAllyShapeTokens.ColorSwatch
                )
                .size(80.dp)
        )
        Text(
            text = colorName,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall,
        )

        val hexColor = if (colr.alpha >= 1.0F)
            "#${"%X".format(colr.toArgb()).substring(startIndex = 2)}"
        else "#%X".format(colr.toArgb())

        Text(
            text = hexColor,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun HorizontalColorSwatch(colorName: String, colorRole: String? = null, color: Int) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val colr = remember(color) {
            Color(color)
        }
        Box(
            modifier = Modifier
                .background(
                    colr,
                    shape = FinAllyShapeTokens.ColorSwatch
                )
                .size(80.dp)
        )
        Column {
            Text(
                text = colorName,
                style = MaterialTheme.typography.titleSmall,
            )
            // Only display the alpha values if not full opague
            val hexColor = if (colr.alpha >= 1.0F)
                "#${"%X".format(colr.toArgb()).substring(startIndex = 2)}"
            else "#%X".format(colr.toArgb())
            Text(
                text = hexColor,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall
            )

            colorRole?.let {
                Text(
                    text = colorRole,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
//endregion

//region Composable Previews
@Preview
@Composable
fun VerticalColorSwatchPreview() {
    VerticalColorSwatch(colorName = "Green/900", color = FinAllyColors.green900.toArgb())
}

@Preview
@Composable
fun HorizontalColorSwatchPreview() {
    HorizontalColorSwatch(colorName = "Red/400", color = FinAllyColors.red400.toArgb())
}
//endregion
