package com.alvayonara.outsched.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "time")
    @SerializedName("time")
    var time: Long? = 0,

    @ColumnInfo(name = "summary")
    @SerializedName("summary")
    var summary: String? = null,

    @ColumnInfo(name = "icon")
    @SerializedName("icon")
    var icon: String? = null,

    @ColumnInfo(name = "temperature")
    @SerializedName("temperature")
    var temperature: Double? = null,

    @ColumnInfo(name = "address")
    var address: String? = null,

    @ColumnInfo(name = "latitude")
    var latitude: String? = null,

    @ColumnInfo(name = "longitude")
    var longitude: String? = null,

    @ColumnInfo(name = "reminded")
    var reminded: Boolean? = false,

    @ColumnInfo(name = "request_code")
    var requestCode: Int = 0
) : Parcelable