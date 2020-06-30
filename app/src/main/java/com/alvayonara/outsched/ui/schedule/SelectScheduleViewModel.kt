package com.alvayonara.outsched.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvayonara.outsched.data.source.ScheduleRepository
import com.alvayonara.outsched.data.source.local.entity.item.ScheduleListItem

class SelectScheduleViewModel(private val scheduleRepository: ScheduleRepository) : ViewModel() {

    private val address = MutableLiveData<String>()
    private val latitude = MutableLiveData<String>()
    private val longitude = MutableLiveData<String>()

    private val id = MutableLiveData<Int>()

    fun setAddressSchedule(address: String) {
        this.address.value = address
    }

    fun setLatitudeSchedule(latitude: String) {
        this.latitude.value = latitude
    }

    fun setLongitudeSchedule(longitude: String) {
        this.longitude.value = longitude
    }

    fun setIdSchedule(id: Int) {
        this.id.value = id
    }

    fun getWeathers(): LiveData<List<ScheduleListItem>> =
        scheduleRepository.getWeathersData(
            latitude.value!!,
            longitude.value!!,
            address.value!!,
            id.value!!
        )
}