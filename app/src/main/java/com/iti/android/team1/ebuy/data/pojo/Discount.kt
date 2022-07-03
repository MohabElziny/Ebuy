package com.iti.android.team1.ebuy.data.pojo

import com.google.gson.annotations.SerializedName


data class PriceRulePost(val price_rule: PriceRuleCreate)

data class PriceRuleCreate(
    @SerializedName("title") var title: String? = null,
    @SerializedName("value_type") var valueType: String? = null,
    @SerializedName("value") var value: String? = null,
    @SerializedName("customer_selection") var customerSelection: String? = null,
    @SerializedName("target_type") var targetType: String? = null,
    @SerializedName("target_selection") var targetSelection: String? = null,
    @SerializedName("allocation_method") var allocationMethod: String? = null,
    @SerializedName("once_per_customer") var oncePerCustomer: Boolean? = null,
    @SerializedName("usage_limit") var usageLimit: Int? = null,
    @SerializedName("starts_at") var startsAt: String? = null,

    )

data class PriceRuleResponse(

    @SerializedName("price_rules") var priceRules: ArrayList<PriceRules> = arrayListOf(),

    )

data class PriceRules(

    @SerializedName("id") var id: Long? = null,
    @SerializedName("value_type") var valueType: String? = null,
    @SerializedName("value") var value: String? = null,
    @SerializedName("customer_selection") var customerSelection: String? = null,
    @SerializedName("target_type") var targetType: String? = null,
    @SerializedName("target_selection") var targetSelection: String? = null,
    @SerializedName("allocation_method") var allocationMethod: String? = null,
    @SerializedName("allocation_limit") var allocationLimit: String? = null,
    @SerializedName("once_per_customer") var oncePerCustomer: Boolean? = null,
    @SerializedName("usage_limit") var usageLimit: String? = null,
    @SerializedName("starts_at") var startsAt: String? = null,
    @SerializedName("ends_at") var endsAt: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("title") var title: String? = null,

    )

data class PriceRule(@SerializedName("price_rule") val priceRule: PriceRules? = null)

data class Discount(
    @SerializedName("discount_codes") var discountCodes: ArrayList<DiscountCodes> = arrayListOf(),
)

data class DiscountCodes(
    @SerializedName("id") var id: Long? = null,
    @SerializedName("price_rule_id") var priceRuleId: Long? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("usage_count") var usageCount: Int? = null,
)

data class DiscountCode(@SerializedName("discount_code") val discountCodes: DiscountCodes? = null)

data class DiscountPost(
    val discount_code: DiscountCreate,
)

data class DiscountCreate(
    @SerializedName("price_rule_id") var priceRuleId: Long? = null,
)