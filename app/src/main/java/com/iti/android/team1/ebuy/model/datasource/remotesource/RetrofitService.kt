package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.iti.android.team1.ebuy.model.pojo.Brands
import retrofit2.http.Header

private const val password = "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"

interface RetrofitService {
    @GET("products.json")
    suspend fun getProductsByCollectionID(
        @Query("collection_id") collectionID:Long
    ):Response<Products>

    @GET("smart_collections.json")
    suspend fun getAllBrands(
        @Header("X-Shopify-Access-Token") pass:String = password
    ) :Response<Brands>
}