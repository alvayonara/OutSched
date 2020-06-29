package com.alvayonara.outsched.data.local.entity.item

class DateItem : ScheduleListItem() {

    var dateList: String? = null

    override fun getType(): Int =
        TYPE_DATE
}