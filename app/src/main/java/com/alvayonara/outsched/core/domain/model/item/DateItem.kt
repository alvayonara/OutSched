package com.alvayonara.outsched.core.domain.model.item

class DateItem : ScheduleListItem() {

    var dateList: String? = null

    override fun getType(): Int =
        TYPE_DATE
}