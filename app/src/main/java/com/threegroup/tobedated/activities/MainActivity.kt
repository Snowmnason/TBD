package com.threegroup.tobedated.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.threegroup.tobedated.composables.PolkaDotCanvas
import com.threegroup.tobedated.composables.SplashScreen
import com.threegroup.tobedated.ui.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, proceed with your logic
                checkUserTokenAndNavigate()
            } else {
                // Permission denied, show a message or request permission again
                requestLocationPermission()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                PolkaDotCanvas()
                SplashScreen()
            }
        }

        // Request location permission if not granted
        requestLocationPermission()
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
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
    private fun checkUserTokenAndNavigate() {
        lifecycleScope.launch {
            val sharedPreferences =
                getSharedPreferences("firebase_user", Context.MODE_PRIVATE)
            val userToken = sharedPreferences.getString("firebase_user_token", null)
            if (userToken != null) {
                navigateToDatingActivity(userToken)
            } else {
                navigateToLoginActivity()
            }
        }
    }

    private fun navigateToDatingActivity(userToken: String) {
        val intent = Intent(this, DatingActivity::class.java)
        intent.putExtra("token", userToken)
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}





