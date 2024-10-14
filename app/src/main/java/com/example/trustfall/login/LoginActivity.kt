package com.example.trustfall.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustfall.data.loginView
import com.example.trustfall.data.registry
import com.example.trustfall.data.startScreen
import com.example.trustfall.login.ui.theme.TrustFallTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrustFallTheme {
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
                        registry(navController)
                    }
                }
            }
        }
    }
}
