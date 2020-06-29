package com.alvayonara.outsched.data.source.local.entity.item

import com.alvayonara.outsched.data.source.local.entity.ScheduleEntity

class ScheduleItem : ScheduleListItem() {

    var scheduleEntity: ScheduleEntity? = null

    override fun getType(): Int =
        TYPE_GENERAL
}