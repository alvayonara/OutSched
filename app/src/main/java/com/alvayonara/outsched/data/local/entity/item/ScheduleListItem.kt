package com.alvayonara.outsched.data.local.entity.item

import com.alvayonara.outsched.data.local.entity.item.ScheduleItem

abstract class ScheduleListItem {

    companion object {
        const val TYPE_DATE = 0
        const val TYPE_GENERAL = 1
    }

    abstract fun getType(): Int
}