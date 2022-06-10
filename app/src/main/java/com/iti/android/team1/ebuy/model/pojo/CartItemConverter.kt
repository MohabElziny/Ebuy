package com.iti.android.team1.ebuy.model.pojo

class CartItemConverter {
    companion object {
        fun convertProductToCartItemEntity(
            product: Product,
            quantity: Int = 1,
        ): CartItem {
            val productVariant = product.productVariants?.get(0)
            return CartItem(
                productVariantID = productVariant?.productVariantId ?: 0,
                productVariantInventoryItemId = productVariant?.productVariantInventoryItemId ?: 0,
                productID = product.productID ?: 0,
                productName = product.productName ?: "",
                variantInventoryQuantity = productVariant?.productVariantInventoryQuantity ?: 0,
                productImageURL = product.productImage?.imageURL ?: "",
                productVariantPrice = productVariant?.productVariantPrice?.toDouble() ?: 0.0,
                productVariantOption1 = productVariant?.productVariantOption1 ?: "",
                productVariantOption2 = productVariant?.productVariantOption2 ?: "",
                customerProductQuantity = quantity,
            )
        }
    }
}