package com.threegroup.tobedated

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.threegroup.tobedated._dating.DatingNav
import com.threegroup.tobedated._login.LoginActivity
import com.threegroup.tobedated.composeables.composables.PolkaDotCanvas
import com.threegroup.tobedated.composeables.composables.SplashScreen
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.shareclasses.storeImageAttempt
import com.threegroup.tobedated.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        var activityToken = sharedPreferences.getString("activityToken", null)
        if(activityToken == null){
            activityToken = "main"
        }
        setContent {
            AppNav(this, activityToken)

        }

    }
    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Location permission granted
            checkUserTokenAndNavigate()
        } else {
            // Permission not granted
            // Handle the case where location permission is not granted
        }
    }
    fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission already granted, proceed
            checkUserTokenAndNavigate()
        }
    }

    private fun getLastLocation(callback: (String) -> Unit) {
        var latitude : String
        var longitude : String
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return callback("error/")
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
                callback("$latitude/$longitude")
            }
            .addOnFailureListener { _ ->
                // Handle failure to retrieve location
                callback("error/")
            }
    }

    private fun checkUserTokenAndNavigate():String {
        var whereTo:String = "login"
        lifecycleScope.launch {
            val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val userToken = sharedPreferences.getString("user_login", null)
            val activityToken = sharedPreferences.getString("activityToken", null)
            getLastLocation { location ->
                if (userToken != null) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                            MyApp.x.setUserInfo(userToken, location).collect { userInfo ->
                                MyApp._signedInUser.value = userInfo
                            }
                        }
                        whereTo = when (activityToken) {
                        "dating" -> "dating"
                        "casual" -> "casual"
                        "friend" -> "friend"
                        else -> "dating"
                    }
                }
                } else {
                    whereTo = "login"
                    navigateToLoginActivity(location)
                }
            }
        }
        return whereTo
    }

    private fun navigateToLoginActivity(location: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("location", location)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
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
}
@Composable
fun AppNav(mainActivity: MainActivity, activityToken:String) {
    val navMain = rememberNavController()
    val navController = rememberNavController()
    val vmApi = viewModel { ApiViewModel(MyApp.x) }
    vmApi.fetchWordOfTheDay()
    //vmApi.fetchHoroscope(MyApp.signedInUser.value!!.star)
    vmApi.fetchPoem()
    var inMain by remember { mutableStateOf(true) }
    var insideWhat by remember { mutableStateOf("") }
    NavHost(navController = navController, startDestination = "SlashScreen",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {
        composable(route = "SlashScreen") {
            AppTheme(activityToken){
                SlashScreen(activityToken = activityToken)
            }
        }
        composable(route = "TopAndBotBarsDating") {
            navMain.popBackStack()
            AppTheme(activity = "DatingNav") {
                TopAndBotBars(
                    vmApi = vmApi,
                    mainNav = navMain,
                    datingClick = {},
                    casualClick = {},
                    friendClick = {},
                    screenNav = { nav ->
                        DatingNav(mainActivity, nav, vmApi,
                            insideWhat ={ inside ->
                                insideWhat = inside
                                inMain = inside == "Main"
                            })
                    },
                    inMainPass = inMain,
                    insideWhatPass = insideWhat
                )
            }
        }
        composable(route = "CasualNav") {
//            navMain.popBackStack()
//            AppTheme(activity = "casual") {
//                TopAndBotBars(
//                    vmApi = vmApi,
//                    mainNav = navMain,
//                    datingClick = {},
//                    casualClick = {},
//                    friendClick = {},
//                    screenNav = { nav ->
//                        CasualNav(dating, nav, vmApi,
//                            insideWhat ={ inside ->
//                                insideWhat = inside
//                                inMain = inside == "Main"
//                            })
//                    },
//                    inMainPass = inMain,
//                    insideWhatPass = insideWhat
//                )
//            }
        }
        composable(route = "FriendsNav") {
            navMain.popBackStack()
            AppTheme(activity = "friend") {
                //DatingNav(mainActivity)
            }
        }
        composable(route = "SignUp") {
            navMain.popBackStack()
            AppTheme(activity = "dating") {
                //Signup(mainActivity)
            }
        }
        composable(route = "Login") {
            AppTheme(activity = "dating") {
                //login(mainActivity)
            }
        }

    }
}
enum class MainRoots {
    Login,
    SignUp,
    TopAndBotBarsDating,
    TopAndBotBarsCasual,
    TopAndBotBarsFriends,
}
@Composable
fun SlashScreen(activityToken:String){
    PolkaDotCanvas()
    when (activityToken) {
        "dating" -> {
            SplashScreen(activity = activityToken, text1 = "To Be Dated")
        }
        "casual" -> {
            SplashScreen(activity = activityToken, text1 = "To Be Casual")
        }
        "friend" -> {
            SplashScreen(activity = activityToken, text1 = "To Be Friended")
        }
    }
    //mainActivity.requestLocationPermission()
}
