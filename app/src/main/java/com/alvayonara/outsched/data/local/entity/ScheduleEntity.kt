package com.alvayonara.outsched.data.local.entity

import android.os.Parcelable
import com.alvayonara.outsched.data.remote.entity.RequestResponse
import com.alvayonara.outsched.utils.ConvertUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScheduleEntity(
    @SerializedName("time") var time: Long? = 0,
    @SerializedName("summary") var summary: String? = "",
    @SerializedName("icon") var icon: String? = "",
    @SerializedName("temperature") var temperature: Double? = null,
    var address: String? = "",
    var latitude: String? = "",
    var longitude: String? = ""
) : Parcelable