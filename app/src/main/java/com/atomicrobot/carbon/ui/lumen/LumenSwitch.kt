package com.atomicrobot.carbon.ui.lumen

import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val AnimationSpec = TweenSpec<Float>(durationMillis = 100)

@Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LumenSwitch(
    checked: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit) = { },
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    properties: LumenSwitchProperties = LumenSwitchProperties(),
    colors: SwitchColors = SwitchDefaults.colors()
) {
    val density = LocalDensity.current
    val minBound = 0f
    val maxBound = remember {
        with(density) {
            (
                (properties.trackHeight - properties.thumbDiameter) -
                    properties.thumbPadding * 2
                ).toPx()
        }
    }
    val swipeableState = rememberSwipeableStateFor(checked, onCheckedChange, AnimationSpec)
    val toggleableModifier = Modifier.toggleable(
        value = checked,
        onValueChange = onCheckedChange,
        enabled = enabled,
        role = Role.Switch,
        interactionSource = interactionSource,
        indication = null
    )

    Box(
        modifier
            .then(Modifier.minimumTouchTargetSize())
            .then(toggleableModifier)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(minBound to false, maxBound to true),
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Vertical,
                enabled = enabled,
                reverseDirection = true,
                interactionSource = interactionSource,
                resistance = null
            )
            .requiredSize(properties.trackWidth, properties.trackHeight)
    ) {
        LumenSwitchImp(
            checked = checked,
            enabled = enabled,
            colors = colors,
            thumbValue = swipeableState.offset,
            interactionSource = interactionSource
        )
    }
}

@Composable
fun BoxScope.LumenSwitchImp(
    checked: Boolean,
    enabled: Boolean,
    colors: SwitchColors,
    thumbValue: State<Float>,
    interactionSource: InteractionSource,
    properties: LumenSwitchProperties = LumenSwitchProperties(),
) {
    val trackColor by colors.trackColor(enabled, checked)
    Canvas(
        Modifier
            .align(Alignment.Center)
            .fillMaxSize()
    ) {
        drawTrack(
            trackColor,
            properties.trackWidth.toPx(),
            properties.trackHeight.toPx(),
            properties.trackCornerRadius,
        )
    }

    val thumbColor by colors.thumbColor(enabled, checked)
    Spacer(
        Modifier
            .align(Alignment.BottomCenter)
            .padding(DefaultSwitchPadding)
            .offset { IntOffset(0, -thumbValue.value.roundToInt()) }
            .indication(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = ThumbRippleRadius)
            )
            .requiredSize(DefaultThumbDiameter)
            .shadow(properties.thumbElevation, CircleShape, clip = false)
            .background(thumbColor, properties.thumbShape)
    )
}

private fun DrawScope.drawTrack(
    trackColor: Color,
    trackWidth: Float,
    trackHeight: Float,
    cornerRadius: CornerRadius
) {
    drawRoundRect(
        trackColor,
        Offset(0F, 0F),
        Size(trackWidth, trackHeight),
        cornerRadius
    )
}

private val ThumbRippleRadius = 24.dp
private val DefaultThumbDiameter = 28.dp
private val DefaultTrackWidth = 32.dp
private val DefaultTrackHeight = 56.dp
private val DefaultSwitchPadding = 2.dp
private val DefaultTrackRadius = CornerRadius(50F, 50F)
private val DefaultThumbShape = CircleShape
private val DefaultThumbElevation = 1.dp

class LumenSwitchProperties
constructor(
    val trackCornerRadius: CornerRadius = DefaultTrackRadius,
    val thumbShape: Shape = DefaultThumbShape,
    val thumbDiameter: Dp = DefaultThumbDiameter,
    val trackWidth: Dp = DefaultTrackWidth,
    val trackHeight: Dp = DefaultTrackHeight,
    val thumbPadding: Dp = DefaultSwitchPadding,
    val thumbElevation: Dp = DefaultThumbElevation
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LumenSwitchProperties

        if (trackCornerRadius != other.trackCornerRadius) return false
        if (thumbShape != other.thumbShape) return false
        if (thumbDiameter != other.thumbDiameter) return false
        if (trackWidth != other.trackWidth) return false
        if (trackHeight != other.trackHeight) return false
        if (thumbPadding != other.thumbPadding) return false

        return true
    }

    override fun hashCode(): Int {
        var result = trackCornerRadius.hashCode()
        result = 31 * result + thumbShape.hashCode()
        result = 31 * result + thumbDiameter.hashCode()
        result = 31 * result + trackWidth.hashCode()
        result = 31 * result + trackHeight.hashCode()
        result = 31 * result + thumbPadding.hashCode()
        return result
    }
}
