package com.iti.android.team1.ebuy.data.pojo


class DraftsLineItemConverter {
    companion object {
        fun convertToDraftLineItem(
            product: Product,
            quantity: Int,
        ): DraftsLineItems = DraftsLineItems(
            quantity = quantity,
            productId = product.productID ?: 0,
            variantId = product.productVariants?.get(0)?.productVariantId ?: 0
        )

        fun convertToLineItem(cartItem: CartItem): LineItems = LineItems(
            id = cartItem.productID,
            variantId = cartItem.productVariantID,
            quantity = cartItem.customerProductQuantity,
            name = cartItem.productName,
            price = cartItem.productVariantPrice.toString()
        )
    }
}