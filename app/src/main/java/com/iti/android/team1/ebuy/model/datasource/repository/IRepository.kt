package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.*
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getAllProducts(): NetworkResponse<Products>
    suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products>
    suspend fun getAllBrands(): NetworkResponse<Brands>
    suspend fun getAllCategories(): NetworkResponse<Categories>
    suspend fun getAllCategoryProducts(
        collectionID: Long,
        productType: String,
    ): NetworkResponse<Products>

    suspend fun getProductDetails(product_id: Long): NetworkResponse<Product>

    suspend fun getAllFavoritesProducts(): List<FavoriteProduct>
    suspend fun removeAllFavoritesProducts()
    suspend fun addProductToFavorite(favoriteProduct: FavoriteProduct): DatabaseResponse<Long?>
    suspend fun deleteProductFromFavorite(productId: Long): DatabaseResponse<Int?>
    suspend fun isFavoriteProduct(productID: Long): Boolean

}