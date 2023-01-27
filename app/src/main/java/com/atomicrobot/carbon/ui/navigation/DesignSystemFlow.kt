package com.atomicrobot.carbon.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import com.atomicrobot.carbon.navigation.CarbonScreens
import com.atomicrobot.carbon.ui.designSystems.DesignSystemDetailScreen
import com.atomicrobot.carbon.ui.designSystems.DesignSystemScreen
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.designSystemFlow(
    onDrawerClicked: () -> Unit,
    onDetailSelected: (String) -> Unit,
    onDetailBack: () -> Unit,
    onUpdateAppBarState: (AppBarState) -> Unit
){
    composable(
        CarbonScreens.DesignSystems.route,
        exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(200)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(200)) },
    ) {
        DesignSystemScreen(
            onDetailSelected = {
                onDetailSelected(
                    CarbonScreens.DesignSystemsDetail.route.replace("{category}",it)
                )
            },
            onDrawerClicked = onDrawerClicked,
            onUpdateAppBarState = onUpdateAppBarState
        )
    }
    composable(
        route = CarbonScreens.DesignSystemsDetail.route,
        arguments = listOf(navArgument("category") { type = NavType.StringType }),
        enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(200)) },
        exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(200)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(200)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(200)) }
    ) { backStackEntry ->
        DesignSystemDetailScreen(
            onBack = onDetailBack,
            detailCategory = backStackEntry.arguments?.getString("category"),
            onUpdateAppBarState = onUpdateAppBarState
        )
    }
}