package com.threegroup.tobedated

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.threegroup.tobedated.composables.PolkaDotCanvas
import com.threegroup.tobedated.ui.theme.AppTheme

class DatingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                PolkaDotCanvas()

            }
        }
    }
}