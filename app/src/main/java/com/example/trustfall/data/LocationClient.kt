package com.example.trustfall.data

import android.location.Location
import kotlinx.coroutines.flow.Flow


interface LocationClient {
    fun getLocationUpdate(interval: Long): Flow<Location>
    class LocationException(message: String): Exception()

}