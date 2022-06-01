package com.iti.android.team1.ebuy.domain.brandproducts

import com.iti.android.team1.ebuy.domain.productsResponse
import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.datasource.localsource.ProductConverter
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.model.pojo.Products

class BrandProductsUseCase(private val repository: IRepository) : IBrandProductsUseCase {
    override suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products> {
        return productsResponse(repository.getProductsByCollectionID(collectionID),
            repository.getAllFavoritesProducts())
    }

    override suspend fun addProductToFavorite(product: Product): DatabaseResponse<Long?> {
        return repository.addProductToFavorite(ProductConverter.convertProductToEntity(product))
    }

    override suspend fun removeProductFromFavorite(productId: Long): DatabaseResponse<Int?> {
        return repository.deleteProductFromFavorite(productId)
    }

}