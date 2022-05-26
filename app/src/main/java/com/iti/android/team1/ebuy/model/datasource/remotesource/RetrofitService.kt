package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Header

private const val PASSWORD = "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"

interface RetrofitService {

    @GET("products.json")
    suspend fun getAllProduct(@Header("X-Shopify-Access-Token") pass: String = PASSWORD): Response<Products>

    @GET("products.json")
    suspend fun getProductsByCollectionID(
        @Query("collection_id") collectionID: Long,
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD
        ): Response<Products>

    @GET("smart_collections.json")
    suspend fun getAllBrands(
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD,
    ): Response<Brands>

    @GET("products.json")
    suspend fun getAllCategories(
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD
    ): Response<Categories>
    @GET("products.json")
    suspend fun getAllCategoryProducts(
        @Query("collection_id") collectionID: Long,@Query("product_type") productType:String,
        @Header("X-Shopify-Access-Token") pass: String = PASSWORD
    ): Response<Products>
}