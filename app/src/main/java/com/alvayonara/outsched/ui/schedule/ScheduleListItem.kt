package com.alvayonara.outsched.ui.schedule

import com.alvayonara.outsched.ui.schedule.item.ScheduleItem

abstract class ScheduleListItem {

    companion object {
        const val TYPE_DATE = 0
        const val TYPE_GENERAL = 1
    }

    abstract fun getType(): Int
}