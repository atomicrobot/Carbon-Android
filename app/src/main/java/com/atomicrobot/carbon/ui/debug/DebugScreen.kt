package com.atomicrobot.carbon.ui.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Podcasts
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Shortcut
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class DebugTabs(val title: String) {
    object Default: DebugTabs("Default Composables")
    object Custom: DebugTabs("Custom Composables")
}

val debugTabs = listOf(
    DebugTabs.Default,
    DebugTabs.Custom,
)

//region Composables
@OptIn(ExperimentalPagerApi::class)
@Composable
fun DebugScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        val scope = rememberCoroutineScope()
        val pagerState: PagerState = rememberPagerState()
        DebugTabs(debugTabs, pagerState, scope)
        HorizontalPager(
            count = debugTabs.size,
            state = pagerState,
        ) { page ->
            when(debugTabs[page]){
                DebugTabs.Default -> DefaultComposables()
                DebugTabs.Custom -> CustomComposables()
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DebugTabs(
    tabs: List<DebugTabs>,
    pagerState: PagerState,
    scope: CoroutineScope,
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = {
            TabRowDefaults.Indicator(Modifier.tabIndicatorOffset(it[pagerState.currentPage]))
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(page = index)
                    }
                },
                text = {
                    Text(text = tab.title)
                },
            )
        }
    }
}

//region Default Material 3 Composables
@Composable
fun DefaultComposables() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        this.defaultMat3Badge()
        this.defaultNavigationBar()
        this.defaultMat3BottomAppBar()
        this.defaultMat3Buttons()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun LazyListScope.defaultMat3Badge() {
    item {
        Text("Badges", style = MaterialTheme.typography.headlineSmall)
        NavigationBar {
            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = {
                    BadgedBox(badge = {
                        Badge {
                            Text("999+")
                        }
                    }) {
                        Icon(
                            Icons.Filled.Mail,
                            contentDescription = "Mail"
                        )
                    }
                },
                label = {
                    Text("Mail")
                }
            )

            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    BadgedBox(badge = {
                        Badge() {
                            Text("10")
                        }
                    }) {
                        Icon(
                            Icons.Outlined.Chat,
                            contentDescription = "Chat"
                        )
                    }
                },
                label = {
                    Text("Chat")
                }
            )

            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    BadgedBox(badge = {
                        Badge()
                    }) {
                        Icon(
                            Icons.Outlined.Groups,
                            contentDescription = "Rooms"
                        )
                    }
                },
                label = {
                    Text("Rooms")
                }
            )

            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    BadgedBox(badge = {
                        Badge() {
                            Text("3")
                        }
                    }) {
                        Icon(
                            Icons.Outlined.Videocam,
                            contentDescription = "Meet"
                        )
                    }
                },
                label = {
                    Text("Meet")
                }
            )
        }
    }
}

fun LazyListScope.defaultNavigationBar() {
    item {
        Text("Navigation Bar", style = MaterialTheme.typography.headlineSmall)
        NavigationBar {
            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Home"
                    )
                },
                label = {
                    Text("Home")
                }
            )

            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    Icon(
                        Icons.Outlined.LibraryMusic,
                        contentDescription = "Music"
                    )
                },
                label = {
                    Text("Music")
                }
            )

            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    Icon(
                        Icons.Outlined.Podcasts,
                        contentDescription = "Podcast"
                    )
                },
                label = {
                    Text("Podcast")
                }
            )
        }
    }
}

fun LazyListScope.defaultMat3BottomAppBar() {
    item {
        Text("Bottom App Bar", style = MaterialTheme.typography.headlineSmall)
        BottomAppBar(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 8.dp, end = 16.dp)
        ) {
            IconButton(onClick = { }, enabled = true) {
                Icon(Icons.Outlined.Search, contentDescription = "Localized description")
            }

            IconButton(onClick = { }, enabled = true) {
                Icon(Icons.Outlined.Delete, contentDescription = "Localized description")
            }

            IconButton(onClick = { }, enabled = true) {
                Icon(Icons.Outlined.Archive, contentDescription = "Localized description")
            }

            IconButton(onClick = { }, enabled = true) {
                Icon(Icons.Outlined.Shortcut, contentDescription = "Localized description")
            }

            Spacer(modifier = Modifier.weight(1f))

            FloatingActionButton(onClick = { }) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    }
}
fun LazyListScope.defaultMat3Buttons() {
    item {
        Text("Buttons", style = MaterialTheme.typography.headlineSmall)
        /* Icon Buttons */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilledIconButton(onClick = { }, enabled = true) {
                Icon(Icons.Outlined.Lock, contentDescription = "Localized description")
            }
            FilledIconButton(onClick = { }, enabled = false) {
                Icon(Icons.Outlined.Lock, contentDescription = "Localized description")
            }
            IconButton(onClick = { }, enabled = true) {
                Icon(Icons.Outlined.Lock, contentDescription = "Localized description")
            }
            IconButton(onClick = { }, enabled = false) {
                Icon(Icons.Outlined.Lock, contentDescription = "Localized description")
            }
        }
        /* Filled Buttons */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { }, enabled = true) {
                Text(text = "Filled")
            }
            Button(onClick = { }, enabled = true) {
                Icon(
                    imageVector = Icons.Filled.AddAPhoto,
                    contentDescription = "Add Photo")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Icon")
            }
            Button(onClick = { }, enabled = false) {
                Text(text = "Disabled")
            }
        }
        /* Outlined Buttons */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = { }, enabled = true) {
                Text(text = "Outlined")
            }
            OutlinedButton(onClick = { }, enabled = true) {
                Icon(
                    imageVector = Icons.Filled.AddAPhoto,
                    contentDescription = "Add Photo")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Icon")
            }
            OutlinedButton(onClick = { }, enabled = false) {
                Text(text = "Disabled")
            }
        }

        Card() {

        }
        /* FAB Buttons */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Filled.Add, "Add")
            }

            ExtendedFloatingActionButton(onClick = { }) {
                Icon(Icons.Filled.Add, "Add")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Extended FAB")
            }
        }
    }
}
//endregion

//region Custom AR Composables
@Composable
fun CustomComposables() {

}
//endregion
//endregion

//region Composable Previews
@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun DebugTabsPreview() {
    CarbonAndroidTheme {
        val scope = rememberCoroutineScope()
        val pagerState: PagerState = rememberPagerState()
        DebugTabs(debugTabs, pagerState, scope)
    }
}

@Preview
@Composable
fun DefaultMat3BadgePreview() {
    LazyColumn {
        defaultMat3Badge()
    }
}

@Preview
@Composable
fun DefaultNavigationBarPreview() {
    LazyColumn {
        defaultNavigationBar()
    }
}

@Preview
@Composable
fun DefaultBottomAppBarPreview() {
    LazyColumn {
        defaultMat3BottomAppBar()
    }
}

@Preview
@Composable
fun DefaultMat3ButtonsPreview() {
    LazyColumn {
        defaultMat3Buttons()
    }
}

//@Preview
//@Composable
//fun DebugScreenPreview() {
//    DebugScreen()
//}

//endregion