package com.devfest.wellnest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.devfest.wellnest.navigation.AppNavigation
import com.devfest.wellnest.ui.theme.WellNestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WellNestTheme {
                AppNavigation()
            }
        }
    }
}
