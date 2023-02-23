package com.hyvu.alebeer.data.remote.entity

import com.google.gson.annotations.SerializedName

data class BeerResponse(
    @SerializedName("data")
    val data: List<BeerData>,
    @SerializedName("loadMore")
    val loadMore: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("total")
    val total: Int
)