package com.alvayonara.outsched.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alvayonara.outsched.data.source.ScheduleRepository
import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity

class DashboardViewModel(private val scheduleRepository: ScheduleRepository) : ViewModel() {

    fun getUpcomingSchedules(): LiveData<List<ScheduleEntity>> =
        scheduleRepository.getAllUpcomingSchedules()

    fun getPastSchedules(): LiveData<List<ScheduleEntity>> =
        scheduleRepository.getAllPastSchedules()
}