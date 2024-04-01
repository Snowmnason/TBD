package com.threegroup.tobedated.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.threegroup.tobedated.callclass.calcDistance
import com.threegroup.tobedated.composables.AlertDialogBox
import com.threegroup.tobedated.composables.DatingNav
import com.threegroup.tobedated.composables.GenericTitleSmall
import com.threegroup.tobedated.composables.datingScreens.AgeSlider
import com.threegroup.tobedated.composables.datingScreens.ChangePreferenceScreen
import com.threegroup.tobedated.composables.datingScreens.ChangeSeekingScreen
import com.threegroup.tobedated.composables.datingScreens.Comeback
import com.threegroup.tobedated.composables.datingScreens.CurrentUserInfo
import com.threegroup.tobedated.composables.datingScreens.DistanceSlider
import com.threegroup.tobedated.composables.datingScreens.EditProfile
import com.threegroup.tobedated.composables.datingScreens.InsideMessages
import com.threegroup.tobedated.composables.datingScreens.InsideProfileSettings
import com.threegroup.tobedated.composables.datingScreens.InsideSearchSettings
import com.threegroup.tobedated.composables.datingScreens.MessageStart
import com.threegroup.tobedated.composables.datingScreens.OtherPreferences
import com.threegroup.tobedated.composables.datingScreens.SearchingButtons
import com.threegroup.tobedated.composables.datingScreens.SeekingBox
import com.threegroup.tobedated.composables.datingScreens.TheirMessage
import com.threegroup.tobedated.composables.datingScreens.TopAndBotBars
import com.threegroup.tobedated.composables.datingScreens.UserInfo
import com.threegroup.tobedated.composables.datingScreens.UserMessage
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.viewModels.DatingViewModel
import kotlin.random.Random


val notifiGroup = Random.nextBoolean()
val notifiChat = Random.nextInt(0, 41) // Generates a random integer between 0 and 40
//val notifiSearching = Random.nextBoolean()

class DatingActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getLastLocation()
            } else {
                // Handle permission denied
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = intent.getStringExtra("token").toString()
        var location = "error"
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request location permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            location = getLastLocation()
        }
        setContent {
            AppTheme {
                DatingNav(this@DatingActivity, token, location)
            }
        }
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun getLastLocation():String {
        var latitude = ""
        var longitude = ""
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return "error"
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Handle the retrieved location
                if (location != null) {
                    // Got last known location
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
                } else {
                    // Last location is null
                    // Handle this situation
                    latitude = "error"
                    longitude = ""
                }
            }
            .addOnFailureListener { e ->
                // Handle failure to retrieve location

            }
        return "$latitude/$longitude"
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    fun clearUserToken() {
        val sharedPreferences = getSharedPreferences("firebase_user", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("firebase_user_token")
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
/*
Start of Seeking Screen
 */
@Composable
fun SearchingScreen(navController: NavHostController, vmDating: DatingViewModel) {
    var isNext by rememberSaveable { mutableStateOf(true) }
    var showReport by rememberSaveable { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }

    var currentProfileIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentPotential = remember { mutableStateOf<UserModel?>(null) }
    var resetScrollState by remember { mutableStateOf(false) }
    val state = rememberScrollState()
    LaunchedEffect(resetScrollState, Unit) {
        vmDating.getPotentialUserData {
            //currentPotential.value = profiles.random()
            currentPotential.value = vmDating.getNextPotential(currentProfileIndex)
            isLoading.value = false // Set loading state to false after data is fetched
        }
        if (resetScrollState) {
            state.scrollTo(0)
            // After resetting, reset the state variable to false
            resetScrollState = false
        }
    }
    fun nextProfile(newPotential:UserModel){
        if(newPotential.name.isNotEmpty()){
            currentPotential.value = newPotential
        }else{
            isNext = false
        }
        resetScrollState = true
    }

    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 2,
        settingsButton = { navController.navigate("SearchPreferenceScreen") },
        state = state,
        currentScreen = {
            currentPotential.value?.let { user ->
                val location = calcDistance(user.location, vmDating.getUser().location)
                if (isNext || isLoading.value) {
                    UserInfo(
                        user = user,//usersArray[currentProfileIndex]
                        location = location,
                        bottomButtons = {
                            SearchingButtons(
                                onClickLike = {
                                    currentProfileIndex++
                                    nextProfile(vmDating.likedCurrentPotential(currentProfileIndex, currentPotential.value!!))
                                    /*TODO Add an animation or something*/
                                },
                                onClickPass = {
                                    currentProfileIndex++
                                    nextProfile(vmDating.passedCurrentPotential(currentProfileIndex, currentPotential.value!!))
                                    /*TODO Add an animation or something*/
                                },
                                onClickReport = { showReport = true /*TODO Add an animation or something*/},
                                onClickSuggest = { /*TODO Add an animation or something*/  },
                            )
                        },
                    )
                } else {
                    Comeback()
                }
            }
        },

    )

    if(showReport){
        AlertDialogBox(
            dialogTitle = "Report!",
            onDismissRequest = { showReport = false },
            dialogText = "This account will be looked into and they will not be able to view your profile",
            onConfirmation = { showReport = false
                currentProfileIndex++
                nextProfile(vmDating.reportedCurrentPotential(currentProfileIndex, currentPotential.value!!))
            }
        )
    }

}

@Composable
fun SearchPreferenceScreen(navController: NavHostController, vmDating: DatingViewModel){
    val currentUser = vmDating.getUser()
    val searchPref by remember { mutableStateOf( currentUser.userPref) }

    val userPref= listOf(searchPref.gender, searchPref.zodiac, searchPref.sexualOri, searchPref.mbti,
        searchPref.children, searchPref.familyPlans, searchPref.meetUp, searchPref.education, searchPref.religion, searchPref.politicalViews,
        searchPref.relationshipType, searchPref.intentions, searchPref.drink, searchPref.smoke, searchPref.weed)

    val pref = listOf("Gender", "Zodiac Sign", "Sexual Orientation", "Mbti", "Children", "Family Plans", "Meeting Up",
        "Education", "Religion", "Political Views", "Relationship Type","Intentions", "Drink", "Smokes", "Weed")
    InsideSearchSettings(
        nav = navController,
        searchSettings = {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(15.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AgeSlider(preferredMin = currentUser.userPref.ageRange.min, preferredMax = currentUser.userPref.ageRange.max, vmDating = vmDating, currentUser = currentUser)
                Spacer(modifier = Modifier.height(14.dp))
                DistanceSlider(preferredMax = currentUser.userPref.maxDistance, vmDating = vmDating, currentUser = currentUser)
                Spacer(modifier = Modifier.height(14.dp))
                SeekingBox(desiredSex = currentUser.seeking, navController )
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(Modifier.fillMaxWidth(), color = AppTheme.colorScheme.onBackground, thickness = 2.dp)
                Spacer(modifier = Modifier.height(6.dp))
                GenericTitleSmall(text = "Premium Settings")
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(Modifier.fillMaxWidth(), color = AppTheme.colorScheme.onBackground, thickness = 2.dp)
                Spacer(modifier = Modifier.height(14.dp))
                for (i in pref.indices){
                    OtherPreferences(title = pref[i], navController = navController, searchPref = userPref[i], clickable = true, index = i)
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    )
}
@Composable
fun ChangePreference(navController: NavHostController, title:String, index:Int, vmDating: DatingViewModel){
    if(index == 69420){
        ChangeSeekingScreen(navController,
            title = title,
            vmDating = vmDating,
            index = index,
        )
    }else{
        ChangePreferenceScreen(navController,
            title = title,
            vmDating = vmDating,
            index = index,
        )
    }
}
/*
End of Seeking Screens
Start of Profile Screens
 */
@Composable
fun ProfileScreen(navController: NavHostController, vmDating: DatingViewModel){
    val currentUser = vmDating.getUser()
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (currentUser.name.isNotEmpty()) {
                isLoading.value = false
        }
    }
    val state = rememberScrollState()
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        isPhoto = false,
        titleText = "Profile",
        nav = navController,
        selectedItemIndex = 4,
        settingsButton = { navController.navigate("EditProfileScreen") },
        state = state,
        currentScreen = {
            CurrentUserInfo(
                currentUser
            )
        }
    )
}
@Composable
fun EditProfileScreen(navController: NavHostController, dating:DatingActivity){
    InsideProfileSettings(
        nav = navController,
        editProfile = {
            EditProfile(dating)
        }
    )
}
/*
End of Profile Screens
Start of Message Screens
 */
@Composable
fun ChatsScreen(navController: NavHostController){
    //val inChat by rememberSaveable { mutableStateOf(false)}
    val state = rememberScrollState()
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "Messages", //Change based on name
        isPhoto = false,
        nav = navController,
        selectedItemIndex = 1,
        settingsButton = { },
        state = state,
        currentScreen = {


            MessageStart(
                noMatches = false,
                userPhoto = "https://media.vanityfair.com/photos/63765577474812eb37ec70bc/master/w_1600,c_limit/Headshot%20-%20credit%20%E2%80%9CNational%20Geographic%20for%20Disney+%E2%80%9D.jpg", //Need this to accept URI
                userName = "Dom",
                userLastMessage = "LOL hows have you been? \nLOL hows have you been?",
                openChat = {
                    navController.navigate("MessagerScreen")

                }
            )


        }
    )
}
@Composable
fun MessagerScreen(navController: NavHostController){
    //TODO need to make this nested I think
    var message by rememberSaveable { mutableStateOf("") }

    InsideMessages(
        nav = navController,
        titleText = "Dom",
        value = message,
        onValueChange = { message = it},
        sendMessage = {/* TODO Send Message*/ },
        goToProfile = {/* TODO Go to Profile from name*/ },
        chatSettings = {},
        startCall = {/* TODO Start normal Call (Need to make a screen for it)*/},
        startVideoCall = {/* TODO Start Video Call (Need to make a screen for it)*/},
        sendAttachment = {/* TODO photos or attachements Message...advise if we should keep*/},
        messages = {
            UserMessage("Oh my god I totally agree")
            TheirMessage(replyMessage = "That's crazy because I don't nerd...",
                userPhoto = "https://media.vanityfair.com/photos/63765577474812eb37ec70bc/master/w_1600,c_limit/Headshot%20-%20credit%20%E2%80%9CNational%20Geographic%20for%20Disney+%E2%80%9D.jpg",
                photoClick = { /* TODO Go to Profile image*/ }
            )

        }
    )
}
/*
End of Message Screens
Start of Groups Screens
 */
@Composable
fun GroupsScreen(navController: NavHostController){

    val state = rememberScrollState()
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        isPhoto = false,
        titleText = "Groups",
        nav = navController,
        selectedItemIndex = 3,
        settingsButton = { },
        state = state,
        currentScreen = {

        }
    )
}
@Composable
fun SomeScreen(navController: NavHostController){
    val state = rememberScrollState()
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { },
        state = state,
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
}