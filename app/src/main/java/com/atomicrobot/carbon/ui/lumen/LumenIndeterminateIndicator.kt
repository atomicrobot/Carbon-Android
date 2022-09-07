package com.atomicrobot.carbon.ui.lumen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.atomicrobot.carbon.R

@Preview
@Composable
fun LumenIndeterminateIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = LinearEasing)
            )
    )
    Image(
        painter = painterResource(id = R.drawable.ic_loading_icon),
        contentDescription = stringResource(id = R.string.cont_desc_loading),
        modifier = modifier
                .progressSemantics()
                .graphicsLayer { rotationZ = rotation }
    )
}