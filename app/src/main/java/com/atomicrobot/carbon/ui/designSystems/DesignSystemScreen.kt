package com.atomicrobot.carbon.ui.designSystems

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.designSystems.DesignSystemScreenState.*
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import timber.log.Timber


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DesignSystemScreen(
    onDismiss: () -> Unit
) {

    var expandedMenu by remember { mutableStateOf(false) }

    var darkModeChecked by remember { mutableStateOf(false) }

    var textScale by remember { mutableStateOf(1.0) }

    var screenState: DesignSystemScreenState by remember { mutableStateOf(OverviewState) }

    BackHandler(
        enabled = true,
        onBack = {
            when (screenState) {
                is DetailState -> screenState = OverviewState
                is OverviewState -> onDismiss()
            }
        }
    )

    MaterialTheme.colorScheme::class.members.forEach {
        Timber.tag("pppp").d(it.name)
        Timber.tag("pppp")
            .d((it.returnType.toString() == "androidx.compose.ui.graphics.Color").toString())
    }

    CarbonAndroidTheme(darkTheme = darkModeChecked) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AdjustmentsRow(
                expandedMenu = expandedMenu,
                darkModeChecked = darkModeChecked,
                textScale = textScale,
                screenState = screenState,
                onMenuExpandedStateChanged = {
                    expandedMenu = it
                },
                onDarkModeChanged = {
                    darkModeChecked = it
                },
                onTextScaleChanged = {
                    textScale = it
                    expandedMenu = false
                },
                onReturnFromDetail = {
                    screenState = OverviewState
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = screenState,
                transitionSpec = {
                    if (targetState is DetailState) {
                        slideInHorizontally(initialOffsetX = { width -> width }) with
                                slideOutHorizontally(targetOffsetX = { width -> -width })
                    } else {
                        slideInHorizontally(initialOffsetX = { width -> -width }) with
                                slideOutHorizontally(targetOffsetX = { width -> width })
                    }
                }
            ) {
                when (screenState) {
                    is OverviewState -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            this.atomsGroup(
                                onAtomClicked = {
                                    screenState = DetailState(listOf())
                                }
                            )

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            this.moleculesGroup(
                                onMoleculeClicked = {
                                    screenState = DetailState(listOf())
                                }
                            )
                        }
                    }
                    is DetailState -> {

                        val composables = getColorSchemeComposables()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            items(composables) { composable ->
                                composable()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun LazyListScope.atomsGroup(
    onAtomClicked: (Atom) -> Unit
) {
    items(Atom.values()) { atom ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onAtomClicked(atom)
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                text = atom.category
            )
        }
    }
}

fun LazyListScope.moleculesGroup(
    onMoleculeClicked: (Molecule) -> Unit
) {
    items(Molecule.values()) { molecule ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onMoleculeClicked(molecule)
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                text = molecule.category
            )
        }
    }
}

@Composable
fun AdjustmentsRow(
    expandedMenu: Boolean,
    darkModeChecked: Boolean,
    textScale: Double,
    screenState: DesignSystemScreenState,
    onMenuExpandedStateChanged: (Boolean) -> Unit,
    onDarkModeChanged: (Boolean) -> Unit,
    onTextScaleChanged: (Double) -> Unit,
    onReturnFromDetail: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (screenState is DetailState) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onReturnFromDetail
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Return to Overview"
                    )
                }
            }
        }

        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            OutlinedButton(
                onClick = { onMenuExpandedStateChanged(!expandedMenu) },
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
                            text = "${textScale}x Text Scale",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "dropdown menu",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )

            DropdownMenu(
                expanded = expandedMenu,
                onDismissRequest = { onMenuExpandedStateChanged(false) }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "0.75x"
                        )
                    },
                    onClick = {
                        onTextScaleChanged(0.75)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "1.0x"
                        )
                    },
                    onClick = {
                        onTextScaleChanged(1.0)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "1.5x"
                        )
                    },
                    onClick = {
                        onTextScaleChanged(1.5)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "2.0x"
                        )
                    },
                    onClick = {
                        onTextScaleChanged(2.0)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(
            checked = darkModeChecked,
            onCheckedChange = onDarkModeChanged
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Dark Mode",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

enum class Atom(val category: String) {
    COLORS(category = "Colors"),
    TYPOGRAPHY(category = "Typography"),
    FONTS(category = "Fonts"),
    LOGOS(category = "Logos"),
    ICONS(category = "Icons")
}

enum class Molecule(val category: String) {
    BUTTONS(category = "Buttons"),
    CHECKBOXES(category = "Checkboxes"),
    RADIOS(category = "Radios"),
    SWITCHES(category = "Switches"),
    SLIDERS(category = "Sliders"),
    PICKERS(category = "Pickers"),
    TEXTFIELDS(category = "Text Fields"),
    BARS(category = "Bars")
}

sealed class DesignSystemScreenState {
    object OverviewState : DesignSystemScreenState()
    class DetailState(val composables: List<@Composable () -> Unit>) : DesignSystemScreenState()
}