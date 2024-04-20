package com.threegroup.tobedated.shareclasses

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
import com.threegroup.tobedated.shareclasses.composables.PolkaDotCanvas
import com.threegroup.tobedated.shareclasses.composables.SplashScreen
import com.threegroup.tobedated.shareclasses.theme.AppTheme
import kotlinx.coroutines.launch

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
                SplashScreen()
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
                    when (activityToken) {
                        "dating" -> {
                            navigateToDatingActivity(userToken, location)
                        }
                        "causal" -> {
                            navigateToCausalActivity(userToken, location)
                        }
                        "friend" -> {
                            navigateToFriendsActivity(userToken, location)
                        }
                        else -> {
                            navigateToDatingActivity(userToken, location)
                        }
                    }

                } else {
                    navigateToLoginActivity(location)
                }
            }
        }
    }
    private fun navigateToDatingActivity(userToken: String, location: String) {
        val intent = Intent(this, DatingActivity::class.java)
        intent.putExtra("token", userToken)
        intent.putExtra("location", location)
        startActivity(intent)
        finish()
    }
    private fun navigateToCausalActivity(userToken: String, location: String) {
        val intent = Intent(this, CausalActivity::class.java)
        intent.putExtra("token", userToken)
        intent.putExtra("location", location)
        startActivity(intent)
        finish()
    }
    private fun navigateToFriendsActivity(userToken: String, location: String) {
        val intent = Intent(this, FriendsActivity::class.java)
        intent.putExtra("token", userToken)
        intent.putExtra("location", location)
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