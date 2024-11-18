package com.example.trustfall.Screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.trustfall.data.bottomNavItem
import com.example.trustfall.ui.theme.primary
import com.example.trustfall.ui.theme.secondary


@Composable
fun MainScreen(){
    val bottomNavItemList = listOf(
        bottomNavItem("Friends", Icons.Default.Contacts),
        bottomNavItem("Home", Icons.Default.Add),
        bottomNavItem("Settings", Icons.Default.Settings)
    )
    var selectedIndex by remember { mutableIntStateOf(1) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar (
            modifier = Modifier.border(1.dp, Color.Black),
            containerColor = secondary
            ){
            bottomNavItemList.forEachIndexed { index, bottomNavItem ->
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                    },
                    icon = {
                        Icon(imageVector = bottomNavItem.icon, contentDescription = "Icon")
                    },
                    label = { Text(text = bottomNavItem.name, fontFamily = fontfamily("Oswald")) }
                )
            }
        } },
        containerColor = primary
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}
@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int){
    when(selectedIndex){
        0-> friendsListView()
        1-> HomePage()
        2-> SettingsScreen()
    }
}
