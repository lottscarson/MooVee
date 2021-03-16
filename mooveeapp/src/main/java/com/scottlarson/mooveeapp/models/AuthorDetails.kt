package com.scottlarson.mooveeapp.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
@Parcelize
class AuthorDetails (
    val rating: Int
) : Parcelable