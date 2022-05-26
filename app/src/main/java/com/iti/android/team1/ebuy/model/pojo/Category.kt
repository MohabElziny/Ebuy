package com.iti.android.team1.ebuy.model.pojo

import com.google.gson.annotations.SerializedName

data class Category(@SerializedName("id") val categoryId : Long,
                    @SerializedName("title") val categoryTitle:String)

data class Categories(
    @SerializedName("custom_collections")
    val list: List<Category>)
