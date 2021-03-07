package com.alvayonara.outsched.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "time") var time: Long = 0,
    @ColumnInfo(name = "summary") var summary: String = "",
    @ColumnInfo(name = "icon") var icon: String = "",
    @ColumnInfo(name = "temperature") var temperature: Double = 0.0,
    @ColumnInfo(name = "address") var address: String = "",
    @ColumnInfo(name = "latitude") var latitude: String = "",
    @ColumnInfo(name = "longitude") var longitude: String = "",
    @ColumnInfo(name = "reminded") var reminded: Boolean = false,
    @ColumnInfo(name = "requestCode") var requestCode: Int = 0
)