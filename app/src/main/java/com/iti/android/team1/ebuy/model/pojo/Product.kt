package com.iti.android.team1.ebuy.model.pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Products(
    @SerializedName("products")
    val products: List<Product>? = null
) : Serializable

data class Product(
    @SerializedName("body_html") val productDescription: String? = null,
    @SerializedName("id") val productID: Long? = null,
    @SerializedName("image") val productImage: ProductImage? = null,
    @SerializedName("options") val productOptions: List<Option>? = null,
    @SerializedName("product_type") val productType: String? = null,
    @SerializedName("status") val productStatus: String? = null,
    @SerializedName("tags") val productTags: String? = null,
    @SerializedName("title") val productName: String? = null,
    @SerializedName("variants") val productVariants: List<Variant>? = null,
    @SerializedName("vendor") val productVendor: String? = null
): Serializable

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
): Serializable

data class Option(
    @SerializedName("id")val optionId: Long? = null,
    @SerializedName("name") val optionName: String? = null,
    @SerializedName("product_id") val productId: Long? = null,
    @SerializedName("values") val optionValues: List<String>? = null
): Serializable

data class ProductImage(
    @SerializedName("height") val imageHeight: Int? = null,
    @SerializedName("id") val imageID: Long? = null,
    @SerializedName("product_id") val productID: Long? = null,
    @SerializedName("src") val imageURL: String? = null,
    @SerializedName("width") val imageWidth: Int? = null
): Serializable