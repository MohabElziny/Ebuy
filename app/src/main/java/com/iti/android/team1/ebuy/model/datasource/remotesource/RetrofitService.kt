package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.Brands
import com.iti.android.team1.ebuy.model.pojo.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val PASSWORD = "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"

interface RetrofitService {

    @Headers(
        "X-shopify-Access-Token:${PASSWORD}"
    )
    @GET("products.json")
    suspend fun getBrandProducts(
        @Query("collection_id") brandID: Long
    ): Response<Products>

    @GET("smart_collections.json")
    suspend fun getAllBrands(): Response<Brands>


}