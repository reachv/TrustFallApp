package com.example.trustfall

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import com.example.trustfall.data.bottomNavItem


val dest = listOf(
    bottomNavItem(
        name = "Home",
        icon = Icons.Rounded.Home
    ),
    bottomNavItem(
        name = "Trips",
        icon = Icons.Rounded.Home
    ),
    bottomNavItem(
        name = "Settings",
        icon = Icons.Rounded.Home
    )
)


@Composable
fun bottomNavBar(){

}