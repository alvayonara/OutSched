package com.alvayonara.outsched.ui.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvayonara.outsched.BuildConfig
import com.alvayonara.outsched.api.ApiRepository
import com.alvayonara.outsched.data.source.ScheduleRepository
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity
import com.alvayonara.outsched.data.source.remote.response.RequestResponse
import com.alvayonara.outsched.data.source.local.entity.item.DateItem
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleItem
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleListItem
import com.alvayonara.outsched.utils.ConvertUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SelectScheduleViewModel(private val scheduleRepository: ScheduleRepository) : ViewModel() {

    private val address = MutableLiveData<String>()
    private val latitude = MutableLiveData<String>()
    private val longitude = MutableLiveData<String>()

    fun setAddressSchedule(address: String) {
        this.address.value = address
    }

    fun setLatitudeSchedule(latitude: String) {
        this.latitude.value = latitude
    }

    fun setLongitudeSchedule(longitude: String) {
        this.longitude.value = longitude
    }

    fun getWeathers(): LiveData<List<ScheduleListItem>> =
        scheduleRepository.getWeathersData(latitude.value!!, longitude.value!!, address.value!!)
}