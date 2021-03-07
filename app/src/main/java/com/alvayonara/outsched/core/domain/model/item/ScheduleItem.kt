package com.alvayonara.outsched.core.domain.model.item

import com.alvayonara.outsched.core.domain.model.Schedule

class ScheduleItem : ScheduleListItem() {

    var schedule: Schedule? = null

    override fun getType(): Int =
        TYPE_GENERAL
}