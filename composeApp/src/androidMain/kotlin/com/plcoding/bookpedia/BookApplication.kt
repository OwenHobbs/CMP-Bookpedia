package com.plcoding.bookpedia

import android.app.Application
import com.plcoding.bookpedia.di.initKoin
import org.koin.android.ext.koin.androidContext

// Registered in AndroidManifest.xml
class BookApplication: Application() { // TODO: what is Application?
    override fun onCreate() {
        super.onCreate()
        initKoin {
            // Add dependencies used just on Android here

            // And context TODO: why?
            androidContext(this@BookApplication)
        }
    }
}