package com.scottlarson.mooveeapp.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.util.*

@Keep
class Movies (
    @SerializedName("results")
    val movies: List<Movie>
)