package com.atomicrobot.carbon.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.atomicrobot.carbon.ui.theme.CarbonAndroidTheme
import com.atomicrobot.carbon.ui.theme.Purple200
import com.atomicrobot.carbon.util.CommitPreviewProvider

@Composable
fun GitInfoNavigation(navController: NavController)
{
    GitInfoScreen()
}
@Composable
fun GitInfoScreen(scaffoldState: ScaffoldState = rememberScaffoldState()) {
    Scaffold(backgroundColor = Purple200) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }

//        Text(
//            text = commit.commitMessage,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Text(text = stringResource(id = R.string.author_format, commit.author))

}
