package com.hyvu.alebeer

import android.app.Application

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Injection.setAppContext(applicationContext)
    }

}