package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.*
import retrofit2.Response
import retrofit2.http.*


private const val PASSWORD = "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"

interface RetrofitService {

    @GET("products.json")
    suspend fun getAllProduct(@Header("X-Shopify-Access-Token") pass: String = PASSWORD): Response<Products>

    @GET("products.json")
    suspend fun getProductsByCollectionID(
        @Query("collection_id") collectionID: Long,
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<Products>

    @GET("smart_collections.json")
    suspend fun getAllBrands(
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<Brands>

    @GET("custom_collections.json")
    suspend fun getAllCategories(
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<Categories>

    @GET("products.json")
    suspend fun getAllCategoryProducts(
        @Query("collection_id") collectionID: Long, @Query("product_type") productType: String,
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<Products>

    @GET("products/{product_id}.json")
    suspend fun getProductDetailsById(
        @Path("product_id") product_id: Long,
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<ProductAPI>

    @POST("customers.json")
    suspend fun registerCustomer(
        @Body customerPost: CustomerPost,
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<CustomerRegisterAPI>

    @GET("customers/search.json")
    suspend fun loginCustomer(
        @Query("query") password: String,
        @Query("email") email: String,
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<CustomerLoginAPI>

    @GET("customers/{customer_id}.json")
    suspend fun getCustomerById(
        @Path("customer_id") customer_id: Long,
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<CustomerRegisterAPI>

    @GET("customers/{customer_id}/orders.json")
    suspend fun getCustomerOrders(
        @Path("customer_id") customer_id: Long,
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<OrderAPI>

    @GET("products.json")
    suspend fun getAllProductsByType(
        @Query("product_type") productType: String,
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<Products>
}