package com.threegroup.tobedated._dating

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.threegroup.tobedated._causal.CausalActivity
import com.threegroup.tobedated._friends.FriendsActivity
import com.threegroup.tobedated._login.LoginActivity
import com.threegroup.tobedated.composeables.composables.OutLinedButton
import com.threegroup.tobedated.composeables.profiles.InsideMatchedProfile
import com.threegroup.tobedated.composeables.searching.SeekingUserInfo
import com.threegroup.tobedated.shareclasses.calcDistance
import com.threegroup.tobedated.shareclasses.storeImageAttempt
import com.threegroup.tobedated.theme.AppTheme
import kotlinx.coroutines.launch

//val notifiSearching = Random.nextBoolean()

const val notifiGroup = false
const val notifiChat = 0//Random.nextInt(0, 41) // Generates a random integer between 0 and 40

class DatingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreference = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("activityToken", "dating")
        editor.apply()
        setContent {
            AppTheme(
                activity = "dating"
            ) {
                DatingNav(this@DatingActivity)
            }
        }
    }

    fun uploadPhotos(
        newImage: String,
        imageNumber: Int,
        imageName: String,
        callback: (String) -> Unit
    ) {
        lifecycleScope.launch {
            val result = storeImageAttempt(newImage, contentResolver, imageNumber, imageName)
            callback(result)
        }
    }

    fun clearUserToken() {
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("user_login")
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun switchActivities(switchActivity: String) {
        val intent = when (switchActivity) {
            "dating" -> {
                Intent(this, DatingActivity::class.java)
            }

            "causal" -> {
                Intent(this, CausalActivity::class.java)
            }

            "friends" -> {
                Intent(this, FriendsActivity::class.java)
            }

            else -> {
                Intent(this, DatingActivity::class.java)
            }
        }
        startActivity(intent)
        finish()
    }
}
@Composable
fun MatchedUserProfile(nav: NavHostController, vmDating: DatingViewModel) {
    val talkedUser = vmDating.getTalkedUser()
    InsideMatchedProfile(
        nav = nav,
        title = talkedUser.name,
        editProfile = {
            val location = calcDistance(talkedUser.location, vmDating.getUser().location)
            SeekingUserInfo(
                user = talkedUser,//usersArray[currentProfileIndex]
                location = location,
                bottomButtons = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(25.dp, 0.dp)
                    ) {
                        OutLinedButton(
                            onClick = {
                                vmDating.reportUser(talkedUser.number, vmDating.getUser().number)
                                nav.navigate("ChatsScreen")
                            },
                            text = "Report",
                            outLineColor = Color.Red,
                            textColor = Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutLinedButton(
                            onClick = { vmDating.deleteMatch(talkedUser.number, vmDating.getUser().number)
                                nav.navigate("ChatsScreen")
                            },
                            text = "Unmatch",
                            outLineColor = Color.Red
                        )
                    }
                },
            )
        }
    )
}

/*
End of Message Screens
Start of Groups Screens
 */
@Composable
fun GroupsScreen(navController: NavHostController, dating: DatingActivity) {
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        isPhoto = false,
        titleText = "Groups",
        nav = navController,
        selectedItemIndex = 3,
        settingsButton = { },
        star = "Ask me",
        currentScreen = {
        }
    )
}

enum class Dating {
    SearchingScreen,
    SearchPreferenceScreen,
    ProfileScreen,
    EditProfileScreen,
    ChatsScreen,
    GroupsScreen,
    SomeScreen,
    MessagerScreen,
    FeedBackMessagerScreen
}