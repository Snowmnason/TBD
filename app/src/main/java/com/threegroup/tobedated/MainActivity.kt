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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.threegroup.tobedated._causal.CausalActivity
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._friends.FriendsActivity
import com.threegroup.tobedated._login.LoginActivity
import com.threegroup.tobedated.composeables.composables.PolkaDotCanvas
import com.threegroup.tobedated.composeables.composables.SplashScreen
import com.threegroup.tobedated.shareclasses.MyApp
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
            AppTheme(
                activity = activityToken
            ) {
                PolkaDotCanvas()
                when (activityToken) {
                    "dating" -> {
                        SplashScreen(activity = activityToken, text1 = "To Be Dated")
                    }

                    "causal" -> {
                        SplashScreen(activity = activityToken, text1 = "To Be Casual")
                    }

                    "friend" -> {
                        SplashScreen(activity = activityToken, text1 = "To Be Friended")
                    }
                }
            }
        }

        // Request location permission if not granted
        requestLocationPermission()
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
    private fun requestLocationPermission() {
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

    private fun checkUserTokenAndNavigate() {
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

                    when (activityToken) {
                        "dating" -> {
                            navigateToDatingActivity()
                        }
                        "causal" -> {
                            navigateToCausalActivity()
                        }
                        "friend" -> {
                            navigateToFriendsActivity()
                        }
                        else -> {
                            navigateToDatingActivity()
                        }
                    }
                }
                } else {
                    navigateToLoginActivity(location)
                }
            }
        }
    }
    private fun navigateToDatingActivity() {
        val intent = Intent(this, DatingActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun navigateToCausalActivity() {
        val intent = Intent(this, CausalActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun navigateToFriendsActivity() {
        val intent = Intent(this, FriendsActivity::class.java)
        startActivity(intent)
        finish()
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
}