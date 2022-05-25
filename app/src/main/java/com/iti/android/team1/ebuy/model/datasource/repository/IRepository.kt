package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.pojo.Products

import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Brands
import com.iti.android.team1.ebuy.model.pojo.Categories
import com.iti.android.team1.ebuy.model.pojo.Category

interface IRepository {

    suspend fun getAllProducts() :NetworkResponse<Products>
    suspend fun getProductsByCollectionID(collectionID:Long) : NetworkResponse<Products>
    suspend fun getAllBrands() : NetworkResponse<Brands>
    suspend fun getAllCategories() : NetworkResponse<Categories>
    suspend fun getAllCategoryProducts(collectionID: Long , productType:String) : NetworkResponse<Products>

}