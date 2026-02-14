package com.ssemaj.imdbapp

import android.app.Application
import com.ssemaj.imdbapp.di.AppComponent
import com.ssemaj.imdbapp.di.DaggerAppComponent

class ImdbApplication : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}
