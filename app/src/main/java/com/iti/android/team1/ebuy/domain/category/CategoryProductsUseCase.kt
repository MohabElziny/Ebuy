package com.iti.android.team1.ebuy.domain.category

import com.iti.android.team1.ebuy.domain.productsResponse
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Categories
import com.iti.android.team1.ebuy.model.pojo.Products

class CategoryProductsUseCase(
    private val repository: IRepository,
) : ICategoryProductsUseCase {

    override suspend fun getAllProducts(): NetworkResponse<Products> {
        return productsResponse(repository.getAllProducts(), repository.getAllFavoritesProducts())
    }

    override suspend fun getAllCategories(): NetworkResponse<Categories> {
        return repository.getAllCategories()
    }

    override suspend fun getAllCategoryProductsByType(
        collectionID: Long,
        productType: String,
    ): NetworkResponse<Products> {
        val networkResponse = repository.getAllCategoryProducts(collectionID, productType)
        return productsResponse(networkResponse, repository.getAllFavoritesProducts())
    }

    override suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products> {
        val networkResponse = repository.getProductsByCollectionID(collectionID)
        return productsResponse(networkResponse, repository.getAllFavoritesProducts())
    }

}