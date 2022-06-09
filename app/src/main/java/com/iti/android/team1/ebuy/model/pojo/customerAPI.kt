package com.iti.android.team1.ebuy.model.pojo

import com.google.gson.annotations.SerializedName

data class CustomerRegisterAPI(
    val customer: Customer,
)

data class CustomerPost( val customer: CustomerRegister)

data class CustomerRegister(
    @SerializedName("email") var email: String,
    @SerializedName("first_name") var firstName: String,
    @SerializedName("last_name") var lastName: String,
    @SerializedName("tags") var password: String,
)

data class CustomerLogin(
    var email: String,
    var password: String,
)

data class CustomerLoginAPI(
    val customers: ArrayList<Customer?>,
)

data class Customer(
    @SerializedName("id") var id: Long? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("orders_count") var ordersCount: Int? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("total_spent") var totalSpent: String? = null,
    @SerializedName("last_order_id") var lastOrderId: String? = null,
    @SerializedName("note") var note: String? = null,
    @SerializedName("verified_email") var verifiedEmail: Boolean? = null,
    @SerializedName("tax_exempt") var taxExempt: Boolean? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("tags") var password: String? = null,
    @SerializedName("last_order_name") var lastOrderName: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("addresses") var addresses: ArrayList<Address> = arrayListOf(),
    @SerializedName("accepts_marketing_updated_at") var acceptsMarketingUpdatedAt: String? = null,
    @SerializedName("marketing_opt_in_level") var marketingOptInLevel: String? = null,
    @SerializedName("tax_exemptions") var taxExemptions: ArrayList<String> = arrayListOf(),
    @SerializedName("sms_marketing_consent") var smsMarketingConsent: SmsMarketingConsent? = SmsMarketingConsent(),
    @SerializedName("default_address") var defaultAddress: DefaultAddress? = DefaultAddress(),

    )

data class Addresses (@SerializedName("addresses") var addresses: ArrayList<Address> = arrayListOf())



data class DefaultAddress(
    @SerializedName("id") var id: Long? = null,
    @SerializedName("customer_id") var customerId: Long? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("company") var company: String? = null,
    @SerializedName("address1") var address1: String? = null,
    @SerializedName("address2") var address2: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("province") var province: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("zip") var zip: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("province_code") var provinceCode: String? = null,
    @SerializedName("country_code") var countryCode: String? = null,
    @SerializedName("country_name") var countryName: String? = null,
    @SerializedName("default") var default: Boolean? = null,
)

data class SmsMarketingConsent(
    @SerializedName("state") var state: String? = null,
    @SerializedName("opt_in_level") var optInLevel: String? = null,
    @SerializedName("consent_updated_at") var consentUpdatedAt: String? = null,
    @SerializedName("consent_collected_from") var consentCollectedFrom: String? = null,
)