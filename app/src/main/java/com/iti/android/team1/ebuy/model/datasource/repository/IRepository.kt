package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.*
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getAllProducts(): NetworkResponse<Products>
    suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products>
    suspend fun getAllBrands(): NetworkResponse<Brands>
    suspend fun getAllCategories(): NetworkResponse<Categories>
    suspend fun getAllCategoryProducts(
        collectionID: Long,
        productType: String,
    ): NetworkResponse<Products>

    suspend fun getProductDetails(product_id: Long): NetworkResponse<Product>
    suspend fun registerCustomer(customerRegister: CustomerRegister): NetworkResponse<Customer>
    suspend fun loginCustomer(customerLogin: CustomerLogin): NetworkResponse<Customer>
    suspend fun getCustomerByID(): NetworkResponse<Customer>
    suspend fun getCustomerOrders(): NetworkResponse<OrderAPI>

    suspend fun getAllFavoritesProducts(): List<FavoriteProduct>
    suspend fun removeAllFavoritesProducts()
    suspend fun addProductToFavorite(product: Product): DatabaseResponse<Long?>
    suspend fun deleteProductFromFavorite(productId: Long): DatabaseResponse<Int?>
    suspend fun isFavoriteProduct(productID: Long): Boolean
    suspend fun updateFavoriteProduct(favoriteProduct: FavoriteProduct): DatabaseResponse<Int>

    suspend fun getFlowFavoriteProducts(): Flow<List<FavoriteProduct>>
    suspend fun getAllCartProducts(): List<CartItem>
    suspend fun removeAllCartProducts()
    suspend fun addProductToCart(product: Product, quantity: Int): DatabaseResponse<Long>
    suspend fun removeProductFromCart(productVariantID: Long): DatabaseResponse<Int>
    suspend fun updateProductInCart(product: Product, quantity: Int)
    suspend fun updateCartItem(cartItem: CartItem)

    suspend fun isProductInCart(productVariantID: Long): Boolean

    fun isEmailValid(email: String): Boolean
    fun isPasswordValid(password: String): Boolean

    fun decodePassword(password: String): String
    fun encodePassword(password: String): String

    fun setUserIdToPrefs(userId: Long)
    fun setAuthStateToPrefs(state: Boolean)
    fun getUserIdFromPrefs(): Long
    fun getAuthStateFromPrefs(): Boolean

    suspend fun addFavorite(
        product: Product,
    ): NetworkResponse<DraftOrder>

    suspend fun addCart(
        product: Product,
        quantity: Int,
    ): NetworkResponse<DraftOrder>

    suspend fun removeFromFavorite(
        productId: Long,
    ): NetworkResponse<DraftOrder>

    suspend fun removeFromCart(
        productId: Long,
    ): NetworkResponse<DraftOrder>

    suspend fun getDraftFromApi(draftId: Long): NetworkResponse<Draft>

    suspend fun getFavoriteItems(): NetworkResponse<Draft>
    suspend fun getCartItems(): NetworkResponse<Draft>
}