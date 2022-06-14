package com.iti.android.team1.ebuy.model.data.remotesource

import retrofit2.Response
import com.iti.android.team1.ebuy.model.pojo.*

interface RemoteSource {
    suspend fun getProductsByCollectionID(
        collectionID: Long,
    ): Response<Products>

    suspend fun getAllBrands(): Response<Brands>
    suspend fun getAllProduct(): Response<Products>
    suspend fun getAllCategories(): Response<Categories>
    suspend fun getAllCategoryProducts(collectionID: Long, productType: String): Response<Products>
    suspend fun getProductDetails(product_id: Long): Response<ProductAPI>
    suspend fun registerCustomer(customerRegister: CustomerRegister): Response<CustomerRegisterAPI>
    suspend fun loginCustomer(customerLogin: CustomerLogin): Response<CustomerLoginAPI>
    suspend fun getCustomerByID(customer_id: Long): Response<CustomerRegisterAPI>
    suspend fun getCustomerOrders(customer_id: Long): Response<OrderAPI>

    suspend fun getAllProductsByType(productType: String): Response<Products>
    suspend fun getAllAddresses(customerId: Long): Response<Addresses>
    suspend fun getAddressDetails(customerId: Long, addressId: Long): Response<Address>
    suspend fun addAddress(customerId: Long, address: AddressDto): Response<Address>
    suspend fun updateAddress(
        customerId: Long,
        addressId: Long,
        newAddress: AddressDto,
    ): Response<Address>

    suspend fun setDefaultAddress(customerId: Long, addressId: Long): Response<Address>
    suspend fun deleteAddress(customerId: Long, addressId: Long): Response<Address>

    suspend fun getAllPriceRules():Response<PriceRuleResponse>
    suspend fun getDiscountCodes(price_rule_id: Long):Response<Discount>
    suspend fun postDraftOrder(draft: Draft): Response<Draft>
    suspend fun updateDraftOrder(draft: Draft): Response<Draft>
    suspend fun getDraftOrder(draftId: Long): Response<Draft>
    suspend fun deleteDraftOrder(draftId: Long): Response<Unit>
    suspend fun updateCustomer(customer: Customer): Response<Customer>
}