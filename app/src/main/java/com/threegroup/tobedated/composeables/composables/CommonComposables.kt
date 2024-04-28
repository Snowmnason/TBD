package com.threegroup.tobedated.composeables.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.threegroup.tobedated.shareclasses.models.getSmallerTextStyle
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun SimpleBox(
    verify: Boolean = false,
    whatsInsideTheBox: @Composable () -> Unit = {},
    edit:Boolean = false,
    onClick: () -> Unit = {}
){
    val thickness = 1
    val boardColor= Color(0xFFB39DB7)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(thickness.dp, boardColor, shape = RoundedCornerShape(4.dp)),
        color = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(modifier = Modifier
            .padding(4.dp, 8.dp)
            .clickable(enabled = edit, onClick = onClick)) {
            whatsInsideTheBox()
        }
    }
}

@Composable
fun SimpleIconBox(
    //whatsInsideTheBox: @Composable () -> Unit = {},
    verify:String = "false",
    answer: String,
    icon: ImageVector?,
    divider:Boolean = false,
    color: Color = AppTheme.colorScheme.onBackground,
){

    Box(modifier = Modifier) {
        if (icon != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    imageVector = icon, contentDescription = "icon", modifier = Modifier
                        .offset(y = (-2).dp)
                        .size(25.dp), tint = AppTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = answer,
                    style = getSmallerTextStyle(color),
                    modifier = Modifier.offset(y = 3.dp),
                )
            }
        } else {
            Text(
                text = answer,
                style = getSmallerTextStyle(color),
                modifier = Modifier.offset(y = 3.dp)
            )
        }
    }
    if(divider){
        VerticalDivider(
            modifier = Modifier.height(20.dp),
            color = Color(0xFFB39DB7),
            thickness = 2.dp
        )
    }
}