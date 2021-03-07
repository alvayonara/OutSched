package com.alvayonara.outsched.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvayonara.outsched.core.domain.usecase.ScheduleUseCase
import com.alvayonara.outsched.di.AppScope
import com.alvayonara.outsched.ui.dashboard.DashboardViewModel
import com.alvayonara.outsched.ui.schedule.ScheduleDetailViewModel
import com.alvayonara.outsched.ui.schedule.SelectScheduleViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

@AppScope
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        return creator.get() as T
    }
}