package by.geekbrains.pictureseveryday

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        lateinit var appInstance: App
            private set
    }
}