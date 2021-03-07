package com.alvayonara.outsched.core.data.source.remote

import android.annotation.SuppressLint
import com.alvayonara.outsched.BuildConfig
import com.alvayonara.outsched.core.data.source.remote.network.ApiResponse
import com.alvayonara.outsched.core.data.source.remote.network.ApiService
import com.alvayonara.outsched.core.domain.model.item.ScheduleListItem
import com.alvayonara.outsched.core.utils.DataMapper
import com.alvayonara.outsched.core.utils.GroupScheduleUtils.consolidateSchedule
import com.alvayonara.outsched.core.utils.GroupScheduleUtils.initSchedule
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    @SuppressLint("CheckResult")
    fun getWeathersData(
        latitude: String,
        longitude: String,
        address: String,
        id: Int,
        requestCode: Int
    ): Flowable<ApiResponse<List<ScheduleListItem>>> {
        val resultData = PublishSubject.create<ApiResponse<List<ScheduleListItem>>>()

        val client = apiService.getWeathersData(BuildConfig.DARK_SKY_KEY, latitude, longitude)

        client
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe({ response ->
                val dataArray = response.hourly.data
                resultData.onNext(
                    if (dataArray.isNotEmpty()) ApiResponse.Success(
                        consolidateSchedule(
                            initSchedule(
                                DataMapper.mapScheduleListResponsesToDomain(
                                    dataArray
                                ),
                                latitude,
                                longitude,
                                address,
                                id,
                                requestCode
                            )
                        )
                    ) else ApiResponse.Empty
                )
            }, { error ->
                resultData.onNext(ApiResponse.Error(error.message.toString()))
            })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }
}