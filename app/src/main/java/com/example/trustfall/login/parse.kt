package com.example.trustfall.login

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.trustfall.BuildConfig
import com.example.trustfall.R
import com.parse.Parse
import com.parse.ParseObject

class parse : Application(){
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.applicationKey)
                .clientKey(BuildConfig.clientKey)
                .server("https://parseapi.back4app.com")
                .build()
        )
        ParseObject.registerSubclass(friendsRequestQuery::class.java);
    }
}

