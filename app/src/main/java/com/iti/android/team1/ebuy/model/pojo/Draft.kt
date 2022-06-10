package com.iti.android.team1.ebuy.model.pojo

import com.google.gson.annotations.SerializedName

data class Draft(
    @SerializedName("draft_order") var draftOrder: DraftOrder = DraftOrder(),
)

data class DraftOrder(
    var id: Long = 0,
    @SerializedName("line_items") var lineItems: ArrayList<DraftsLineItems> = arrayListOf(),
    @SerializedName("total_price") var totalPrice: String = "",
    @SerializedName("subtotal_price") var subtotalPrice: String = "",
    var customer: DraftCustomerID = DraftCustomerID(),
    @SerializedName("total_tax") var totalTax: String = "",
)

data class DraftsLineItems(
    var price: String = "",
    @SerializedName("product_id") var productId: Long = 0,
    var quantity: Int = 0,
    var title: String = "",
    @SerializedName("variant_id") var variantId: Long = 0,
    @SerializedName("variant_title") var variantTitle: String = "",
    var vendor: String = "",
)

data class DraftCustomerID(
    var id: Long = 0,
)
