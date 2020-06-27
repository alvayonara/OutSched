package com.alvayonara.outsched.ui.schedule.item

import com.alvayonara.outsched.data.local.entity.ScheduleEntity
import com.alvayonara.outsched.ui.schedule.ScheduleListItem

class ScheduleItem : ScheduleListItem() {

    var scheduleEntity: ScheduleEntity? = null

    override fun getType(): Int = TYPE_GENERAL
}