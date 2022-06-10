package com.iti.android.team1.ebuy.model.pojo


class DraftsLineItemConverter {
    companion object {
        fun convertToLineItem(
            product: Product,
            quantity: Int,
        ): DraftsLineItems = DraftsLineItems(
            quantity = quantity,
            productId = product.productID ?: 0,
            variantId = product.productVariants?.get(0)?.productVariantId ?: 0
        )

    }
}