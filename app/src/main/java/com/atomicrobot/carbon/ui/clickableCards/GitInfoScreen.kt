package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.atomicrobot.carbon.BuildConfig
import com.atomicrobot.carbon.data.api.github.model.DetailedCommit
import com.atomicrobot.carbon.ui.components.BottomBar
import com.atomicrobot.carbon.ui.components.TopBar
import com.atomicrobot.carbon.util.DetailedCommitPreviewProvider

//@Composable
//fun GitInfoNavigation(navController: NavController)
//{
//    GitInfoScreen()
//}

@Preview
@Composable
fun GitInfoScreen(
    @PreviewParameter(DetailedCommitPreviewProvider::class, limit = 1) detailedCommit: DetailedCommit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    buildVersion: String = BuildConfig.VERSION_NAME,
    fingerprint: String = BuildConfig.VERSION_FINGERPRINT) {
    //TODO uncommenting view model causes preview to break
//    val viewModel: GitCardInfoViewModel = getViewModel()
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //TODO Passing Topbar unable to take advantage of navigation
//            TopBar(title = appBarTitle(navBackStackEntry),
//            buttonIcon = Icons.Filled.Menu,
//            onButtonClicked = {
//                scope.launch{
//                    scaffoldState.drawerState.open()
//                }
//            })
            TopBar(title = "Git Info Temp TODO Title")
            LazyColumn(modifier = Modifier.weight(1f)){
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        text = "Message:\n" +
                                "${detailedCommit.detailedCommitMessage}"
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Author:\n" +
                                "${detailedCommit.detailedCommitAuthor}"
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Node ID:\n" +
                                "${detailedCommit.detailedCommitNode_id}"
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Html_url:\n" +
                                "${detailedCommit.detailedCommitHtml_url}"
                    )
                }
                item {

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "TreePath:\n" +
                                "${detailedCommit.detailedCommitTreePath}"
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Tree Type:\n" +
                                "${detailedCommit.detailedCommitTreeType}"
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Tree Size:\n" +
                                "${detailedCommit.detailedCommitTreeSize}"
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Tree URL:\n" +
                                "${detailedCommit.detailedCommitTreeURL}"
                    )
                }

            }
            BottomBar(
                buildVersion = buildVersion,
                fingerprint = fingerprint
            )
        }
    }

}


