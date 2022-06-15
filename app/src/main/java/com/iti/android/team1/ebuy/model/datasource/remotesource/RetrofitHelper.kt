package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://mad-ism2022.myshopify.com/admin/api/2022-04/"

private val retrofit = Retrofit.Builder().apply {
    addConverterFactory(GsonConverterFactory.create())
    baseUrl(BASE_URL)
}.build()

object RetrofitHelper : RemoteSource {
    private val retrofitService by lazy { retrofit.create(RetrofitService::class.java) }

    override suspend fun getProductsByCollectionID(collectionID: Long): Response<Products> {
        return retrofitService.getProductsByCollectionID(collectionID)
    }

    override suspend fun getAllBrands(): Response<Brands> {
        return retrofitService.getAllBrands()
    }

    override suspend fun getAllProduct(): Response<Products> {
        return retrofitService.getAllProduct()
    }

    override suspend fun getAllCategories(): Response<Categories> {
        return retrofitService.getAllCategories()
    }

    override suspend fun getAllCategoryProducts(
        collectionID: Long,
        productType: String,
    ): Response<Products> {
        return retrofitService.getAllCategoryProducts(collectionID, productType)
    }

    override suspend fun getProductDetails(product_id: Long): Response<ProductAPI> {
        return retrofitService.getProductDetailsById(product_id)
    }

    override suspend fun registerCustomer(customerRegister: CustomerRegister): Response<CustomerRegisterAPI> {
        return retrofitService.registerCustomer(CustomerPost(customerRegister))

    }

    override suspend fun loginCustomer(customerLogin: CustomerLogin) =
        retrofitService.loginCustomer(password = customerLogin.password,
            email = customerLogin.email)


    override suspend fun getCustomerByID(customer_id: Long): Response<CustomerRegisterAPI> {
        return retrofitService.getCustomerById(customer_id)
    }

    override suspend fun getCustomerOrders(customer_id: Long): Response<OrderAPI> {
        return retrofitService.getCustomerOrders(customer_id)
    }

    override suspend fun getAllProductsByType(productType: String): Response<Products> {
        return retrofitService.getAllProductsByType(productType)
    }

    override suspend fun getAllAddresses(
        customerId: Long,
    ): Response<Addresses> = retrofitService.getAllAddresses(customerId)

    override suspend fun getAddressDetails(customerId: Long, addressId: Long) =
        retrofitService.getAddressDetails(addressId, addressId)

    override suspend fun addAddress(customerId: Long, address: AddressDto) =
        retrofitService.addAddress(customerId, address = address)

    override suspend fun updateAddress(customerId: Long, addressId: Long, newAddress: AddressDto) =
        retrofitService.updateAddress(customerId, addressId, newAddress)

    override suspend fun setDefaultAddress(customerId: Long, addressId: Long) =
        retrofitService.setDefaultAddress(customerId, addressId)

    override suspend fun deleteAddress(customerId: Long, addressId: Long) =
        retrofitService.deleteAddress(customerId, addressId)

    override suspend fun getAllPriceRules(): Response<PriceRuleResponse> {
        return retrofitService.getAllPriceRules()
    }

    override suspend fun getDiscountCodes(price_rule_id: Long): Response<Discount> {
        return retrofitService.getDiscountCodes(price_rule_id)
    }

    override suspend fun postDraftOrder(draft: Draft): Response<Draft> {
        return retrofitService.postDraftOrder(draft)
    }

    override suspend fun updateDraftOrder(draft: Draft): Response<Draft> {
        return retrofitService.updateDraftOrder(draft.draftOrder.id, draft)
    }

    override suspend fun getDraftOrder(draftId: Long): Response<Draft> {
        return retrofitService.getDraftOrder(draftId)
    }

    override suspend fun deleteDraftOrder(draftId: Long): Response<Unit> {
        return retrofitService.deleteDraftOrder(draftId)
    }

    override suspend fun updateCustomer(customer: Customer): Response<Customer> {
        val customerId = customer.id ?: 0
        return retrofitService.updateCustomer(customerId, CustomerRegisterAPI(customer))
    }

    override suspend fun postOrder(order: Order): Response<Order> {
        return retrofitService.postOrder(OrderPost(order))
    }
}