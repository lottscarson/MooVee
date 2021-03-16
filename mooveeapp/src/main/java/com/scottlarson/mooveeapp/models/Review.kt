package com.scottlarson.mooveeapp.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Represents a movie review retrieved from the Movie Database API
 */
@Keep
@Parcelize
class Review (
        val author: String,
        @SerializedName("author_details")
        val authorDetails: AuthorDetails,
        val reviewDate: Date,
        val content: String
) : Parcelable
