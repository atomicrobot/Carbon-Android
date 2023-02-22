package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.atomicrobot.carbon.R
import com.atomicrobot.carbon.data.api.github.model.Author
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.ui.components.BottomBar
import com.atomicrobot.carbon.ui.components.TopBar
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.atomicrobot.carbon.ui.theme.Purple200
import com.atomicrobot.carbon.util.CommitPreviewProvider

@Composable
fun GitInfoNavigation(navController: NavController)
{
    GitInfoScreen()
}
@Composable
@Preview
fun GitInfoScreen(scaffoldState: ScaffoldState = rememberScaffoldState()) {
    Scaffold(backgroundColor = Purple200) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar("Git Info Test Title")
            Card(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = "Some Test Text")
            }
            BottomBar()
        }
    }

//        Text(
//            text = commit.commitMessage,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Text(text = stringResource(id = R.string.author_format, commit.author))

}
