package com.iti.android.team1.ebuy.model

import com.google.gson.annotations.SerializedName

data class Variant(
    @SerializedName("grams") val productVariantGrams: Int? = null,
    @SerializedName("id") val productVariantId: Long? = null,
    @SerializedName("inventory_item_id")  val productVariantInventoryItemId: Long? = null,
    @SerializedName("inventory_quantity")  val productVariantInventoryQuantity: Int? = null,
    @SerializedName("old_inventory_quantity") val productVariantOldInventoryQuantity: Int? = null,
    @SerializedName("option1") val productVariantOption1: String? = null,
    @SerializedName("option2") val productVariantOption2: String? = null,
    @SerializedName("price") val productVariantPrice: String? = null,
    @SerializedName("product_id") val productId: Long? = null,
    @SerializedName("requires_shipping") val productVariantRequiresShipping: Boolean? = null,
    @SerializedName("taxable") val productVariantTaxable: Boolean? = null,
    @SerializedName("weight")  val productVariantWeight: Int? = null,
    @SerializedName("weight_unit") val productVariantWeightUnit: String? = null
)