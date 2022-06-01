package com.iti.android.team1.ebuy.model.datasource.localsource

import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import com.iti.android.team1.ebuy.model.pojo.Product

class ProductConverter {
    companion object {
        fun convertProductToEntity(product: Product): FavoriteProduct =
            FavoriteProduct(product.productID ?: 0,
                product.productName ?: "",
                product.productVariants?.get(0)?.productVariantPrice?.toDouble() ?: 0.0,
                product.productImage?.imageURL ?: "",
                product.productVariants?.get(0)?.productVariantInventoryQuantity ?: 0)

    }
}