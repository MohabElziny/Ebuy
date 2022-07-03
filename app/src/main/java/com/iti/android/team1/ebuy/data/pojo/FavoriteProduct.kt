package com.iti.android.team1.ebuy.data.pojo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProduct(
    @NonNull
    @PrimaryKey
    val productID: Long,
    val productName: String,
    val productPrice: Double,
    val productImageUrl: String,
    var stock: Int,
    var currency: String,
)
