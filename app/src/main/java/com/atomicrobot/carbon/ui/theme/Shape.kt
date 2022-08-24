package com.atomicrobot.carbon.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

val lumenShapes = Shapes(
    medium = RoundedCornerShape(8.dp),
    large= RoundedCornerShape(24.dp)
)

val Shapes.RoundedTextField: Shape
    get() = RoundedCornerShape(8.dp)