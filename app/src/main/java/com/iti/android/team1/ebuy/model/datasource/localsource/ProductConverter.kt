package com.iti.android.team1.ebuy.model.datasource.localsource

import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import com.iti.android.team1.ebuy.model.pojo.Product

class ProductConverter {
    companion object {
        fun convertProductToEntity(product: Product): FavoriteProduct =
            FavoriteProduct(product.productID ?: 0)
    }
}