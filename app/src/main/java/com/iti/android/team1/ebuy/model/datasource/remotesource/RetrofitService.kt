package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("products.json")
    suspend fun getBrandProducts(
        @Query("collection_id") brandID:Long
    ):Response<Products>
}