package com.iti.android.team1.ebuy.data.pojo

object ConvertAddToShoppingAdd {
    fun convertToShipping(
        add: Address,
    ) = ShippingAddress().apply {
        firstName = add.firstName
        address1 = add.address1
        address2 = add.address2
        phone = add.phone
        city = add.city
        zip = add.zip
        province = add.province
        country = add.country
        lastName = add.lastName
        company = add.company
        name = add.name
        countryCode = add.countryCode
        provinceCode = add.provinceCode
    }
    fun convertToBilling(
        add: Address,
    ) = BillingAddress().apply {
        firstName = add.firstName
        address1 = add.address1
        address2 = add.address2
        phone = add.phone
        city = add.city
        zip = add.zip
        province = add.province
        country = add.country
        lastName = add.lastName
        company = add.company
        name = add.name
        countryCode = add.countryCode
        provinceCode = add.provinceCode
    }
}