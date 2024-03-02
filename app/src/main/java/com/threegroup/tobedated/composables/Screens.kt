package com.threegroup.tobedated.composables

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.threegroup.tobedated.ui.theme.AppTheme
import kotlin.math.ceil

@Composable
fun PolkaDotCanvas() {
    val color12 = AppTheme.colorScheme.secondary.copy(alpha = 0.45f)
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val cir1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    Box(modifier = Modifier
        .fillMaxSize()
        .background(AppTheme.colorScheme.background)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 2.dp.toPx()
            val gap = 40.dp.toPx()
            val canvasWidth = size.width
            val canvasHeight = size.height

            val rows = ceil((canvasHeight + gap) / gap).toInt()
            val cols = ceil((canvasWidth + gap) / gap).toInt()
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    val offset = (if (row % 2 == 0) gap / 2 else 0).toInt()
                    val x = col * gap + gap / 2 + offset - gap / 2
                    val y = row * gap + gap / 2 - gap / 2
                    val location = Offset(x, y)
                    drawCircle(
                        color = color12,
                        style = Stroke(
                            width = strokeWidth,
                        ),
                        center = location,
                        radius = cir1 * 0.1f
                    )
                }
            }
        }
    }
}