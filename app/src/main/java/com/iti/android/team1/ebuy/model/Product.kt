package com.iti.android.team1.ebuy.model
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("body_html") val productDescription: String? = null,
    @SerializedName("id") val productID: Long? = null,
    @SerializedName("image") val productImage: Image? = null,
    @SerializedName("options") val productOptions: List<Option>? = null,
    @SerializedName("product_type") val productType: String? = null,
    @SerializedName("status") val productStatus: String? = null,
    @SerializedName("tags") val productTags: String? = null,
    @SerializedName("title") val productName: String? = null,
    @SerializedName("variants") val productVariants: List<Variant>? = null,
    @SerializedName("vendor") val productVendor: String? = null
)