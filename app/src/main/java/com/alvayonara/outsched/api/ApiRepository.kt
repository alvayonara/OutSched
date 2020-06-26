package com.alvayonara.outsched.api

import android.app.Application
import com.alvayonara.outsched.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRepository : Application() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_DARK_SKY_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val darkSky: DarkSkyApi = retrofit.create(DarkSkyApi::class.java)
}