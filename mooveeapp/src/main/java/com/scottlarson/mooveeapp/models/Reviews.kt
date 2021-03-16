package com.scottlarson.mooveeapp.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Reviews (
    @SerializedName("results")
    val reviews: List<Review>
)