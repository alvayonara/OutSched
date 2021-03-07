package com.alvayonara.outsched.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvayonara.outsched.core.ui.ViewModelFactory
import com.alvayonara.outsched.ui.dashboard.DashboardViewModel
import com.alvayonara.outsched.ui.schedule.ScheduleDetailViewModel
import com.alvayonara.outsched.ui.schedule.SelectScheduleViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectScheduleViewModel::class)
    abstract fun bindSelectScheduleViewModel(viewModel: SelectScheduleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScheduleDetailViewModel::class)
    abstract fun bindScheduleDetailViewModel(viewModel: ScheduleDetailViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}