package com.example.trustfall

import android.app.Activity
import android.app.Activity.BIND_AUTO_CREATE
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.trustfall.data.LocationService
import com.example.trustfall.data.googleDirections.DirectionsRetroFitCall
import com.example.trustfall.data.googleDirections.RetroFitHelper
import com.example.trustfall.data.googleDirections.Route
import com.example.trustfall.data.googleDirections.directionsJsonReturn
import com.example.trustfall.screens.fontfamily
import com.example.trustfall.screens.updateUserLocation
import com.example.trustfall.ui.theme.TrustFallTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.parse.LogInCallback
import com.parse.ParseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ActiveTrip : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var bundle = intent.extras
        var dest = bundle!!.getString("dest").toString()
        var origin = bundle!!.getString("origin").toString()
        setContent {
            TrustFallTheme {
                var result = remember { mutableStateListOf<LatLng>() }
                val directionsCall = RetroFitHelper.getInstance().create(DirectionsRetroFitCall::class.java)
                val directions: Call<directionsJsonReturn>? = directionsCall.getDirection(origin=origin, destination = dest, key = BuildConfig.googleMapsApi)
                directions?.enqueue(object: Callback<directionsJsonReturn> {
                    override fun onResponse(
                        call: Call<directionsJsonReturn>,
                        response: Response<directionsJsonReturn>
                    ) {
                        if(response.isSuccessful){
                            val res : List<Route>? = response.body()?.routes
                            val str : String = res?.get(0)?.overview_polyline?.points.toString()
                            decodePoly(str).forEach {
                                result.add(it)
                            }
                        }
                    }

                    override fun onFailure(call: Call<directionsJsonReturn>, t: Throwable) {
                        Log.e("HomePage Retrofit", "FailureCall: $t")
                    }
                })
                val LocationIntent = Intent(applicationContext, LocationService::class.java).apply { action = LocationService.ACTION_START }
                applicationContext.startService(LocationIntent)
                ActivetTripDisplay(intent, applicationContext, result)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(applicationContext, LocationService::class.java).apply { action = LocationService.ACTION_STOP }
        applicationContext.startService(intent)
    }
}
@Composable
private fun ActivetTripDisplay(
    intent: Intent,
    applicationContext: Context,
    result: List<LatLng>
) {
    val context = LocalContext.current as? Activity
    var route = ArrayList<LatLng>()
    route.addAll(result)
    Log.e("response", route.toString())
    var isCurrentLocation by remember { mutableStateOf(false) }
    lateinit var binder : LocationService.LocationBinder
    var userPosition by remember { mutableStateOf(LatLng(0.toDouble(), 0.toDouble())) }
    var cameraPosition = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(userPosition, 15f) }
    var cameraPositionState by remember { mutableStateOf(cameraPosition) }
    var alertLevel by remember { mutableStateOf(0) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
        alertLevel = alertCheck(userPosition, route)
        updateUserLocationActive(userPosition, alertLevel, route)
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
    applicationContext.bindService(
        Intent(applicationContext, LocationService::class.java),
        connection,
        BIND_AUTO_CREATE
    )
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        GoogleMap(
            modifier = Modifier.height(700.dp),
            cameraPositionState = cameraPositionState
        ) {
            Marker(state = MarkerState(userPosition))
            Polyline(points = route, color = Color.Red)

        }
        Card(
            modifier = Modifier.wrapContentSize().padding(10.dp)
        ) {
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
                    .padding(5.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.White),
                textStyle = TextStyle(fontFamily = fontfamily("Oswald"), color = Color.Black)
            )
            Button(
                modifier = Modifier.padding(bottom = 5.dp).align(Alignment.CenterHorizontally),
                onClick = {
                    ParseUser.logInInBackground(ParseUser.getCurrentUser().username, password, LogInCallback { user, e ->
                        if(e != null){
                            Toast.makeText(applicationContext, "Wrong password, please try again.", Toast.LENGTH_SHORT).show()
                            return@LogInCallback
                        }
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        context?.startActivity(intent)
                        context?.finish()
                    })
                }
            ) {
                Text("End Trip")
            }
        }
    }
}


fun alertCheck(curr : LatLng, area: List<LatLng>): Int{
    var result = 0
    if(distanceTo(curr.latitude, curr.longitude, area[0].latitude, area[0].longitude) >= 1){
        result = 1
    }
    if(distanceTo(curr.latitude, curr.longitude, area[0].latitude, area[0].longitude) >= 5){
        result = 2
    }
    return result
}
fun updateUserLocationActive(userPosition: LatLng, alertLevel: Int = 0, area: List<LatLng>) {
    val user = ParseUser.getCurrentUser()
    user.put("currLat", userPosition.latitude)
    user.put("currLong", userPosition.longitude)
    user.put("alertLevel", alertLevel)
    user.save()
}

fun distanceTo(lat1: Double, long1: Double, lat2: Double, long2: Double): Float {
    val earthRadius = 3958.75
    val dLat = Math.toRadians(lat2 - lat1)
    val dLng = Math.toRadians(long2 - long1)
    val a = sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(
        Math.toRadians(lat2)
    ) * sin(dLng / 2) * sin(dLng / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val dist = earthRadius * c

    return dist.toFloat()
}

private fun decodePoly(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val p = LatLng(lat.toDouble() / 1E5,
            lng.toDouble() / 1E5)
        poly.add(p)
    }
    return poly
}