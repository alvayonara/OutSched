package com.alvayonara.outsched.core.domain.model.item

abstract class ScheduleListItem {

    companion object {
        const val TYPE_DATE = 0
        const val TYPE_GENERAL = 1
    }

    abstract fun getType(): Int
}