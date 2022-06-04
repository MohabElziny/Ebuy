package com.iti.android.team1.ebuy.model.datasource.localsource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.iti.android.team1.ebuy.model.pojo.CartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM cartItems")
    suspend fun getAllItemsInCart(): List<CartItem>

    @Query("DELETE FROM cartItems")
    suspend fun removeAllItemsFromCart(): Int

    @Insert(onConflict = REPLACE)
    suspend fun insertItemToCart(cartItem: CartItem): Long

    @Query("DELETE FROM cartItems WHERE productVariantID = :productVariantID")
    suspend fun removeItemFromCart(productVariantID: Long): Int

    @Update
    suspend fun updateItemInTheCart(cartItem: CartItem)

    @Query("SELECT EXISTS(SELECT productVariantID FROM cartItems WHERE productVariantID= :productVariantID)")
    suspend fun isProductInCart(productVariantID: Long): Boolean
}