package com.alvayonara.outsched.core.data.source.remote.network

import com.alvayonara.outsched.core.data.source.remote.response.RequestResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{api_key}/{latitude},{longitude}?extend=hourly&lang=en&exclude=currently,minutely,daily,flags&units=si")
    fun getWeathersData(
        @Path("api_key") api_key: String?,
        @Path("latitude") latitude: String?,
        @Path("longitude") longitude: String?
    ): Flowable<RequestResponse>
}