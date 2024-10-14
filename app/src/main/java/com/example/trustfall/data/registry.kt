package com.example.trustfall.data

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trustfall.R
import com.example.trustfall.login.ui.theme.primary
import com.example.trustfall.login.ui.theme.secondary
import com.parse.ParseUser


@Composable
fun registry(navController: NavController){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
    ){
        var username by remember { mutableStateOf(TextFieldValue("")) }
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        var email by rememberSaveable { mutableStateOf("") }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape

            ){
                Image(
                    painter = painterResource(R.drawable.icon),
                    contentDescription = "",
                    modifier = Modifier

                )
            }
            Text(
                "Create an Account",
                fontSize = 32.sp
            )
            Text(
                "Find the peace you deserve",
                fontSize = 16.sp
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
                },
                placeholder = { Text("Enter Your Username") }
            )

            OutlinedTextField(
                value = username,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 8.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.White),
                onValueChange = {newText ->
                    email = newText.toString()
                },
                placeholder = { Text("Enter Your Email") }
            )

            OutlinedTextField(
                value = password,
                shape = RoundedCornerShape(15.dp),
                onValueChange = { password = it },
                singleLine = true,
                placeholder = { Text("Enter Your Password") },
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
                    .padding(start = 5.dp, end = 5.dp, top = 8.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.White)
            )
            Button(
                onClick = {
                    var user = ParseUser()

                },
                colors = ButtonDefaults.buttonColors(containerColor = secondary),
                modifier = Modifier
                    .padding(top = 8.dp)
            ){
                Text("Register")
            }
            Button(
                onClick = {
                    navController.navigate("Home")
                },
                colors = ButtonDefaults.buttonColors(containerColor = secondary),
                modifier = Modifier
                    .padding(top = 8.dp)
            ){
                Text("Back")
            }
        }
    }
}