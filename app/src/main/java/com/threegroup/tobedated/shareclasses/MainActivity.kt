package com.threegroup.tobedated.shareclasses

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._login.LoginActivity
import com.threegroup.tobedated.shareclasses.composables.PolkaDotCanvas
import com.threegroup.tobedated.shareclasses.composables.SplashScreen
import com.threegroup.tobedated.shareclasses.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                getLastLocation()
//            } else {
//                // Handle permission denied
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            AppTheme {
                PolkaDotCanvas()
                SplashScreen()
            }
        }

        // Request location permission if not granted

        requestLocationPermission()
        //TODO I dunno how to do this, it askes for permission, if you don't give permission it just hangs when you give them permission
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed
            checkUserTokenAndNavigate()
        }
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
            return "error/"
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
            .addOnFailureListener { _ ->
                // Handle failure to retrieve location

            }
        return "$latitude/$longitude"
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }



    private fun checkUserTokenAndNavigate() {
        lifecycleScope.launch {
            val sharedPreferences = getSharedPreferences("firebase_user", Context.MODE_PRIVATE)
            val userToken = sharedPreferences.getString("firebase_user_token", null)
            val location = getLastLocation()
            if (userToken != null) {
                navigateToDatingActivity(userToken, location)
            } else {
                navigateToLoginActivity(location)
            }
        }
    }

    private fun navigateToDatingActivity(userToken: String, location:String) {
        val intent = Intent(this, DatingActivity::class.java)
        intent.putExtra("token", userToken)
        intent.putExtra("location", location)
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginActivity(location:String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("location", location)
        startActivity(intent)
        finish()
    }
}





