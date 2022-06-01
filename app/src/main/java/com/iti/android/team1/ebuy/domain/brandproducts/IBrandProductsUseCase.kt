package com.iti.android.team1.ebuy.domain.brandproducts

import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.model.pojo.Products

interface IBrandProductsUseCase {
    suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products>

    suspend fun addProductToFavorite(product: Product): DatabaseResponse<Long?>

    suspend fun removeProductFromFavorite(productId: Long): DatabaseResponse<Int?>
}