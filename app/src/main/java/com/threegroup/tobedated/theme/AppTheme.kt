package com.threegroup.tobedated.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val darkColorScheme = AppColorScheme(
    primary = prim,
    secondary = sec,
    tertiary = tri,
    background = bgDark,
    surface = surDark,
    onBackground = onDark,
    onSurface = onSurDark,
    onPrimary = onLight,
    onSecondary = onLight,
    onTertiary = barDark
)
val darkColorSchemeFriends = AppColorScheme(
    primary = primF,
    secondary = secF,
    tertiary = triF,
    background = bgDarkF,
    surface = surDarkF,
    onBackground = onDarkF,
    onSurface = onSurDarkF,
    onPrimary = onLightF,
    onSecondary = onLightF,
    onTertiary = barDarkF,
)
val darkColorSchemeCasual = AppColorScheme(
    primary = primC,
    secondary = secC,
    tertiary = triC,
    background = bgDarkC,
    surface = surDarkC,
    onBackground = onDarkC,
    onSurface = onSurDarkC,
    onPrimary = onLightC,
    onSecondary = onLightC,
    onTertiary = barDarkC,
)
val lightColorScheme = AppColorScheme(
    primary = prim,
    secondary = sec,
    tertiary = tri,
    background = bgLight,
    surface = surLight,
    onBackground = onLight,
    onSurface = onSurLight,
    onPrimary = onLight,
    onSecondary = onLight,
    onTertiary = barLight
)
val lightColorSchemeFriends = AppColorScheme(
    primary = primF,
    secondary = secF,
    tertiary = triF,
    background = bgLightF,
    surface = surLightF,
    onBackground = onLightF,
    onSurface = onSurLightF,
    onPrimary = onLightF,
    onSecondary = onLightF,
    onTertiary = barLightF,
)
val lightColorSchemeCasual = AppColorScheme(
    primary = primC,
    secondary = secC,
    tertiary = triC,
    background = bgLightC,
    surface = surLightC,
    onBackground = onLightC,
    onSurface = onSurLightC,
    onPrimary = onLightC,
    onSecondary = onLightC,
    onTertiary = barLightC,
)


private val typography = AppTypography(
    titleLarge = TextStyle(
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,

    ),
    titleSmall = TextStyle(
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    body = TextStyle(
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = JoseFinSans,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = JoseFinSans,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)
private val shape = AppShape(
    container = RoundedCornerShape(12.dp),
    button = RoundedCornerShape(50)
)
private val size = AppSize(
    large = 24.dp,
    medium = 16.dp,
    normal = 12.dp,
    small = 8.dp
)

@Composable
fun AppTheme(
    activity:String,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
){
    //var colorScheme : AppColorScheme
    val colorScheme = when (activity) {
        "dating" -> if (isDarkTheme) darkColorScheme else lightColorScheme
        "friend" -> if (isDarkTheme) darkColorSchemeFriends else lightColorSchemeFriends
        "casual" -> if (isDarkTheme) darkColorSchemeCasual else lightColorSchemeCasual
        else -> if (isDarkTheme) darkColorScheme else lightColorScheme

    }
    val rippleIndication = rememberRipple()
    CompositionLocalProvider (
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShape provides shape,
        LocalAppSize provides size,
        LocalIndication provides rippleIndication,
        content = content
    )
}

object AppTheme {
    val colorScheme: AppColorScheme
        @Composable get() = LocalAppColorScheme.current

    val typography: AppTypography
        @Composable get() = LocalAppTypography.current

    val shape: AppShape
        @Composable get() = LocalAppShape.current

    val size: AppSize
        @Composable get() = LocalAppSize.current
}

