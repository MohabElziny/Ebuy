package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.google.gson.Gson
import com.iti.android.team1.ebuy.model.pojo.Products
import retrofit2.Response
import com.iti.android.team1.ebuy.model.pojo.Brands
import com.iti.android.team1.ebuy.model.pojo.Categories
import com.iti.android.team1.ebuy.model.pojo.Category
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://c48655414af1ada2cd256a6b5ee391be:" +
        "@mobile-ismailia.myshopify.com/admin/api/2022-04/"

private val retrofit = Retrofit.Builder().apply {
    addConverterFactory(GsonConverterFactory.create())
    this.baseUrl(baseUrl)
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

    override suspend fun getAllCategoryProducts(collectionID: Long , productType:String): Response<Products> {
        return retrofitService.getAllCategoryProducts(collectionID,productType)
    }
}