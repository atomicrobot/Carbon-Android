package com.atomicrobot.carbon.ui.designSystems

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemsScreen() {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val pages = immutableListOf(
        stringResource(id = R.string.design_systems_framework),
        stringResource(id = R.string.design_systems_custom)
    )

    Column() {
        TabContent(
            pages = pages,
            pagerState = pagerState,
            scope = scope
        )
        PagerContent(
            pages = pages,
            pagerState = pagerState
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(
    pages: List<String>,
    pagerState: PagerState,
    scope: CoroutineScope,
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = {
            TabRowDefaults.Indicator(Modifier.tabIndicatorOffset(it[pagerState.currentPage]))
        }
    ) {
        pages.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(page = index)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerContent(
    pages: List<String>,
    pagerState: PagerState
) {
    HorizontalPager(
        count = pages.size,
        state = pagerState,
    ) { page ->
        IndividualPageContent(page = page)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualPageContent(page: Int) {

    val composablesByPage = immutableListOf(
        immutableListOf(
            Pair(Badge(
                modifier = Modifier.size(48.dp),
                content = { Text(text = "10") }
            ),
                "Badge"
            ),
            Pair(BottomAppBar(
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "test"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "test"
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "test"
                        )
                    }
                }
            ),
                "BottomAppBar"
            )
        ),
        immutableListOf()
    )

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(composablesByPage[page]) { pair ->
            IndividualContentItem(
                composableItem = { pair.first },
                //composableName = pair.second
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun IndividualContentItem(
    composableItem: @Composable () -> Unit
) {
    Card(

    ) {

    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        composableItem()
    }

}