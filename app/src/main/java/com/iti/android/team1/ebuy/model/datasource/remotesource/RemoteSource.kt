package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.Products
import retrofit2.Response
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Brands
import com.iti.android.team1.ebuy.model.pojo.Categories
import com.iti.android.team1.ebuy.model.pojo.Category

interface RemoteSource {
    suspend fun getProductsByCollectionID(
        collectionID:Long
    ) : Response<Products>

    suspend fun getAllBrands() : Response<Brands>
    suspend fun getAllProduct(): Response<Products>
    suspend fun getAllCategories(): Response<Categories>
    suspend fun getAllCategoryProducts(collectionID: Long , productType:String): Response<Products>
}