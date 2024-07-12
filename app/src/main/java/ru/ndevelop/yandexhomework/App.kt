package ru.ndevelop.yandexhomework

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings
import ru.ndevelop.yandexhomework.di.components.AppComponent
import ru.ndevelop.yandexhomework.di.components.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        appComponent = DaggerAppComponent.factory().create(this)
    }

    companion object {
        internal lateinit var INSTANCE: App
            private set
    }
}

fun Context.appComponent() = when (this) {
    is App -> appComponent
    else -> (applicationContext as App).appComponent
}