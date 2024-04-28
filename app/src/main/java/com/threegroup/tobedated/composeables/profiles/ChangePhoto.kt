package com.threegroup.tobedated.composeables.profiles

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.threegroup.tobedated.R
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated._signUp.composables.PhotoQuestion
import com.threegroup.tobedated.composeables.composables.SimpleBox
import com.threegroup.tobedated.composeables.searching.ChangePreferenceTopBar
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun ChangePhoto(
    nav: NavHostController,
    vmDating: DatingViewModel,
    dating: DatingActivity
) {
    val currentUser = vmDating.getUser()
    var photo1 by rememberSaveable { mutableStateOf(currentUser.image1) }
    var photo2 by rememberSaveable { mutableStateOf(currentUser.image2) }
    var photo3 by rememberSaveable { mutableStateOf(currentUser.image3) }
    var photo4 by rememberSaveable { mutableStateOf(currentUser.image4) }
    val nullPhoto = painterResource(id = R.drawable.photoholder)
    var imageUri1 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri2 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri3 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri4 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var upload1 by rememberSaveable { mutableStateOf(false) }
    var upload2 by rememberSaveable { mutableStateOf(false) }
    var upload3 by rememberSaveable { mutableStateOf(false) }
    var upload4 by rememberSaveable { mutableStateOf(false) }

    val galleryLauncher1 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    imageUri1 = it
                    photo1 = it.toString()
                }
            })//  newUser.image1 = it.toString()
    val galleryLauncher2 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    imageUri2 = it
                    photo2 = it.toString()
                }
            })//newUser.image2 = it.toString()
    val galleryLauncher3 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    imageUri3 = it
                    photo3 = it.toString()
                }
            })//newUser.image3 = it.toString()
    val galleryLauncher4 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    imageUri4 = it
                    photo4 = it.toString()
                }
            })//newUser.image4 = it.toString()
    ChangePreferenceTopBar(
        nav = nav,
        title = "Photos",
        changeSettings = {
            Spacer(modifier = Modifier.height(24.dp))
            SimpleBox(
                whatsInsideTheBox = {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        PhotoQuestion(
                            photo1 = if (imageUri1 != null) {
                                rememberAsyncImagePainter(model = imageUri1)
                            } else {
                                rememberAsyncImagePainter(model = photo1)
                            },
                            photo2 = if (imageUri2 != null) {
                                rememberAsyncImagePainter(model = imageUri2)
                            } else {
                                rememberAsyncImagePainter(model = photo2)
                            },
                            photo3 = if (imageUri3 != null) {
                                rememberAsyncImagePainter(model = imageUri3)
                            } else {
                                rememberAsyncImagePainter(model = photo3)
                            },
                            photo4 = if (imageUri4 != null) {
                                rememberAsyncImagePainter(model = imageUri4)
                            } else if (photo4 != "") {
                                rememberAsyncImagePainter(model = photo4)
                            } else {
                                nullPhoto
                            },
                            onClick1 = {
                                galleryLauncher1.launch("image/*")
                                upload1 = true
                            },
                            onClick2 = {
                                galleryLauncher2.launch("image/*")
                                upload2 = true
                            },
                            onClick3 = {
                                galleryLauncher3.launch("image/*")
                                upload3 = true
                            },
                            onClick4 = {
                                galleryLauncher4.launch("image/*")
                                upload4 = true
                            },
                        )
                    }
                }
            )
        },
        save = {
            Button(
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                modifier = Modifier.offset(y = 5.dp),
                onClick = {
                    if (upload1) {
                        dating.uploadPhotos(photo1, 1, currentUser.number) { newResult ->
                            currentUser.image1 = newResult
                        }
                    }
                    if (upload2) {
                        dating.uploadPhotos(photo2, 2, currentUser.number) { newResult ->
                            currentUser.image2 = newResult
                        }
                    }
                    if (upload3) {
                        dating.uploadPhotos(photo3, 3, currentUser.number) { newResult ->
                            currentUser.image3 = newResult
                        }
                    }
                    if (upload4) {
                        dating.uploadPhotos(photo4, 4, currentUser.number) { newResult ->
                            currentUser.image4 = newResult
                        }
                    }
                    nav.popBackStack()
                    vmDating.updateUser(currentUser)
                }
            ) {
                Text(
                    text = "Confirm",
                    style = AppTheme.typography.titleSmall,
                    color = Color(0xFF93C47D)
                )
            }
        }
    )
}