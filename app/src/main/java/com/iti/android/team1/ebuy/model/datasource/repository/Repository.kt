package com.iti.android.team1.ebuy.model.datasource.repository

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.datasource.localsource.ILocalSource
import com.iti.android.team1.ebuy.model.datasource.localsource.converters.CartItemConverter
import com.iti.android.team1.ebuy.model.datasource.localsource.converters.ProductConverter
import com.iti.android.team1.ebuy.model.datasource.remotesource.RemoteSource
import com.iti.android.team1.ebuy.model.datasource.remotesource.RetrofitHelper
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse.FailureResponse
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse.SuccessResponse
import com.iti.android.team1.ebuy.model.pojo.*
import com.iti.android.team1.ebuy.util.AuthRegex
import com.iti.android.team1.ebuy.util.Decoder
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.json.JSONObject

class Repository(
    private val localSource: ILocalSource,
    private val remoteSource: RemoteSource = RetrofitHelper,
    private val decoder: Decoder = Decoder,
    private val authRegex: AuthRegex = AuthRegex,
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

    override suspend fun getAllFavoritesProducts(): List<FavoriteProduct> {
        return localSource.getAllFavoriteProducts()
    }

    override suspend fun removeAllFavoritesProducts() {
        localSource.removeAllFavoriteProducts()
    }

    override suspend fun addProductToFavorite(
        product: Product,
    ): DatabaseResponse<Long> {
        return if (
            product.productID == localSource.addProductToFavorites(ProductConverter.convertProductToEntity(
                product))
        )
            DatabaseResponse.Success(data = product.productID)
        else
            DatabaseResponse.Failure("Error duo inserting product to favorite with id ${product.productID}")
    }

    override suspend fun deleteProductFromFavorite(productId: Long): DatabaseResponse<Int> {
        val insertCode = localSource.removeProductFromFavorites(productId)
        return if (insertCode > 0)
            DatabaseResponse.Success(data = insertCode)
        else
            DatabaseResponse.Failure(errorMsg = "Error duo inserting product to favorite with id $productId")
    }

    override suspend fun isFavoriteProduct(productID: Long): Boolean {
        return localSource.isFavoriteProduct(productID)
    }


    override suspend fun updateFavoriteProduct(favoriteProduct: FavoriteProduct): DatabaseResponse<Int> {
        val state = localSource.updateFavoriteProduct(favoriteProduct)
        return if (state > 0)
            DatabaseResponse.Success(state)
        else
            DatabaseResponse.Failure("Error duo updating product with id: ${favoriteProduct.productID} with code state: $state")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun registerCustomer(customerRegister: CustomerRegister): NetworkResponse<Customer> {
        val response =
            remoteSource.registerCustomer(customerRegister.copy(password = Decoder.encode(
                customerRegister.password)))
        return if (response.isSuccessful) {
            SuccessResponse(response.body()?.customer ?: Customer())
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun loginCustomer(customerLogin: CustomerLogin): NetworkResponse<Customer> {
        val response = remoteSource.loginCustomer(customerLogin)
        return if (response.isSuccessful) {
            SuccessResponse(response.body()?.customers?.get(0) ?: Customer())
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun getCustomerByID(customer_id: Long): NetworkResponse<Customer> {
        val response = remoteSource.getCustomerByID(customer_id)
        return if (response.isSuccessful) {
            SuccessResponse(response.body()?.customer ?: Customer())
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun getCustomerOrders(customer_id: Long): NetworkResponse<OrderAPI> {
        val response = remoteSource.getCustomerOrders(customer_id)
        return if (response.isSuccessful) {
            SuccessResponse(response.body() ?: OrderAPI())
        } else {
            parseError(response.errorBody())
        }
    }


    override suspend fun getFlowFavoriteProducts(): Flow<List<FavoriteProduct>> {
        return localSource.getFlowFavoriteProducts()
    }

    override suspend fun getAllCartProducts(): List<CartItem> {
        return localSource.getAllCartProducts()
    }

    override suspend fun removeAllCartProducts() {
        localSource.removeAllFavoriteProducts()
    }

    override suspend fun addProductToCart(product: Product, quantity: Int): DatabaseResponse<Long> {
        val addResult =
            localSource.addProductToCart(CartItemConverter.convertProductToCartItemEntity(product,
                quantity))
        return if (product.productVariants?.get(0)?.productVariantId == addResult) {
            DatabaseResponse.Success(addResult)
        } else {
            DatabaseResponse.Failure("Error while adding product ${product.productName} to cart with code $addResult")
        }
    }

    override suspend fun removeProductFromCart(productVariantID: Long): DatabaseResponse<Int> {
        val removeResult = localSource.removeProductFromCart(productVariantID)
        return if (removeResult > 0) {
            DatabaseResponse.Success(removeResult)
        } else {
            DatabaseResponse.Failure("Error while remove the product with code $removeResult")
        }
    }

    override suspend fun updateProductInCart(product: Product, quantity: Int) {
        localSource.updateProductInCart(CartItemConverter.convertProductToCartItemEntity(product,
            quantity))
    }

    override suspend fun isProductInCart(productVariantID: Long): Boolean {
        return localSource.isProductInCart(productVariantID)
    }

    override fun isEmailValid(email: String): Boolean {
        return authRegex.isEmailValid(email)
    }

    override fun isPasswordValid(password: String): Boolean {
        return authRegex.isPasswordValid(password)
    }

    @SuppressLint("NewApi")
    override fun decodePassword(password: String): String {
        return decoder.decode(password)
    }

    @SuppressLint("NewApi")
    override fun encodePassword(password: String): String {
        return decoder.encode(password)
    }

    override fun setUserIdToPrefs(userId: Long) = localSource.setUserIdToPrefs(userId)

    override fun setAuthStateToPrefs(state: Boolean) = localSource.setAuthStateToPrefs(state)

    override fun getUserIdFromPrefs() = localSource.getUserIdFromPrefs()

    override fun getAuthStateFromPrefs() = localSource.getAuthStateFromPrefs()


}

private fun parseError(errorBody: ResponseBody?): FailureResponse {
    return errorBody?.let {
        val errorMessage = runCatching {
            JSONObject(it.string()).getString("errors")
        }
        return FailureResponse(errorMessage.getOrDefault("Empty Error"))
    } ?: FailureResponse("Null Error")
}