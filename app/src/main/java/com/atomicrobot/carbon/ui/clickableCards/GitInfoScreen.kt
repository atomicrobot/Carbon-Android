package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.atomicrobot.carbon.ui.components.BottomBar
import com.atomicrobot.carbon.ui.components.TopBar

@Composable
fun GitInfoNavigation(navController: NavController)
{
    GitInfoScreen()
}
@Composable
@Preview
fun GitInfoScreen(scaffoldState: ScaffoldState = rememberScaffoldState()) {
    //TODO uncommenting view model causes preview to break
//    val viewModel: GitCardInfoViewModel = getViewModel()
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar("Git Info Test Title")
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                text = "Test text"
            )
//            ) {
//                Text(text = "Some Test Text")
//            }
            BottomBar()
        }
    }


}
