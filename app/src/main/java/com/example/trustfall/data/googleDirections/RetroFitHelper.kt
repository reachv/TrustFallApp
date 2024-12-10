package com.example.trustfall.data.googleDirections

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitHelper {
    val baseURL = "https://maps.googleapis.com/"
    fun getInstance(): Retrofit{
        return Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build()
    }
}