package com.example.trustfall.screens

import android.Manifest
import android.app.Activity
import android.app.Activity.BIND_AUTO_CREATE
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustfall.data.LocationService
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun HomePage(){
    val context = LocalContext.current as Activity
    var navHostController = rememberNavController()
    var startDestination = if(permissionCheck(context)) "mapsDisplay" else "permissionsRequestDisplay"
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ){
        composable("mapsDisplay"){
            mapsDisplay()
        }
        composable("permissionsRequestDisplay"){
            permissionsRequestDisplay(navHostController)
        }
    }
}

@Composable
fun permissionsRequestDisplay(navController: NavController) {
    var context = LocalContext.current as Activity
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("You have not given the necessary permissions.")
        Button(
            onClick = {
                requestPermission(context)
                if(permissionCheck(context)){
                    navController.navigate("mapsDisplay")
                }
            }
        ){
        }
    }
}

@Composable
fun mapsDisplay() {
    var isCurrent by remember { mutableStateOf(false) }
    val context = LocalContext.current as Activity
    lateinit var tester : LocationService.LocationBinder
    var userPosition by remember { mutableStateOf(LatLng(0.toDouble(), 0.toDouble())) }
    var cameraPosition = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(userPosition, 15f) }
    var cameraPositionState by remember { mutableStateOf(cameraPosition) }

    val LocationHandler = Handler(Looper.getMainLooper()){
        userPosition = LatLng(it.data.getDouble("lat"), it.data.getDouble("long"))
        if(!isCurrent){
            cameraPositionState = CameraPositionState(
                position = CameraPosition.fromLatLngZoom(
                    userPosition, 10f
                )
            )
            isCurrent = true
        }
        Log.e("testings", userPosition.toString())
        true
    }
    val connection = object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            tester = p1 as LocationService.LocationBinder
            tester.setHandler(LocationHandler)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }
    }

    val intent = Intent(context, LocationService::class.java)
    context.bindService(
        intent,
        connection,
        BIND_AUTO_CREATE
        )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.padding(bottom = 75.dp),
            cameraPositionState = cameraPositionState
        ){
            Marker(
                state = MarkerState(userPosition)
            ){

            }
        }
    }
    Row {
        Button(
            onClick = {intent.apply { action = LocationService.ACTION_START
                context.startService(this)}}
        ) {
            Text("Start Tracking")
        }
        Button(
            onClick = {intent.apply { action = LocationService.ACTION_STOP
                context.startService(this)}}
        ) {
            Text("Stop Tracking")
        }
    }
}

fun requestPermission(context: Activity){
    ActivityCompat.requestPermissions(
        context,
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ), 0)

}

fun permissionCheck(context: Context): Boolean{
    var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    permissions.forEachIndexed { index, s ->
        if(ActivityCompat.checkSelfPermission(context, s) == PackageManager.PERMISSION_DENIED){
            return false
        }
    }
    return true
}