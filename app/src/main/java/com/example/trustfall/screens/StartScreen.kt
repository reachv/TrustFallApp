package com.example.trustfall.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trustfall.R
import com.example.trustfall.dataStore.settingsDataStore
import com.example.trustfall.ui.theme.primary
import com.example.trustfall.ui.theme.secondary
import com.parse.ParseUser

@Composable
fun startScreen(navController: NavController){

    val fontFamily = fontfamily("Lobster Two")
    val dataStoreContext = LocalContext.current
    val settingsDataStore = settingsDataStore(dataStoreContext)
    var checkboxState by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        checkboxState = settingsDataStore.getFromDataStore()
    }
    if(checkboxState && ParseUser.getCurrentUser() != null){
        goMainActivity(activity = LocalContext.current as? Activity)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primary),
        contentAlignment = Alignment.Center
    ){
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Welcome",
                fontFamily = fontFamily,
                fontSize = 50.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
            Button(
                shape = RectangleShape,
                onClick = {
                    navController.navigate("Login")
                },
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    "Login",
                    modifier = Modifier
                        .clickable {
                            navController.navigate("Login")
                        },
                    fontFamily = fontfamily("Oswald")
                )
            }
            Button(
                shape = RectangleShape,
                onClick = {
                    navController.navigate("Login")
                },
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    "Sign up",
                    modifier = Modifier
                        .clickable {
                            navController.navigate("Register")
                        },
                    fontFamily = fontfamily("Oswald")
                )
            }
        }
    }
}

fun fontfamily(font: String) : FontFamily{
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    val font = GoogleFont(font)
    val fontfamily = FontFamily(
        Font(
            googleFont = font,
            fontProvider = provider
        )
    )
    return fontfamily
}
@Composable
fun navButton(dest: String, navController: NavController, name: String){
    OutlinedButton(
        shape = RoundedCornerShape(8.dp),
        onClick = {
            navController.navigate(dest)
        },
        colors = ButtonDefaults.buttonColors(containerColor = secondary, contentColor = Color.Black),
    ){
        Text(name)
    }
}