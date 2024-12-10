package com.example.trustfall

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.trustfall.data.LocationService
import com.example.trustfall.data.bottomNavItem
import com.example.trustfall.screens.HomePage
import com.example.trustfall.screens.SettingsScreen
import com.example.trustfall.screens.fontfamily
import com.example.trustfall.screens.friendsListView
import com.example.trustfall.ui.theme.TrustFallTheme
import com.example.trustfall.ui.theme.primary
import com.example.trustfall.ui.theme.secondary
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

class MainActivity : ComponentActivity() {
    var isConnected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrustFallTheme {
                MainScreen()
            }
        }
    }
    @Composable
    fun MainScreen(){
        val context = LocalContext.current as Activity
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
                                val intent = Intent(context, LocationService::class.java).apply {
                                    action = LocationService.ACTION_STOP}
                                context.startService(intent)
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
}


