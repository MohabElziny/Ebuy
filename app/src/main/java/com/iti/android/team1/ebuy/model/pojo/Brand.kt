package com.iti.android.team1.ebuy.model.pojo

import com.google.gson.annotations.SerializedName

data class Brand(
    @SerializedName("smart_collections")
    val smartCollections: List<SmartCollection>
)

data class Image(
    @SerializedName("src")
    val src: String
)

data class SmartCollection(
    @SerializedName("id")
    val id: Long,
    @SerializedName("image")
    val image: Image,
    @SerializedName("title")
    val title: String,
)