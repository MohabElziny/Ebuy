package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.iti.android.team1.ebuy.model.pojo.Brands

interface RetrofitService {
    @GET("products.json")
    suspend fun getBrandProducts(
        @Query("collection_id") brandID: Long
    ): Response<Products>

    @GET("smart_collections.json")
    suspend fun getAllBrands(): Response<Brands>

    @GET("products.json")
    suspend fun getAllProduct(): Response<Products>
}