package com.alvayonara.outsched.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvayonara.outsched.data.source.ScheduleRepository
import com.alvayonara.outsched.di.Injection
import com.alvayonara.outsched.ui.dashboard.DashboardViewModel
import com.alvayonara.outsched.ui.schedule.ScheduleDetailViewModel
import com.alvayonara.outsched.ui.schedule.SelectScheduleViewModel

class ViewModelFactory private constructor(private val scheduleRepository: ScheduleRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SelectScheduleViewModel::class.java) -> {
                SelectScheduleViewModel(scheduleRepository) as T
            }
            modelClass.isAssignableFrom(ScheduleDetailViewModel::class.java) -> {
                ScheduleDetailViewModel(scheduleRepository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(scheduleRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}