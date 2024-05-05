package com.threegroup.tobedated.composeables.profiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.threegroup.tobedated.MainActivity
import com.threegroup.tobedated._signUp.composables.BigButton
import com.threegroup.tobedated.composeables.composables.GenericBodyText
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.composables.SimpleBox


@Composable
fun LogOut(dating: MainActivity){
    BigButton(
        text = "Log Out",
        onClick = {
            dating.clearUserToken()
    }, isUse = true)
}
@Composable
fun EditProfile(
    title: String,
    navController: NavController,
    userSetting: String,
    clickable: Boolean = false,
    index:Int
) {

    val modifier = if (clickable) {
        Modifier.clickable { navController.navigate("ChangeProfileScreen/$title/$index") }
    } else {
        Modifier
    }

    SimpleBox(
        whatsInsideTheBox = {
            Row(
                modifier = Modifier
                    .padding(15.dp, 15.dp, 15.dp, 0.dp)
                    .fillMaxWidth()
                    .then(modifier),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GenericTitleText(text = "$title:")
                //Spacer(modifier = Modifier.height(4.dp))
                GenericBodyText(text = userSetting, modifier.offset(y=0.dp))
            }
        }
    )
}


