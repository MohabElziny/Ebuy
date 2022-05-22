package com.iti.android.team1.ebuy.model

import com.google.gson.annotations.SerializedName

data class Option(
    @SerializedName("id")val optionId: Long? = null,
    @SerializedName("name") val optionName: String? = null,
    @SerializedName("product_id") val productId: Long? = null,
    @SerializedName("values") val optionValues: List<String>? = null
)