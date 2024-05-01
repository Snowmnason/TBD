package com.threegroup.tobedated._signUp.composables.questions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.threegroup.tobedated.R

@Composable
fun PhotoQuestion(
    photo1: Painter = painterResource(id = R.drawable.photoholder),
    photo2: Painter = painterResource(id = R.drawable.photoholder),
    photo3: Painter = painterResource(id = R.drawable.photoholder),
    photo4: Painter = painterResource(id = R.drawable.photoholder),
    onClick1: () -> Unit,
    onClick2: () -> Unit,
    onClick3: () -> Unit,
    onClick4: () -> Unit,
    isEnabled2:Boolean = true,
    isEnabled3:Boolean = true,
    isEnabled4:Boolean = true,
) {
    Box(
        modifier = Modifier
            .size(525.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PFPPhotoButton(photo1, onClick1)
                Spacer(modifier = Modifier.height(2.dp))
                PFPPhotoButton(photo2, onClick2, isEnabled2)
            }
            Spacer(modifier = Modifier.width(2.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PFPPhotoButton(photo3, onClick3, isEnabled3)
                Spacer(modifier = Modifier.height(2.dp))
                PFPPhotoButton(photo4, onClick4, isEnabled4)
            }
        }
    }
}

@Composable
fun PFPPhotoButton(
    photo: Painter,
    onClick: () -> Unit,
    isEnabled:Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(9f / 16f)
            .padding(0.dp)
            .fillMaxSize(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,

            ),
        contentPadding = PaddingValues(2.dp),
        enabled = isEnabled,
        content = {
            Image(
                painter = photo,
                contentDescription = "Your profile photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    )
}