package com.example.trustfall

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustfall.screens.loginView
import com.example.trustfall.screens.registry
import com.example.trustfall.screens.startScreen
import com.example.trustfall.ui.theme.TrustFallTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrustFallTheme {
                val context: Context = this
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "Home"
                ){
                    composable("Home"){
                        startScreen(navController)
                    }
                    composable("Login"){
                        loginView(navController)
                    }
                    composable("Register"){
                        registry(navController, context)
                    }
                }
            }
        }
    }
}

