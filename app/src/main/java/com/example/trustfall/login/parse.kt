package com.example.trustfall.login

import android.app.Application
import com.example.trustfall.R
import com.parse.Parse

class parse : Application(){
    override fun onCreate() {
        super.onCreate()
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(R.string.applicationId.toString())
                .clientKey(R.string.clientKey.toString())
                .server("https://parseapi.back4app.com")
                .build()
        )

    }
}