package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.google.gson.Gson
import com.iti.android.team1.ebuy.model.pojo.Products
import retrofit2.Response
import com.iti.android.team1.ebuy.model.pojo.Brands
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://mobile-ismailia.myshopify.com/admin/api/2022-04/"

private val gson = Gson()

private val retrofit = Retrofit.Builder().apply {
    addConverterFactory(GsonConverterFactory.create())
    baseUrl(BASE_URL)
}.build()

object RetrofitHelper : RemoteSource {
    private val retrofitService by lazy { retrofit.create(RetrofitService::class.java)}

    override suspend fun getProductsByCollectionID(collectionID: Long): Response<Products> {
        return retrofitService.getBrandProducts(collectionID)
    }

    override suspend fun getAllBrands(): Response<Brands> {
        return retrofitService.getAllBrands()
    }
}