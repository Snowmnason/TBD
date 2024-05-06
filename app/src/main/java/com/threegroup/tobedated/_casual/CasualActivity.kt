package com.threegroup.tobedated._casual

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated._casual.composables.UserInfoC
import com.threegroup.tobedated.composeables.composables.Comeback
import com.threegroup.tobedated.composeables.composables.PolkaDotCanvas
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.theme.AppTheme

class CasualActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("activityToken", "casual")
        editor.apply()
        setContent {
            AppTheme(
                activity = "casual"//casual
            ) {
                val viewModelCasual = viewModel { CasualViewModel(MyApp.x) }
                viewModelCasual.setLoggedInUser()

                if(viewModelCasual.getUser().hasCasual){
                    CasualNav(this@CasualActivity, viewModelCasual)
                }else{
                    PolkaDotCanvas()
                    SignUpCasualNav(this@CasualActivity, viewModelCasual)
                }
                //CasualNav(this@CasualActivity, token, location)
            }
        }
    }
    fun newContent(viewModelCasual:CasualViewModel){
        setContent {
            AppTheme(
                activity = "casual"//casual
            ) {
                CasualNav(this@CasualActivity, viewModelCasual)
            }
        }
    }
    //This is the fuck tab
    //adding it so I can say "fuck tab"
//    fun switchActivities(switchActivity:String){
//        val intent = when (switchActivity) {
//            "dating" -> {
//                Intent(this, DatingActivity::class.java)
//            }
//
//            "casual" -> {
//                Intent(this, CasualActivity::class.java)
//            }
//
//            "friends" -> {
//                Intent(this, FriendsActivity::class.java)
//            }
//            else -> {
//                Intent(this, DatingActivity::class.java)
//            }
//        }
//        startActivity(intent)
//        finish()
//    }
}
@Composable
fun SearchingScreen(navController: NavHostController, casual: CasualActivity, vmCasual: CasualViewModel, vmApi: ApiViewModel){
    val state = rememberScrollState()

}
@Composable
fun ProfileScreen(navController: NavHostController, casual: CasualActivity, vmCasual: CasualViewModel, vmApi: ApiViewModel){
    val currentUser = vmCasual.getUser()
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (currentUser.name.isNotEmpty()) {
            isLoading.value = false
        }
    }
    UserInfoC(
        currentUser,
        bioClick = { navController.navigate("BioEdit") },
        prompt1Click = {navController.navigate("PromptEdit/1")},
        prompt2Click = {navController.navigate("PromptEdit/2")},
        prompt3Click = {navController.navigate("PromptEdit/3")},
        photoClick = {navController.navigate("ChangePhoto")},
        doesEdit = true
    )

}
@Composable
fun EditProfileScreen(nav: NavHostController, casualActivity: CasualActivity, vmCasual: CasualViewModel){
    PolkaDotCanvas()
}
@Composable
fun SearchPreferenceScreen(nav: NavHostController, vmCasual: CasualViewModel){
    PolkaDotCanvas()
}
@Composable
fun ChatsScreen(navController: NavHostController, casual: CasualActivity, vmCasual: CasualViewModel, vmApi: ApiViewModel){

}
@Composable
fun GroupsScreen(navController: NavHostController, casual: CasualActivity, vmApi: ApiViewModel){

}
@Composable
fun SomeScreen(navController: NavHostController, casual: CasualActivity, vmApi: ApiViewModel){

}
@Composable
fun MessagerScreen(nav: NavHostController, vmCasual: CasualViewModel){
    PolkaDotCanvas()
}
@Composable
fun ChangePreference(navController: NavHostController, title:String, index:Int, vmCasual: CasualViewModel){
    PolkaDotCanvas()
}
@Composable
fun ChangeProfileScreen(navController: NavHostController, title:String, index:Int, vmCasual: CasualViewModel){
    PolkaDotCanvas()
}
@Composable
fun ComeBackScreen(navController: NavHostController, casual: CasualActivity, vmApi: ApiViewModel){
    Comeback(text = "currently loading your future connection", todo = "wait", vmApi = vmApi)
}

enum class Casual {
    SearchingScreen,
    SearchPreferenceScreen,
    ProfileScreen,
    EditProfileScreen,
    ChatsScreen,
    GroupsScreen,
    SomeScreen,
    MessagerScreen,
}