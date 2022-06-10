package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.DraftsLineItems
import com.iti.android.team1.ebuy.model.pojo.Product

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