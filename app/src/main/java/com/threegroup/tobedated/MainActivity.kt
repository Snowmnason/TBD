package com.threegroup.tobedated

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.threegroup.tobedated.shareclasses.storeImageAttempt
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
        //requestLocationPermission()
    }
    val requestLocationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.values.all { it }) {
                // All permissions granted, proceed
            1+1
            //checkUserTokenAndNavigate()
        }
//        else {
//            // Permission not granted
//            // Handle the case where location permission is not granted
//        }
    }
    private fun requestLocationPermission() {
        val permissionsToRequest = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (!isLocationPermissionGranted(this)) {
            requestLocationPermissionLauncher.launch(permissionsToRequest)
        } else {
            // Permissions already granted, proceed
            //checkUserTokenAndNavigate()
        }
    }

    private fun getLastLocation(callback: (String) -> Unit) {
        var latitude : String
        var longitude : String
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
            //return callback("error/")
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
    fun isLocationPermissionGranted(context: Context): Boolean {
        val coarsePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val finePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        return coarsePermission || finePermission
    }

    fun checkUserTokenAndNavigate(callback: (String, String) -> Unit) {
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
                        val whichActivity = when (activityToken) {
                            "dating" -> "dating"
                            "casual" -> "casual"
                            "friend" -> "friend"
                            else -> "dating"
                        }
                            callback(whichActivity, "no")
                    }
                } else {
                    callback("login", location)
                }
            }
        }
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
        editor.remove("activityToken")
        editor.apply()
    }
    fun setLastActivity(lastActivity: String){
        val sharedPreference = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("activityToken", lastActivity)
        editor.apply()
    }


    fun saveTokenToSharedPreferences(token: String?) {
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_login", token)
        editor.apply()
    }
    fun showToast(message: String, ) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


