package com.example.trustfall.screens

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustfall.LoginActivity
import com.example.trustfall.data.settingNavItem
import com.example.trustfall.ui.theme.primary
import com.parse.LogInCallback
import com.parse.ParseUser
import java.util.regex.Pattern


@Composable
fun SettingsScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "SettingsPage"
    ) {
        composable("SettingsPage") {
            SettingPage(navController)
        }
        composable("Account Settings") {
            AccountSettingPage(navController)
        }
        composable("Emergency Contact") {
            PreviousPage(navController)
        }
        composable("Contact Support") {
            SettingPage(navController)
        }
        composable("Terms of Service") {
            TermsAndService(navController)
        }
        composable("Log Out") {
            LogOut(navController)
        }
        composable("Change Username") {
            ChangeUsernameDisplay(navController)
        }
        composable("Change Password") {
            ChangePasswordDisplay(navController)
        }
    }
}

@Composable
fun ChangePasswordDisplay(navController: NavHostController) {
    val context = LocalContext.current as Activity
    var oldPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var newPasswordConfirm by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var newpasswordVisible by rememberSaveable { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(primary)
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(5.dp),
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Text(
                "Update your password",
                fontFamily = fontfamily("Oswald"),
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            OutlinedTextField(
                value = oldPassword,
                shape = RoundedCornerShape(15.dp),
                onValueChange = { oldPassword = it },
                singleLine = true,
                placeholder = {
                    Text(
                        "Enter Your Password",
                        fontFamily = fontfamily("Oswald")
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 8.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.White),
                textStyle = TextStyle(fontFamily = fontfamily("Oswald"))
            )
            OutlinedTextField(
                value = newPassword,
                shape = RoundedCornerShape(15.dp),
                onValueChange = { newPassword = it },
                singleLine = true,
                placeholder = {
                    Text(
                        "Enter Your New Password",
                        fontFamily = fontfamily("Oswald")
                    )
                },
                visualTransformation = if (newpasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (newpasswordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (newpasswordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { newpasswordVisible = !newpasswordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 8.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.White),
                textStyle = TextStyle(fontFamily = fontfamily("Oswald"))
            )
            OutlinedTextField(
                value = newPasswordConfirm,
                shape = RoundedCornerShape(15.dp),
                onValueChange = { newPasswordConfirm = it },
                singleLine = true,
                placeholder = {
                    Text(
                        "Re-Enter Your Password",
                        fontFamily = fontfamily("Oswald")
                    )
                },
                visualTransformation = if (newpasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (newpasswordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (newpasswordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { newpasswordVisible = !newpasswordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 8.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.White),
                textStyle = TextStyle(fontFamily = fontfamily("Oswald"))
            )
            Spacer(Modifier.padding(5.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    ParseUser.logInInBackground(
                        ParseUser.getCurrentUser().username,
                        oldPassword,
                        LogInCallback { user, e ->
                            if (e != null) {
                                Toast.makeText(
                                    context,
                                    "Unable to confirm password. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@LogInCallback
                            }
                            if (newPassword.equals(newPasswordConfirm)) {
                                val passwordPattern: Pattern =
                                    Pattern.compile("[a-zA-Z0-9!@#$]{5,24}")
                                if (!passwordPattern.matcher(newPassword).matches()) {
                                    Toast.makeText(
                                        context,
                                        "New password does not meet requirements, please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@LogInCallback
                                }
                                val user = ParseUser.getCurrentUser()
                                user.setPassword(newPassword)
                                user.saveInBackground { e ->
                                    if (e != null) {
                                        Toast.makeText(
                                            context,
                                            "Unable to save new password, please try again.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Log.e("Settings.kt", "PasswordChangeException: " + e)
                                        return@saveInBackground
                                    }
                                    Toast.makeText(context, "Successfully changed", Toast.LENGTH_SHORT).show()
                                    navController.navigate("Account Settings")
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Password does not match, please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@LogInCallback
                            }
                        })
                }
            ) {
                Text("Change password", fontFamily = fontfamily("Oswald"))
            }
        }
    }
}

@Composable
fun ChangeUsernameDisplay(navController: NavHostController) {
    val context = LocalContext.current as Activity
    var username by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(primary)
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(5.dp),
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Text(
                "Update your username",
                fontFamily = fontfamily("Oswald"),
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Username",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp, end = 5.dp, top = 16.dp),
                fontFamily = fontfamily("Oswald"),
                fontSize = 24.sp
            )
            OutlinedTextField(
                value = username,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 8.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.White),
                onValueChange = { newText ->
                    username = newText
                },
                textStyle = TextStyle(fontFamily = fontfamily("Oswald")),
                placeholder = {
                    Text(
                        ParseUser.getCurrentUser().username,
                        fontFamily = fontfamily("Oswald"),
                        color = Color.Gray
                    )
                }
            )
            Spacer(modifier = Modifier.padding(bottom = 5.dp))
            Button(
                onClick = {
                    if (usernameUnique(username, context)) {
                        val usernamePattern: Pattern = Pattern.compile("[A-Za-z0-9]{5,24}")
                        if (!usernamePattern.matcher(username).matches()) {
                            Toast.makeText(
                                context,
                                "Username does not meet requirement. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        val user = ParseUser.getCurrentUser()
                        user.username = username
                        user.saveInBackground { e ->
                            if (e != null) {
                                Toast.makeText(
                                    context,
                                    "Unable to save. Please try again later",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@saveInBackground
                            } else {
                                Toast.makeText(context, "Successfully changed", Toast.LENGTH_SHORT)
                                    .show()
                                navController.navigate("Account Settings")
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Username already exists. Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Change username")
            }
            Spacer(modifier = Modifier.padding(bottom = 5.dp))
        }
    }
}

@Composable
fun TermsAndService(navController: NavHostController) {
    Text(
        "Copyright 2024 TrustFall Inc\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.",
        modifier = Modifier
            .padding(5.dp)
            .wrapContentSize(),
        fontFamily = fontfamily("Oswald"),
        fontSize = 12.sp
    )
}

@Composable
fun PreviousPage(navController: NavHostController) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text("Emergency")
    }
}

@Composable
fun LogOut(navController: NavHostController) {
    ParseUser.logOut()
    val activity = LocalContext.current as? Activity
    val intent = Intent(activity, LoginActivity::class.java)
    activity?.startActivity(intent)
    activity?.finish()
}

@Composable
fun SettingPage(navController: NavHostController) {
    val TextwithIconFont = fontfamily("Oswald")
    val navigation = listOf(
        settingNavItem("Account Settings", TextwithIconFont, Icons.Default.Person),
        settingNavItem("Emergency Contact", TextwithIconFont, Icons.Default.Phone),
        settingNavItem("Contact Support", TextwithIconFont, Icons.Default.SupportAgent),
        settingNavItem("Terms of Service", TextwithIconFont, Icons.Default.AccountBalance),
        settingNavItem("Log out", TextwithIconFont, Icons.Default.Logout)
    )
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .background(primary)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Settings", fontFamily = fontfamily("Lobster Two"), fontSize = 32.sp)
            Spacer(modifier = Modifier.padding(5.dp))
            navigation.forEachIndexed { index, settingNavItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable(
                            onClick = {
                                navController.navigate(settingNavItem.title)
                            }
                        ),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        settingNavItem.imageVector, contentDescription = "asdf", modifier = Modifier
                            .padding(end = 5.dp)
                            .clip(
                                CircleShape
                            )
                    )
                    Text(
                        fontFamily = settingNavItem.fontFamily,
                        text = settingNavItem.title,
                        fontSize = 25.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(">")
                }
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val borderSize = 1.dp.toPx()
                        val y = size.height - borderSize
                        drawLine(
                            color = Color.Gray,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = borderSize
                        )

                    })
            }
        }
    }
}

@Composable
fun AccountSettingPage(navController: NavHostController) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .background(primary)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "Account Settings",
                fontFamily = fontfamily("Lobster Two"),
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(5.dp))
            val settingsItemList = listOf(
                settingNavItem("Change Username", fontfamily("Oswald"), Icons.Default.VerifiedUser),
                settingNavItem("Change Password", fontfamily("Oswald"), Icons.Default.Password)
            )
            settingsItemList.forEachIndexed { index, settingNavItem ->
                TextWithIcon(navController, settingNavItem)
            }
        }
    }
}

@Composable
fun TextWithIcon(navController: NavHostController, settingNavItem: settingNavItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable(
                onClick = {
                    navController.navigate(settingNavItem.title)
                }
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            settingNavItem.imageVector,
            contentDescription = "asdf",
            modifier = Modifier.padding(end = 5.dp)
        )
        Text(
            fontFamily = settingNavItem.fontFamily,
            text = settingNavItem.title,
            fontSize = 24.sp
        )
        Spacer(Modifier.weight(1f))
        Text(">")
    }

    Spacer(modifier = Modifier
        .fillMaxWidth()
        .drawBehind {
            val borderSize = 1.dp.toPx()
            val y = size.height - borderSize
            drawLine(
                color = Color.Gray,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = borderSize
            )
        }
    )
}

