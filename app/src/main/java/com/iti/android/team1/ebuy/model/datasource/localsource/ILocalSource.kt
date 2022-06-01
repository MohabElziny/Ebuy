package com.iti.android.team1.ebuy.model.datasource.localsource

import com.iti.android.team1.ebuy.model.pojo.CartItem
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct

interface ILocalSource {
    suspend fun addProductToFavorites(favoriteProduct: FavoriteProduct): Long
    suspend fun removeProductFromFavorites(productID: Long): Int
    suspend fun getAllFavoriteProducts(): List<FavoriteProduct>
    suspend fun removeAllFavoriteProducts()
    suspend fun isFavoriteProduct(productID: Long): Boolean
    suspend fun addProductToCart(cartItem: CartItem): Long
    suspend fun removeProductFromCart(productVariantID: Long): Int
    suspend fun getAllCartProducts(): List<CartItem>
    suspend fun removeAllCartProducts(): Int
    suspend fun updateProductInCart(cartItem: CartItem)
}