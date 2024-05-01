package com.threegroup.tobedated._signUp.composes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.threegroup.tobedated.R
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated._signUp.composables.questions.PhotoQuestion
import com.threegroup.tobedated._signUp.composables.SignUpFormat
import com.threegroup.tobedated.composeables.composables.DialogWithImage

@Composable
fun photoScreen(signUpVM: SignUpViewModel):Boolean{
    var photo1 by rememberSaveable { mutableStateOf(signUpVM.getUser().image1) }
    var photo2 by rememberSaveable { mutableStateOf(signUpVM.getUser().image2) }
    var photo3 by rememberSaveable { mutableStateOf(signUpVM.getUser().image3) }
    var photo4 by rememberSaveable { mutableStateOf(signUpVM.getUser().image4) }
    var showFinalDialog by rememberSaveable { mutableStateOf(true) }
    if (showFinalDialog) {
        DialogWithImage(
            onDismissRequest = { showFinalDialog = false },
            imageDescription = "first photo",
            body = "To make everyone look and feel more equal\nWe have a strict first photo policy!\nPlease Select a photo that matches the one above\nThe second third and forth express yourself\nBut we also shy against group photos",
            painter = painterResource(id = R.drawable.firstphoto),
        )
    }
    val nullPhoto = painterResource(id = R.drawable.photoholder)
    var imageUri1 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri2 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri3 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri4 by rememberSaveable { mutableStateOf<Uri?>(null) }

    val enables by rememberSaveable { mutableStateOf(BooleanArray(3) { false }) }
    val galleryLauncher1 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    imageUri1 = it
                    photo1 = it.toString()
                    signUpVM.setUser("image1", photo1)
                }
            })
    val galleryLauncher2 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    imageUri2 = it
                    photo2 = it.toString()
                    signUpVM.setUser("image2", photo2)
                }
            })
    val galleryLauncher3 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    imageUri3 = it
                    photo3 = it.toString()
                    signUpVM.setUser("image3", photo3)
                }
            })
    val galleryLauncher4 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    imageUri4 = it
                    photo4 = it.toString()
                    signUpVM.setUser("image4", photo4)
                }
            })

    SignUpFormat(
        title = "Add some photos!",
        label = "Show yourself off!",
        enterField = {
            PhotoQuestion(
                photo1 = if (imageUri1 != null) {
                    rememberAsyncImagePainter(model = imageUri1)
                } else {
                    nullPhoto
                },
                photo2 = if (imageUri2 != null) {
                    rememberAsyncImagePainter(model = imageUri2)
                } else {
                    nullPhoto
                },
                photo3 = if (imageUri3 != null) {
                    rememberAsyncImagePainter(model = imageUri3)
                } else {
                    nullPhoto
                },
                photo4 = if (imageUri4 != null) {
                    rememberAsyncImagePainter(model = imageUri4)
                } else {
                    nullPhoto
                },
                onClick1 = {
                    galleryLauncher1.launch("image/*")
                    enables[0] = true
                },
                onClick2 = {
                    galleryLauncher2.launch("image/*")
                    enables[1] = true
                },
                onClick3 = {
                    galleryLauncher3.launch("image/*")
                    enables[2] = true
                },
                onClick4 = {
                    galleryLauncher4.launch("image/*")
                },
                isEnabled2 = enables[0],
                isEnabled3 = enables[1],
                isEnabled4 = enables[2],
            )
        },
    )
    return (photo1.isNotEmpty() && photo2.isNotEmpty() && photo3.isNotEmpty())
}