package com.example.trustfall.login

import android.app.Application
import com.example.trustfall.BuildConfig
import com.example.trustfall.R
import com.parse.Parse

class parse : Application(){
    override fun onCreate() {
        super.onCreate()

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.applicationKey)
                .clientKey(BuildConfig.clientKey)
                .server("https://parseapi.back4app.com")
                .build()
        )
    }
}

