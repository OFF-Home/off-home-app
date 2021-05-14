package com.offhome.app.common



import android.app.Application
import android.content.Context

class MyApp : Application() {

    override fun onCreate() {
        instanceApp = this
        super.onCreate()
    }

    companion object Factory {
        private lateinit var instanceApp: MyApp
        fun getInstance(): MyApp = instanceApp
        fun getContext(): Context = instanceApp
    }
}
