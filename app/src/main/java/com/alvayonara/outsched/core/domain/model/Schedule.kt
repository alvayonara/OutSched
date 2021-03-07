package com.alvayonara.outsched.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedule(
    var id: Int = 0,
    var time: Long = 0,
    var summary: String = "",
    var icon: String = "",
    var temperature: Double = 0.0,
    var address: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var reminded: Boolean = false,
    var requestCode: Int = 0
): Parcelable
