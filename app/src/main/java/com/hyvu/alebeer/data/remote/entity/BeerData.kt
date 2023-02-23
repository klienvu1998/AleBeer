package com.hyvu.alebeer.data.remote.entity

import com.google.gson.annotations.SerializedName

data class BeerData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("rating")
    val rating: Rating,
    @SerializedName("sale_off_time")
    val sale_off_time: Long
)