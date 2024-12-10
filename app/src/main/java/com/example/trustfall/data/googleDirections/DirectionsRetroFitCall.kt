package com.example.trustfall.data.googleDirections

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsRetroFitCall {

    @GET("/maps/api/directions/json")
    fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Call<directionsJsonReturn>
}