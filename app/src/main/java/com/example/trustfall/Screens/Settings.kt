package com.example.trustfall.Screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustfall.LoginActivity
import com.example.trustfall.data.settingNavItem
import com.example.trustfall.ui.theme.primary
import com.example.trustfall.ui.theme.secondary
import com.parse.ParseUser


@Composable
fun SettingsScreen(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "SettingsPage"
    ){
        composable("SettingsPage"){
            SettingPage(navController)
        }
        composable("Account Settings"){
            AccountSettingPage(navController)
        }
        composable("Emergency Contact"){
            PreviousPage(navController)
        }
        composable("Contact Support"){
            SettingPage(navController)
        }
        composable("Terms of Service"){
            TermsAndService(navController)
        }
        composable("Log Out"){
            LogOut(navController)
        }
    }
}

@Composable
fun TermsAndService(navController: NavHostController) {
    Text("Copyright 2024 TrustFall Inc\n" +
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
    Surface (modifier = Modifier.fillMaxSize().background(Color.White)) {
        Text("Emergency")
    }
}
@Composable
fun LogOut(navController: NavHostController){
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
    Card (
        modifier = Modifier.wrapContentSize().padding(5.dp),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ){
        Column (
            modifier = Modifier
                .background(primary)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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
                    Icon(settingNavItem.imageVector, contentDescription = "asdf", modifier = Modifier.padding(end = 5.dp).clip(
                        CircleShape))
                    Text(
                        fontFamily = settingNavItem.fontFamily,
                        text = settingNavItem.title,
                        fontSize = 25.sp)
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
    Column(
        modifier = Modifier
            .background(primary)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ){
        Text("Account Settings", fontFamily = fontfamily("Lobster Two"), fontSize = 32.sp)
        val settingsItemList = listOf(
            settingNavItem("Change Username", fontfamily("Oswald"), Icons.Default.VerifiedUser),
            settingNavItem("Change Password", fontfamily("Oswald"), Icons.Default.Password)
        )
        settingsItemList.forEachIndexed { index, settingNavItem ->
            TextWithIcon(navController, settingNavItem)
        }
    }
}

@Composable
fun TextWithIcon(navController : NavHostController, settingNavItem: settingNavItem){
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
        Icon(settingNavItem.imageVector, contentDescription = "asdf", modifier = Modifier.padding(end = 5.dp))
        Text(
            fontFamily = settingNavItem.fontFamily,
            text = settingNavItem.title,
            fontSize = 24.sp)
    }
    Spacer(modifier = Modifier.height(7.dp))
}

