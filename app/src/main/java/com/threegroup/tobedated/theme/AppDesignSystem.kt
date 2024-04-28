package com.threegroup.tobedated.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class AppColorScheme(
    val primary : Color,
    val secondary : Color,
    val tertiary : Color,
    val background : Color,
    val surface : Color,
    val onBackground : Color,
    val onSurface : Color,
    val onPrimary : Color,
    val onSecondary : Color,
    val onTertiary : Color,
)

data class AppTypography(
    val titleLarge : TextStyle,
    val titleMedium : TextStyle,
    val titleSmall : TextStyle,
    val bodyLarge : TextStyle,
    val bodyMedium: TextStyle,
    val body: TextStyle,
    val bodySmall : TextStyle,
    val labelLarge : TextStyle,
    val labelMedium : TextStyle,
    val labelSmall : TextStyle
)

data class AppShape(
    val container : Shape,
    val button : Shape,
)

data class AppSize(
    val large : Dp,
    val medium : Dp,
    val normal : Dp,
    val small : Dp
)

val LocalAppColorScheme = staticCompositionLocalOf {
    AppColorScheme(
        primary = Color.Unspecified,
        secondary = Color.Unspecified,
        tertiary = Color.Unspecified,
        background = Color.Unspecified,
        surface = Color.Unspecified,
        onBackground = Color.Unspecified,
        onSurface = Color.Unspecified,
        onPrimary = Color.Unspecified,
        onSecondary = Color.Unspecified,
        onTertiary = Color.Unspecified,
    )
}

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        titleLarge  = TextStyle.Default,
        titleMedium = TextStyle.Default,
        titleSmall  = TextStyle.Default,
        bodyLarge  = TextStyle.Default,
        bodyMedium = TextStyle.Default,
        body  = TextStyle.Default,
        bodySmall  = TextStyle.Default,
        labelLarge  = TextStyle.Default,
        labelMedium = TextStyle.Default,
        labelSmall  = TextStyle.Default,
    )
}

val LocalAppShape = staticCompositionLocalOf {
    AppShape(
        container = RectangleShape,
        button = RectangleShape,
    )
}

val LocalAppSize = staticCompositionLocalOf {
    AppSize(
        large  = Dp.Unspecified,
        medium = Dp.Unspecified,
        normal = Dp.Unspecified,
        small  = Dp.Unspecified,
    )
}