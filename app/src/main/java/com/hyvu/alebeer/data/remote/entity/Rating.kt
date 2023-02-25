package com.hyvu.alebeer.data.remote.entity

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("average")
    val average: Double,
    @SerializedName("reviews")
    val reviews: Int
)