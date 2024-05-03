package com.threegroup.tobedated._friends

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated._causal.CausalActivity
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._friends.composables.TopAndBotBarsFriends
import com.threegroup.tobedated.composeables.composables.Comeback
import com.threegroup.tobedated.composeables.composables.PolkaDotCanvas
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.theme.AppTheme
import kotlin.random.Random

val notifiGroup = Random.nextBoolean()
val notifiChat = Random.nextInt(0, 41) // Generates a random integer between 0 and 40

class FriendsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("activityToken", "friend")
        editor.apply()
        setContent {
            AppTheme(
                activity = "friend"
            ) {
                val viewModelFriend = viewModel { FriendViewModel(MyApp.x) }
                viewModelFriend.setLoggedInUser()

                if(viewModelFriend.getUser().hasCasual){
                    FriendNav(this@FriendsActivity, viewModelFriend)
                }else{
                    FriendNav(this@FriendsActivity, viewModelFriend)
                }
            }
        }
    }
    fun switchActivities(switchActivity:String){
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
fun SearchingScreen(navController: NavHostController, friend:FriendsActivity, vmApi: ApiViewModel){
    val state = rememberScrollState()
    TopAndBotBarsFriends(
        friend = friend,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 2,
        settingsButton = { },
        state = state,
        vmApi = vmApi,
        currentScreen = {
            Comeback(text = "Simple text", todo = "wait")
        }
    )
}
@Composable
fun ProfileScreen(navController: NavHostController, friend:FriendsActivity, vmApi: ApiViewModel){
    val state = rememberScrollState()
    TopAndBotBarsFriends(
        friend = friend,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { },
        state = state,
        vmApi = vmApi,
        currentScreen = {
            PolkaDotCanvas()
        }
    )
}
@Composable
fun EditProfileScreen(nav:NavHostController, friendsActivity: FriendsActivity, vmFriends:FriendViewModel){
    PolkaDotCanvas()
}
@Composable
fun SearchPreferenceScreen(nav:NavHostController, vmFriends:FriendViewModel){
    PolkaDotCanvas()
}
@Composable
fun ChatsScreen(navController: NavHostController, friend:FriendsActivity, vmApi: ApiViewModel){
    val state = rememberScrollState()
    TopAndBotBarsFriends(
        friend = friend,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { },
        state = state,
        vmApi = vmApi,
        currentScreen = {
            PolkaDotCanvas()
        }
    )
}
@Composable
fun GroupsScreen(navController: NavHostController, friend:FriendsActivity, vmApi: ApiViewModel){
    val state = rememberScrollState()
    TopAndBotBarsFriends(
        friend = friend,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { },
        state = state,
        vmApi = vmApi,
        currentScreen = {
            PolkaDotCanvas()
        }
    )
}
@Composable
fun SomeScreen(navController: NavHostController, friend:FriendsActivity, vmApi: ApiViewModel){
    val state = rememberScrollState()
    TopAndBotBarsFriends(
        friend = friend,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { },
        state = state,
        vmApi = vmApi,
        currentScreen = {
            PolkaDotCanvas()
        }
    )
}
@Composable
fun MessagerScreen(nav:NavHostController, vmFriends:FriendViewModel){
    PolkaDotCanvas()
}
@Composable
fun ChangePreference(navController: NavHostController, title:String, index:Int, vmFriends:FriendViewModel){
    PolkaDotCanvas()
}
@Composable
fun ChangeProfileScreen(navController: NavHostController, title:String, index:Int, vmFriends:FriendViewModel){

}
@Composable
fun ComeBackScreen(navController: NavHostController, friend: FriendsActivity, vmApi: ApiViewModel){
    TopAndBotBarsFriends(
        friend = friend,
        notifiChat = com.threegroup.tobedated._dating.notifiChat,
        notifiGroup = com.threegroup.tobedated._dating.notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { navController.navigate("SearchPreferenceScreen") },
        vmApi = vmApi,
        currentScreen = {
            Comeback(text = "currently loading your future connection", todo = "wait")
        }
    )
}

enum class Friend {
    SearchingScreen,
    SearchPreferenceScreen,
    ProfileScreen,
    EditProfileScreen,
    ChatsScreen,
    GroupsScreen,
    SomeScreen,
    MessagerScreen,
}