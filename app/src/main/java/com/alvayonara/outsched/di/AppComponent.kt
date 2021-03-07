package com.alvayonara.outsched.di

import com.alvayonara.outsched.core.di.CoreComponent
import com.alvayonara.outsched.ui.dashboard.PastScheduleFragment
import com.alvayonara.outsched.ui.dashboard.UpcomingScheduleFragment
import com.alvayonara.outsched.ui.schedule.ScheduleDetailDialogFragment
import com.alvayonara.outsched.ui.schedule.SelectScheduleActivity
import dagger.Component

@AppScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [AppModule::class, ViewModelModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(fragment: PastScheduleFragment)
    fun inject(fragment: UpcomingScheduleFragment)
    fun inject(fragment: ScheduleDetailDialogFragment)
    fun inject(activity: SelectScheduleActivity)
}