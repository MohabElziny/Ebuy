package com.iti.android.team1.ebuy.domain.brandproducts

import com.iti.android.team1.ebuy.model.factories.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Products

interface IBrandProductsUseCase {
    suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products>
}