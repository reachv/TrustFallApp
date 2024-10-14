package com.example.trustfall.data

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trustfall.login.ui.theme.primary


@Composable
fun startScreen(navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primary),
        contentAlignment = Alignment.Center
    ){
        Column {
            Text(
                "Welcome",
                modifier = Modifier
                    .padding(16.dp))
            Text(
                "Login",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate("Login")
                })
            Text(
                "Sign up",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate("Register")
                })
        }
    }
}