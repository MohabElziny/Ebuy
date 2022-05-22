package com.iti.android.team1.ebuy.model

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("height") val imageHeight: Int? = null,
    @SerializedName("id") val imageID: Long? = null,
    @SerializedName("product_id") val productID: Long? = null,
    @SerializedName("src") val imageURL: String? = null,
    @SerializedName("width") val imageWidth: Int? = null
)