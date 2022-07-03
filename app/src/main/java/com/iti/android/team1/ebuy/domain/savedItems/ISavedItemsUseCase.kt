package com.iti.android.team1.ebuy.domain.savedItems

import com.iti.android.team1.ebuy.data.factories.NetworkResponse
import com.iti.android.team1.ebuy.data.pojo.Product

interface ISavedItemsUseCase {
    suspend fun getFavoriteItems(): NetworkResponse<List<Product>>
}