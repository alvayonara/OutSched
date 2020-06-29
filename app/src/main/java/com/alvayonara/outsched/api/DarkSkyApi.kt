package com.alvayonara.outsched.api

import com.alvayonara.outsched.data.remote.response.RequestResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DarkSkyApi {

    @GET("{api_key}/{latitude},{longitude}?extend=hourly&lang=en&exclude=currently,minutely,daily,flags&units=si")
    fun getWeathersData(
        @Path("api_key") api_key: String?,
        @Path("latitude") latitude: String?,
        @Path("longitude") longitude: String?
    ): Call<RequestResponse>
}