package com.iti.android.team1.ebuy.data.pojo

import com.google.gson.annotations.SerializedName

data class Brands(
    @SerializedName("smart_collections")
    val brands: List<Brand>
)

data class Image(
    @SerializedName("src")
    val imageUrl: String
)

data class Brand(
    @SerializedName("id")
    val brandID: Long,
    @SerializedName("image")
    val brandImage: Image,
    @SerializedName("title")
    val brandTitle: String,
)