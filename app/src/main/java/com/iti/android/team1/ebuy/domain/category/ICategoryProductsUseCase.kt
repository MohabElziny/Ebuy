package com.iti.android.team1.ebuy.domain.category

import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Categories
import com.iti.android.team1.ebuy.model.pojo.Products

interface ICategoryProductsUseCase {
    suspend fun getAllProducts(): NetworkResponse<Products>
    suspend fun getAllCategories(): NetworkResponse<Categories>
    suspend fun getAllCategoryProductsByType(
        collectionID: Long,
        productType: String,
    ): NetworkResponse<Products>
    suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products>
}