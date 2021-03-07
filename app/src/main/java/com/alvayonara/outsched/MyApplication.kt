package com.alvayonara.outsched

import android.app.Application
import com.alvayonara.outsched.core.di.CoreComponent
import com.alvayonara.outsched.core.di.DaggerCoreComponent
import com.alvayonara.outsched.di.AppComponent
import com.alvayonara.outsched.di.DaggerAppComponent

open class MyApplication : Application() {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(applicationContext)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }
}