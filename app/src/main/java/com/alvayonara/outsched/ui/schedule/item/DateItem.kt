package com.alvayonara.outsched.ui.schedule.item

import com.alvayonara.outsched.ui.schedule.ScheduleListItem

class DateItem : ScheduleListItem() {

    var dateList: String? = null

    override fun getType(): Int = TYPE_DATE
}