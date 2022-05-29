package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.*
import kotlinx.coroutines.flow.Flow
import com.iti.android.team1.ebuy.model.pojo.*

interface IRepository {
    suspend fun getAllProducts(): NetworkResponse<Products>
    suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products>
    suspend fun getAllBrands(): NetworkResponse<Brands>
    suspend fun getAllCategories(): NetworkResponse<Categories>
    suspend fun getAllCategoryProducts(
        collectionID: Long,
        productType: String,
    ): NetworkResponse<Products>
    suspend fun getProductDetails(product_id :Long):NetworkResponse<Product>

    suspend fun getAllFavoritesProducts(): Flow<List<FavoriteProduct>>
    suspend fun removeAllFavoritesProducts()
    suspend fun addProductToFavorite(favoriteProduct: FavoriteProduct)
    suspend fun deleteProductFromFavorite(favoriteProduct: FavoriteProduct)
}