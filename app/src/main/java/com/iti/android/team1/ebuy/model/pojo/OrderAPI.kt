package com.iti.android.team1.ebuy.model.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class OrderAPI {
    @SerializedName("orders")
    var orders: ArrayList<Order> = arrayListOf()
}

data class OrderPost(
    var order: Order,
)

@Parcelize
data class Money(

    @SerializedName("amount") var amount: String? = null,
    @SerializedName("currency_code") var currencyCode: String? = null,

    ) : Parcelable

@Parcelize
data class PriceSet(

    @SerializedName("shop_money") var shopMoney: Money? = Money(),
    @SerializedName("presentment_money") var presentmentMoney: Money? = Money(),

    ) : Parcelable

@Parcelize
data class BillingAddress(

    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("address1") var address1: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("zip") var zip: String? = null,
    @SerializedName("province") var province: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("address2") var address2: String? = null,
    @SerializedName("company") var company: String? = null,
    @SerializedName("latitude") var latitude: String? = null,
    @SerializedName("longitude") var longitude: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("country_code") var countryCode: String? = null,
    @SerializedName("province_code") var provinceCode: String? = null,

    ) : Parcelable

data class EmailMarketingConsent(

    @SerializedName("state") var state: String? = null,
    @SerializedName("opt_in_level") var optInLevel: String? = null,
    @SerializedName("consent_updated_at") var consentUpdatedAt: String? = null,

    )

@Parcelize
data class LineItems(

    @SerializedName("id") var id: Long? = null,
    @SerializedName("admin_graphql_api_id") var adminGraphqlApiId: String? = null,
    @SerializedName("fulfillable_quantity") var fulfillableQuantity: Int? = null,
    @SerializedName("fulfillment_service") var fulfillmentService: String? = null,
    @SerializedName("fulfillment_status") var fulfillmentStatus: String? = null,
    @SerializedName("gift_card") var giftCard: Boolean? = null,
    @SerializedName("grams") var grams: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("price_set") var priceSet: Money? = Money(),
    @SerializedName("product_exists") var productExists: Boolean? = null,
    @SerializedName("product_id") var productId: Long? = null,
    @SerializedName("properties") var properties: ArrayList<String> = arrayListOf(),
    @SerializedName("quantity") var quantity: Int? = null,
    @SerializedName("requires_shipping") var requiresShipping: Boolean? = null,
    @SerializedName("sku") var sku: String? = null,
    @SerializedName("taxable") var taxable: Boolean? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("total_discount") var totalDiscount: String? = null,
    @SerializedName("total_discount_set") var totalDiscountSet: PriceSet? = PriceSet(),
    @SerializedName("variant_id") var variantId: Long? = null,
    @SerializedName("variant_inventory_management") var variantInventoryManagement: String? = null,
    @SerializedName("variant_title") var variantTitle: String? = null,
    @SerializedName("vendor") var vendor: String? = null,
    @SerializedName("tax_lines") var taxLines: ArrayList<String> = arrayListOf(),
    @SerializedName("duties") var duties: ArrayList<String> = arrayListOf(),
    @SerializedName("discount_allocations") var discountAllocations: ArrayList<String> = arrayListOf(),

    ) : Parcelable

@Parcelize
data class ShippingAddress(

    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("address1") var address1: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("zip") var zip: String? = null,
    @SerializedName("province") var province: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("address2") var address2: String? = null,
    @SerializedName("company") var company: String? = null,
    @SerializedName("latitude") var latitude: String? = null,
    @SerializedName("longitude") var longitude: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("country_code") var countryCode: String? = null,
    @SerializedName("province_code") var provinceCode: String? = null,

    ) : Parcelable

@Parcelize
data class Order(

    @SerializedName("id") var id: Long? = null,
    @SerializedName("admin_graphql_api_id") var adminGraphqlApiId: String? = null,
    @SerializedName("app_id") var appId: Long? = null,
    @SerializedName("browser_ip") var browserIp: String? = null,
    @SerializedName("buyer_accepts_marketing") var buyerAcceptsMarketing: Boolean? = null,
    @SerializedName("cancel_reason") var cancelReason: String? = null,
    @SerializedName("cancelled_at") var cancelledAt: String? = null,
    @SerializedName("cart_token") var cartToken: String? = null,
    @SerializedName("checkout_id") var checkoutId: String? = null,
    @SerializedName("checkout_token") var checkoutToken: String? = null,
    @SerializedName("closed_at") var closedAt: String? = null,
    @SerializedName("confirmed") var confirmed: Boolean? = null,
    @SerializedName("contact_email") var contactEmail: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("current_subtotal_price") var currentSubtotalPrice: String? = null,
    @SerializedName("current_subtotal_price_set") var currentSubtotalPriceSet: PriceSet? = PriceSet(),
    @SerializedName("current_total_discounts") var currentTotalDiscounts: String? = null,
    @SerializedName("current_total_discounts_set") var currentTotalDiscountsSet: PriceSet? = PriceSet(),
    @SerializedName("current_total_duties_set") var currentTotalDutiesSet: String? = null,
    @SerializedName("current_total_price") var currentTotalPrice: String? = null,
    @SerializedName("current_total_price_set") var currentTotalPriceSet: PriceSet? = PriceSet(),
    @SerializedName("current_total_tax") var currentTotalTax: String? = null,
    @SerializedName("current_total_tax_set") var currentTotalTaxSet: PriceSet? = PriceSet(),
    @SerializedName("customer_locale") var customerLocale: String? = null,
    @SerializedName("device_id") var deviceId: String? = null,
    @SerializedName("discount_codes") var discountCodes: ArrayList<String> = arrayListOf(),
    @SerializedName("email") var email: String? = null,
    @SerializedName("estimated_taxes") var estimatedTaxes: Boolean? = null,
    @SerializedName("financial_status") var financialStatus: String? = null,
    @SerializedName("fulfillment_status") var fulfillmentStatus: String? = null,
    @SerializedName("gateway") var gateway: String? = null,
    @SerializedName("landing_site") var landingSite: String? = null,
    @SerializedName("landing_site_ref") var landingSiteRef: String? = null,
    @SerializedName("location_id") var locationId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("note") var orderStatus: String? = null,
    @SerializedName("note_attributes") var noteAttributes: ArrayList<String> = arrayListOf(),
    @SerializedName("number") var number: Int? = null,
    @SerializedName("order_number") var orderNumber: Int? = null,
    @SerializedName("order_status_url") var orderStatusUrl: String? = null,
    @SerializedName("original_total_duties_set") var originalTotalDutiesSet: String? = null,
    @SerializedName("payment_gateway_names") var paymentGatewayNames: ArrayList<String> = arrayListOf(),
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("presentment_currency") var presentmentCurrency: String? = null,
    @SerializedName("processed_at") var processedAt: String? = null,
    @SerializedName("processing_method") var processingMethod: String? = null,
    @SerializedName("reference") var reference: String? = null,
    @SerializedName("referring_site") var referringSite: String? = null,
    @SerializedName("source_identifier") var sourceIdentifier: String? = null,
    @SerializedName("source_name") var sourceName: String? = null,
    @SerializedName("source_url") var sourceUrl: String? = null,
    @SerializedName("subtotal_price") var subtotalPrice: String? = null,
    @SerializedName("subtotal_price_set") var subtotalPriceSet: PriceSet? = PriceSet(),
    @SerializedName("tags") var tags: String? = null,
    @SerializedName("tax_lines") var taxLines: ArrayList<String> = arrayListOf(),
    @SerializedName("taxes_included") var taxesIncluded: Boolean? = null,
    @SerializedName("test") var test: Boolean? = null,
    @SerializedName("token") var token: String? = null,
    @SerializedName("total_discounts") var totalDiscounts: String? = null,
    @SerializedName("total_discounts_set") var totalDiscountsSet: PriceSet? = PriceSet(),
    @SerializedName("total_line_items_price") var totalLineItemsPrice: String? = null,
    @SerializedName("total_line_items_price_set") var totalLineItemsPriceSet: PriceSet? = PriceSet(),
    @SerializedName("total_outstanding") var totalOutstanding: String? = null,
    @SerializedName("total_price") var totalPrice: String? = null,
    @SerializedName("total_price_set") var totalPriceSet: PriceSet? = PriceSet(),
    @SerializedName("total_price_usd") var totalPriceUsd: String? = null,
    @SerializedName("total_shipping_price_set") var totalShippingPriceSet: PriceSet? = PriceSet(),
    @SerializedName("total_tax") var totalTax: String? = null,
    @SerializedName("total_tax_set") var totalTaxSet: PriceSet? = PriceSet(),
    @SerializedName("total_tip_received") var totalTipReceived: String? = null,
    @SerializedName("total_weight") var totalWeight: Int? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("billing_address") var billingAddress: BillingAddress? = BillingAddress(),
    @SerializedName("customer") var customer: Customer? = Customer(),
    @SerializedName("discount_applications") var discountApplications: ArrayList<String> = arrayListOf(),
    @SerializedName("fulfillments") var fulfillments: ArrayList<String> = arrayListOf(),
    @SerializedName("line_items") var lineItems: ArrayList<LineItems> = arrayListOf(),
    @SerializedName("payment_terms") var paymentTerms: String? = null,
    @SerializedName("refunds") var refunds: ArrayList<String> = arrayListOf(),
    @SerializedName("shipping_address") var shippingAddress: ShippingAddress? = ShippingAddress(),
    @SerializedName("shipping_lines") var shippingLines: ArrayList<String> = arrayListOf(),

    ) : Parcelable
