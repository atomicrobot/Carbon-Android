package com.atomicrobot.carbon.ui.designSystems

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atomicrobot.carbon.ui.components.DesignSystemDetailNavigation
import com.atomicrobot.carbon.ui.designSystems.DesignSystemViewModel.Event
import com.atomicrobot.carbon.ui.components.DesignSystemTopBarActions
import com.atomicrobot.carbon.ui.navigation.AppBarState
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.atomicrobot.carbon.util.LocalActivity

@Composable
fun DesignSystemDetailScreen(
    designSystemViewModel: DesignSystemViewModel = hiltViewModel(LocalActivity.current),
    detailCategory: String?,
    onBack: () -> Unit,
    onUpdateAppBarState: (AppBarState) -> Unit
) {

    val designSystemState by designSystemViewModel.designSystemState.collectAsState()

    BackHandler(
        enabled = true,
        onBack = onBack
    )

    LaunchedEffect(Unit){
        onUpdateAppBarState(
            AppBarState(
                title = detailCategory ?: "",
                navigation = {
                     DesignSystemDetailNavigation(
                         onBackToSystemClicked = onBack
                     )
                },
                actions = {
                    DesignSystemTopBarActions(
                        onDarkModeToggled = {
                            designSystemViewModel.applyAction(Event.ToggleDarkMode(it))
                        },
                        onFontScaleSet = {
                            designSystemViewModel.applyAction(Event.SetFontScale(it))
                        },
                        fontScale = designSystemState.fontScale,
                        darkMode = designSystemState.isInDarkMode
                    )
                }
            )
        )
    }

    CarbonAndroidTheme(
        darkTheme = designSystemState.isInDarkMode,
        testingFontScale = designSystemState.fontScale
    ){
        val composables = selectedComposableSet(
            category = detailCategory,
            isInDarkMode = designSystemState.isInDarkMode
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(composables) { composable ->
                        composable()
                    }
                }
            }
        }
    }

}

@Composable
fun selectedComposableSet(
    category: String?,
    isInDarkMode: Boolean
): List<@Composable () -> Unit> {

    return when (category){
        Atom.COLORS.category -> getColorSchemeComposables(isInDarkMode)
        Atom.TYPOGRAPHY.category -> getTypographyComposables()
        Atom.FONTS.category -> getFontComposables()
        Atom.SHAPES.category -> getShapeComposables()
        Molecule.BUTTONS.category -> getButtonComposables()
        Molecule.TEXTFIELDS.category -> getTextFieldComposables()
        Molecule.PROGRESS.category -> getProgressComposables()
        Molecule.BARS.category -> getBarComposables()
        else -> listOf()
    }
}