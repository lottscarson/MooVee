package com.scottlarson.mooveeapp.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Represents a movie retrieved from the Movie Database API
 */
@Keep
@Parcelize
class Movie (
        val id: String,
        val title: String,
        @SerializedName("release_date")
        val releaseDate: Date,
        val overview: String,
        @SerializedName("vote_average")
        val voteAverage: Float
) : Parcelable
