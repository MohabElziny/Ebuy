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

    fun decode(input: String): String
    fun encode(input: String): String

    fun setUserIdToPrefs(userId: Long)
    fun setAuthStateToPrefs(state: Boolean)
    fun getUserIdFromPrefs(): Long
    fun getAuthStateFromPrefs(): Boolean
    fun setFavoritesIdToPrefs(favId: String)
    fun setCartIdToPrefs(cartId: String)

    suspend fun getAllProductsByType(productType: String): NetworkResponse<Products>
    suspend fun getAllAddresses(customerId: Long): NetworkResponse<Addresses>
    suspend fun getAddressDetails(customerId: Long, addressId: Long): NetworkResponse<Address>
    suspend fun addAddress(customerId: Long, address: AddressDto): NetworkResponse<Address>
    suspend fun updateAddress(
        customerId: Long,
        addressId: Long,
        newAddress: AddressDto,
    ): NetworkResponse<Address>

    suspend fun setDefaultAddress(customerId: Long, addressId: Long): NetworkResponse<Address>
    suspend fun deleteAddress(customerId: Long, addressId: Long): NetworkResponse<Address>

    suspend fun getAllPriceRules():NetworkResponse<PriceRuleResponse>
    suspend fun getDiscountCodes(price_rule_id: Long):NetworkResponse<Discount>

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

    suspend fun updateCart(cartItems: List<CartItem>)

    suspend fun getDraftFromApi(draftId: Long): NetworkResponse<Draft>
    suspend fun getFavoriteItems(): NetworkResponse<Draft>
    suspend fun getCartItems(): NetworkResponse<Draft>
}