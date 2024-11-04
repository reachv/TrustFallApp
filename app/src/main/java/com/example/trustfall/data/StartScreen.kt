package com.example.trustfall.data

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trustfall.MainActivity
import com.example.trustfall.R
import com.example.trustfall.login.ui.theme.primary
import com.example.trustfall.login.ui.theme.secondary
import com.parse.ParseUser
import java.util.regex.Pattern

@Composable
fun startScreen(navController: NavController){

    val fontFamily = fontfamily("Lobster Two")
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
                fontSize = 32.sp,
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

fun emailCheck(email: String): Boolean {
    if(email.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return false
    }
    return true
}

@Composable
fun registryButton(password: String, username: String, email: String, context: Context){
    OutlinedButton(
        shape = RoundedCornerShape(8.dp),
        onClick = {
            if(requirementCheck(password, username) && emailCheck(email)) {
                var user = ParseUser()
                user.username = username
                user.setPassword(password)
                user.email = email
                user.signUpInBackground {
                    if(it != null){
                        Log.e("StartScreen", "SignupException:" + it)
                        Toast.makeText(context, "Unable to signup, Please try again", Toast.LENGTH_SHORT).show()
                        return@signUpInBackground
                    }
                    var intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            }else{
                Toast.makeText(context, "Username and/or Password does not meet requirement", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier.padding(top = 5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = secondary, contentColor = Color.Black)
    ) {
        Text("Create account")
    }
}

fun requirementCheck(password: String, username: String): Boolean {
    val usernamePattern : Pattern = Pattern.compile("[A-Za-z0-9]{5,24}")
    val passwordPattern : Pattern = Pattern.compile("[a-zA-Z0-9!@#$]{8,24}")
    if(password.length > 15 || username.length > 15 || password.isNullOrBlank() || username.isNullOrBlank()){
        return false
    }
    if(!passwordPattern.matcher(password).matches() && !usernamePattern.matcher(username).matches()) {
        return false
    }
    return true
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