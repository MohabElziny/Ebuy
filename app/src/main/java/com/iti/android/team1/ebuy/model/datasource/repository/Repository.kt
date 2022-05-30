package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.datasource.localsource.ILocalSource
import com.iti.android.team1.ebuy.model.datasource.remotesource.RemoteSource
import com.iti.android.team1.ebuy.model.datasource.remotesource.RetrofitHelper
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse.FailureResponse
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse.SuccessResponse
import com.iti.android.team1.ebuy.model.pojo.*
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.json.JSONObject

class Repository(
    private val localSource: ILocalSource,
    private val remoteSource: RemoteSource = RetrofitHelper,
) : IRepository {

    override suspend fun getAllBrands(): NetworkResponse<Brands> {
        val response = remoteSource.getAllBrands()
        return if (response.isSuccessful) {
            SuccessResponse(response.body() ?: Brands(emptyList()))
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun getAllProducts(): NetworkResponse<Products> {
        val response = remoteSource.getAllProduct()
        return if (response.isSuccessful) {
            SuccessResponse(response.body() ?: Products(emptyList()))
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products> {
        val response = remoteSource.getProductsByCollectionID(collectionID)
        return if (response.isSuccessful) {
            SuccessResponse(response.body() ?: Products(emptyList()))
        } else {
            parseError(response.errorBody())
        }
    }

    private fun parseError(errorBody: ResponseBody?): FailureResponse {
        return errorBody?.let {
            val errorMessage = kotlin.runCatching {
                JSONObject(it.string()).getString("errors")
            }
            return FailureResponse(errorMessage.getOrDefault("Empty Error"))
        } ?: FailureResponse("Null Error")
    }

//    private fun <T> sendResponseBack( obj :Any,response: Response<T>): NetworkResponse<Any> {
//        return if (response.isSuccessful) {
//            SuccessResponse(response.body() ?: obj())
//        } else {
//            parseError(response.errorBody())
//        }
//    }

    override suspend fun getAllCategories(): NetworkResponse<Categories> {
        val response = remoteSource.getAllCategories()
        return if (response.isSuccessful) {
            SuccessResponse(response.body() ?: Categories(emptyList()))
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun getAllCategoryProducts(
        collectionID: Long,
        productType: String,
    ): NetworkResponse<Products> {
        val response = remoteSource.getAllCategoryProducts(collectionID, productType)
        return if (response.isSuccessful) {
            SuccessResponse(response.body() ?: Products(emptyList()))
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun getProductDetails(product_id: Long): NetworkResponse<Product> {
        val response = remoteSource.getProductDetails(product_id)
        return if (response.isSuccessful) {
            SuccessResponse(response.body()?.product ?: Product())
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun getAllFavoritesProducts(): DatabaseResponse<List<FavoriteProduct>> {
        return DatabaseResponse.Success(data = localSource.getAllFavoriteProducts())
    }

    override suspend fun removeAllFavoritesProducts() {
        localSource.removeAllFavoriteProducts()
    }

    override suspend fun addProductToFavorite(favoriteProduct: FavoriteProduct): DatabaseResponse<Long> {
        return if (favoriteProduct.productID == localSource.addProductToFavorites(favoriteProduct))
            DatabaseResponse.Success(data = favoriteProduct.productID)
        else
            DatabaseResponse.Failure("Error duo inserting product to favorite with id ${favoriteProduct.productID}")
    }

    override suspend fun deleteProductFromFavorite(productId: Long): DatabaseResponse<Int> {
        val insertCode = localSource.removeProductFromFavorites(productId)
        return if (insertCode > 0)
            DatabaseResponse.Success(data = insertCode)
        else
            DatabaseResponse.Failure(errorMsg = "Error duo inserting product to favorite with id $productId")
    }

    override suspend fun isFavoriteProduct(productID: Long): DatabaseResponse<Boolean> {
        return DatabaseResponse.Success(data = localSource.isFavoriteProduct(productID))
    }
}