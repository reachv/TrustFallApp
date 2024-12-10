package com.example.trustfall.screens

import android.Manifest
import android.R
import android.app.Activity
import android.app.Activity.BIND_AUTO_CREATE
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trustfall.ActiveTrip
import com.example.trustfall.BuildConfig
import com.example.trustfall.data.LocationService
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.parse.ParseUser


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

    var StartTripButton by remember { mutableStateOf(false) }
    var inverseButtons by rememberSaveable{ mutableStateOf(false) }
    var isCurrentLocation by remember { mutableStateOf(false) }

    val context = LocalContext.current as Activity
    lateinit var binder : LocationService.LocationBinder

    var userPosition by remember { mutableStateOf(LatLng(0.toDouble(), 0.toDouble())) }
    var cameraPosition = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(userPosition, 15f) }
    var cameraPositionState by remember { mutableStateOf(cameraPosition) }
    var destination by remember { mutableStateOf("") }

    if(!Places.isInitialized() && BuildConfig.googleMapsApi != null){
        Places.initialize(context, BuildConfig.googleMapsApi)
    }

    val LocationHandler = Handler(Looper.getMainLooper()){
        userPosition = LatLng(it.data.getDouble("lat"), it.data.getDouble("long"))
        if(!isCurrentLocation){
            cameraPositionState = CameraPositionState(
                position = CameraPosition.fromLatLngZoom(
                    userPosition, 10f
                )
            )
            isCurrentLocation = true
        }
        updateUserLocation(userPosition)
        Log.e("testings", userPosition.toString())
        true
    }
    val connection = object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            binder = p1 as LocationService.LocationBinder
            binder.setHandler(LocationHandler)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }
    }

    context.bindService(
        Intent(context, LocationService::class.java),
        connection,
        BIND_AUTO_CREATE
        )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            onPlaceSelected = {place ->
                destination = place
                StartTripButton = true
            }
        )
        GoogleMap(
            modifier = Modifier.height(600.dp),
            cameraPositionState = cameraPositionState
        ){
            Marker(
                state = MarkerState(userPosition)
            )
        }
        Row {
            Button(
                onClick = {
                    Intent(context, LocationService::class.java).apply {
                        action = LocationService.ACTION_START
                        context.startService(this)}
                    inverseButtons = true
                },
                enabled = !inverseButtons
            ) {
                Text("Start Tracking")
            }
            Button(
                onClick = {
                    val intent = Intent(context, LocationService::class.java).apply {
                        action = LocationService.ACTION_STOP}
                    context.startService(intent)
                    inverseButtons = false},
                enabled = inverseButtons
            ) {
                Text("Stop Tracking")
            }
            Button(
                onClick = {
                    val stopTrackingIntent = Intent(context, LocationService::class.java).apply {
                        action = LocationService.ACTION_STOP}
                    context.startService(stopTrackingIntent)

                    var intent = Intent(context, ActiveTrip::class.java)
                    intent.putExtra("dest", destination)
                    intent.putExtra("origin", "${userPosition.latitude}, ${userPosition.longitude}")
                    context.startActivity(intent)
                },
                enabled = StartTripButton
            ) {
                Text("Start Trip")
            }
        }
    }
}

fun updateUserLocation(userPosition: LatLng) {
    val user = ParseUser.getCurrentUser()
    user.put("currLat", userPosition.latitude)
    user.put("currLong", userPosition.longitude)
    user.save()
}

fun requestPermission(context: Activity){
    ActivityCompat.requestPermissions(
        context,
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.FOREGROUND_SERVICE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
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

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onPlaceSelected: (String) -> Unit
) {

    // Determine the appropriate text color based on the current theme
    val textColor =  Color.BLACK
    // Use AndroidView to integrate an Android View (AutoCompleteTextView) into Jetpack Compose
    AndroidView(
        factory = { context ->
            AutoCompleteTextView(context).apply {
                hint = "Search for a place" // Set the hint text for the search bar

                // Set the text color and hint text color based on the current theme
                setTextColor(textColor)

                // Set the layout params to ensure the view takes up the full width
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                // Initialize the Places API and the Autocomplete Adapter
                val autocompleteAdapter = ArrayAdapter<String>(context, R.layout.simple_dropdown_item_1line)
                val placesClient = Places.createClient(context)
                val autocompleteSessionToken = AutocompleteSessionToken.newInstance()

                // Add a text change listener to capture and handle the user's input
                addTextChangedListener { editable ->
                    val query = editable?.toString() ?: "" // Get the user's input as a query string
                    if (query.isNotEmpty()) {
                        // Build a request to fetch autocomplete predictions based on the user's input
                        val request = FindAutocompletePredictionsRequest.builder()
                            .setSessionToken(autocompleteSessionToken)
                            .setQuery(query)
                            .build()

                        // Fetch autocomplete predictions from the Places API
                        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                            autocompleteAdapter.clear() // Clear the previous suggestions
                            response.autocompletePredictions.forEach { prediction ->
                                // Add each prediction to the adapter for displaying in the dropdown
                                autocompleteAdapter.add(prediction.getFullText(null).toString())
                            }
                            autocompleteAdapter.notifyDataSetChanged() // Notify the adapter to update the dropdown
                        }
                    }
                }

                // Set the adapter to the AutoCompleteTextView to display suggestions
                setAdapter(autocompleteAdapter)

                // Set an item click listener to handle when the user selects a suggestion
                setOnItemClickListener { _, _, position, _ ->
                    val selectedPlace = autocompleteAdapter.getItem(position) ?: return@setOnItemClickListener

                    // Call the callback function to handle the selected place
                    onPlaceSelected(selectedPlace)
                }
            }
        },
        modifier = modifier // Apply the passed modifier to the AutoCompleteTextView
            .fillMaxWidth() // Make sure the composable fills the maximum width available
            .padding(16.dp) //  Add padding to the search bar
    )
}



