package com.example.trustfall.Screens

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trustfall.MainActivity
import com.example.trustfall.R
import com.example.trustfall.ui.theme.primary
import com.example.trustfall.ui.theme.secondary
import com.parse.ParseUser
import java.util.regex.Pattern

@Composable
fun registry(navController: NavController, context: Context){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
    ){
        val fontFamily = fontfamily("Lobster Two")
        var username by remember { mutableStateOf(TextFieldValue("")) }
        var firstName by remember { mutableStateOf(TextFieldValue("")) }
        var lastName by remember { mutableStateOf(TextFieldValue("")) }
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        var email by rememberSaveable { mutableStateOf("") }
        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier
                .wrapContentSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier.padding(top = 10.dp)
                ){
                    Image(
                        painter = painterResource(R.drawable.icon),
                        contentDescription = "",

                    )
                }
                Text(
                    fontFamily = fontFamily,
                    text = "Create an Account",
                    fontSize = 32.sp
                )
                Text(
                    "Find the peace you deserve",
                    fontSize = 16.sp,
                    color = Color.Gray
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
                    value = firstName,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(Color.White),
                    onValueChange = { newText ->
                        firstName = newText
                    },
                    placeholder = {Text("Enter Your First Name")}
                )
                OutlinedTextField(
                    value = lastName,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(Color.White),
                    onValueChange = { newText ->
                        lastName = newText
                    },
                    placeholder = {Text("Enter Your Last Name")}
                )
                OutlinedTextField(
                    value = email,
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
                registryButton(password, username.text, email, firstName.text, lastName.text, context)
                navButton("Home", navController, "Back")
                Spacer(Modifier.padding(bottom = 5.dp))
            }
        }
    }
}

@Composable
fun registryButton(password: String, username: String, firstName: String, lastName: String, email: String, context: Context){
    OutlinedButton(
        shape = RoundedCornerShape(8.dp),
        onClick = {
            if(requirementCheck(password, username, firstName, lastName) && emailCheck(email, context) && usernameUnique(username, context)) {
                var friendsList = ArrayList<ParseUser>()
                var user = ParseUser()
                user.put("firstName", firstName.replaceFirstChar { it.uppercase() })
                user.put("lastName", lastName.replaceFirstChar { it.uppercase() })
                user.username = username
                user.setPassword(password)
                user.email = email
                user.put("friendsList", friendsList)
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

fun usernameUnique(username: String, context: Context): Boolean {
    val userQuery = ParseUser.getQuery()
    var result = true
    userQuery.findInBackground { objects, e ->
        if(e != null){
            Toast.makeText(context, "Unable to register", Toast.LENGTH_SHORT).show()
            return@findInBackground
        }
        for (x in objects){
            if(username == x.username){
                result = false
                break
            }
        }
    }
    return result
}

fun requirementCheck(password: String, username: String, firstName: String, lastName: String): Boolean {
    val usernamePattern: Pattern = Pattern.compile("[A-Za-z0-9]{5,24}")
    val namePattern: Pattern = Pattern.compile("[A-Za-z0-9]{3,20}")
    val passwordPattern: Pattern = Pattern.compile("[a-zA-Z0-9!@#$]{8,24}")
    if (password.isNullOrBlank() || username.isNullOrBlank()) {
        return false
    }
    if (!passwordPattern.matcher(password).matches() && !usernamePattern.matcher(username)
            .matches()
    ) {
        return false
    }
    if (!namePattern.matcher(firstName).matches()) return false
    if (!namePattern.matcher(lastName).matches()) return false
    return true
}


fun emailCheck(email: String, activity: Context): Boolean {
    if(email.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        Toast.makeText(activity, "Invalid Email", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}
