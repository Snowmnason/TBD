package com.threegroup.tobedated._signUp.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.threegroup.tobedated.R
import com.threegroup.tobedated.shareclasses.composables.GenericLabelText
import com.threegroup.tobedated.shareclasses.composables.GenericTitleText
import com.threegroup.tobedated.shareclasses.theme.AppTheme
import com.threegroup.tobedated.shareclasses.theme.JoseFinSans
import com.threegroup.tobedated.shareclasses.theme.shadowWithOpacity

@Composable
fun getCustomTextStyle(): TextStyle {
    return TextStyle(
        color = AppTheme.colorScheme.onBackground,
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        shadow = Shadow(
            color = shadowWithOpacity,
            offset = Offset(4f, 4f),
            blurRadius = 4f
        )
    )
}
@Composable
fun getCustomButtonStyle(): TextStyle {
    return TextStyle(
        color = AppTheme.colorScheme.onBackground,
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        shadow = Shadow(
            color = shadowWithOpacity,
            offset = Offset(4f, 4f),
            blurRadius = 4f
        )
    )
}
@Composable
fun BackButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(5.dp,15.dp,0.dp,0.dp)
    ) {
        val photo = if (isSystemInDarkTheme()) painterResource(id = R.drawable.backdark) else painterResource(id = R.drawable.back)
        Image(
            painter = photo,
            contentDescription = "back",
            modifier = Modifier.size(125.dp), // Adjust the size here as needed
        )
    }
}

@Composable
fun getCustomTextStyleLabel(
    opacity:Float = .5F,
    fontSize: TextUnit = 26.sp,

    ): TextStyle {
    val colorWithOpacity = AppTheme.colorScheme.onBackground.copy(alpha = opacity)
    return TextStyle(
        color = colorWithOpacity,
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = fontSize,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        shadow = Shadow(
            color = shadowWithOpacity,
            offset = Offset(4f, 4f),
            blurRadius = 4f
        )
    )
}

@Composable
fun BigButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit, // onClick function with no return value
    isUse: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, 50.dp)
    ) {
        val gradient = if (isUse) {
            Brush.linearGradient(
                0.6f to AppTheme.colorScheme.secondary,
                1.0f to AppTheme.colorScheme.primary,
            )
        } else {
            Brush.horizontalGradient(
                0.0f to Color(0xCC7d758d),
                0.5f to Color(0x997d758d),
                1.0f to Color(0xCC7d758d),
            )
        }
        Button(
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            contentPadding = PaddingValues(),
            onClick = { onClick() }, // Call the provided onClick function
            enabled = isUse,
        ) {
            Box(
                modifier = Modifier
                    .background(gradient)
                    .then(modifier)
                    .height(55.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = text,
                    style = AppTheme.typography.titleMedium,
                    fontSize = 25.sp
                )
            }
        }
    }
}

@Composable
fun SignUpFormat(
    title: String,
    label: String,
    enterField: @Composable () -> Unit = {},
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 65.dp, 0.dp, 145.dp)
    ) {
        Surface(
            modifier = Modifier
                .padding(25.dp, 0.dp, 25.dp, 0.dp),
            color = AppTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp)
        ) {
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(0) }
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .verticalScroll(state)
                    .fillMaxSize()
            ) {
                // Title
                GenericTitleText(text = title, style = AppTheme.typography.titleMedium,)
                Spacer(modifier = Modifier.height(5.dp))
                enterField()
                //Fun Label
                Spacer(modifier = Modifier.height(12.dp))
                GenericLabelText(
                    text = label,
                    style = AppTheme.typography.labelMedium
                )
            }
        }
    }
}


