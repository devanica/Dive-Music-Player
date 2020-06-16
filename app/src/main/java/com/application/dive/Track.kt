package com.application.dive

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(val id: Long,
                 val trackName: String,
                 val artistName: String,
                 val trackDuration: String) : Parcelable {
}