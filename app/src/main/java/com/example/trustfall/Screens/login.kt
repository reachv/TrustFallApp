package com.example.trustfall.Screens

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trustfall.MainActivity
import com.example.trustfall.R
import com.example.trustfall.dataStore.settingsDataStore
import com.example.trustfall.ui.theme.primary
import com.parse.LogInCallback
import com.parse.ParseUser
import kotlinx.coroutines.launch

@Composable
fun loginView(navController: NavController){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(primary),
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(300.dp, 600.dp)
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .background(primary)
        ){
            usernameET(navController)
        }
    }
}

@Composable
fun usernameET(navController: NavController){
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity
    val dataStoreContext = LocalContext.current
    val settingsDataStore = settingsDataStore(dataStoreContext)
    var checkboxState by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        checkboxState = settingsDataStore.getFromDataStore()
    }
    Card (Modifier.wrapContentSize(), elevation = CardDefaults.cardElevation(10.dp)){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier.padding(10.dp)

            ){
                Image(
                    painter = painterResource(R.drawable.icon),
                    contentDescription = "",
                    modifier = Modifier

                )
            }
            Text(
                text = "Username",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp, end = 5.dp, top = 16.dp)
            )
            OutlinedTextField(
                value = username,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 8.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.White),
                onValueChange = {newText ->
                    username = newText
                }
            )
            Text(
                text = "Password",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp, end = 5.dp, bottom = 8.dp, top = 8.dp)
            )

            OutlinedTextField(
                value = password,
                shape = RoundedCornerShape(15.dp),
                onValueChange = { password = it },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                },
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.White)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.Start)
            ) {
                Checkbox(
                    checked = checkboxState,
                    onCheckedChange = { x ->
                        scope.launch {
                            settingsDataStore.saveState(x)
                        }
                        checkboxState = x

                    }
                )
                Text(
                    text = "Remember me?"
                )
            }
            Button(
                onClick = {
                    ParseUser.logInInBackground(username.text, password, LogInCallback { user, e ->
                        if(e != null){
                            Log.e("Login.kt", "LoginEception: " + username.text + password)
                            Toast.makeText(activity, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show()
                            return@LogInCallback
                        }
                        goMainActivity(activity)
                    })
                }
            ) {
                Text("Login")
            }
            navButton("Home", navController, "Back")
            Spacer(Modifier.padding(bottom = 5.dp))
        }
    }
}

fun goMainActivity(activity: Activity?) {
    val intent = Intent(activity, MainActivity::class.java)
    activity?.startActivity(intent)
    activity?.finish()
}

