package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.datasource.localsource.ILocalSource
import com.iti.android.team1.ebuy.model.datasource.localsource.converters.CartItemConverter
import com.iti.android.team1.ebuy.model.datasource.localsource.converters.ProductConverter
import com.iti.android.team1.ebuy.model.datasource.remotesource.DraftsLineItemConverter
import com.iti.android.team1.ebuy.model.datasource.remotesource.RemoteSource
import com.iti.android.team1.ebuy.model.datasource.remotesource.RetrofitHelper
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse.FailureResponse
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse.SuccessResponse
import com.iti.android.team1.ebuy.model.pojo.*
import com.iti.android.team1.ebuy.util.Decoder
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class Repository(
    private val localSource: ILocalSource,
    private val remoteSource: RemoteSource = RetrofitHelper,
    private val decoder: Decoder = Decoder,
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
        val response =
            remoteSource.loginCustomer(customerLogin.copy(password = encode(customerLogin.password)))
        return if (response.isSuccessful) {

            if (!response.body()?.customers.isNullOrEmpty())
                SuccessResponse(response.body()?.customers?.get(0) ?: Customer())
            else
                SuccessResponse(Customer())

        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun getCustomerByID(): NetworkResponse<Customer> {
        val response = remoteSource.getCustomerByID(getUserIdFromPrefs())
        return if (response.isSuccessful) {
            SuccessResponse(response.body()?.customer ?: Customer())
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun getCustomerOrders(): NetworkResponse<OrderAPI> {
        val response = remoteSource.getCustomerOrders(getUserIdFromPrefs())
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

    override suspend fun updateCartItem(cartItem: CartItem) {
        localSource.updateProductInCart(cartItem)
    }

    override suspend fun isProductInCart(productVariantID: Long): Boolean {
        return localSource.isProductInCart(productVariantID)
    }

    override fun decode(input: String): String {
        return decoder.decode(input)
    }

    override fun encode(input: String): String {
        return decoder.encode(input)
    }

    override fun setUserIdToPrefs(userId: Long) =
        localSource.setUserIdToPrefs(encode(userId.toString()))

    override fun setAuthStateToPrefs(state: Boolean) = localSource.setAuthStateToPrefs(state)

    override fun getUserIdFromPrefs() = decode(localSource.getUserIdFromPrefs()).toLong()

    override fun getAuthStateFromPrefs() = localSource.getAuthStateFromPrefs()

    override suspend fun addFavorite(
        product: Product,
    ): NetworkResponse<DraftOrder> {
        getCustomer()?.let {
            if (it.favoriteID.isEmpty()) {
                return postDraftOrder(product, it, isFavorite = true)
            } else {
                return updateDraftOrder(product, draftId = it.favoriteID.toLong())
            }
        } ?: return FailureResponse("Can't find User")
    }

    override suspend fun removeFromFavorite(productId: Long): NetworkResponse<DraftOrder> {
        getCustomer()?.let {
            return removeFavoriteOrCartItem(it, productId, true)
        } ?: return FailureResponse("Can't find User")
    }

    override suspend fun addCart(product: Product, quantity: Int): NetworkResponse<DraftOrder> {
        getCustomer()?.let {
            if (it.cartID.isEmpty()) {
                return postDraftOrder(product, it, quantity, false)
            } else {
                return updateDraftOrder(product, quantity, it.cartID.toLong())
            }
        } ?: return FailureResponse("Can't find User")
    }

    override suspend fun removeFromCart(productId: Long): NetworkResponse<DraftOrder> {
        getCustomer()?.let {
            return removeFavoriteOrCartItem(it, productId, false)
        } ?: return FailureResponse("Can't find User")
    }

    private suspend fun removeFavoriteOrCartItem(
        customer: Customer,
        productId: Long,
        isFavorite: Boolean,
    ): NetworkResponse<DraftOrder> {
        val draft =
            if (isFavorite) getDraft(customer.favoriteID.toLong()) else getDraft(customer.cartID.toLong())
        if (draft?.draftOrder?.lineItems != null) {
            if (draft.draftOrder.lineItems.count() > 1) {
                draft.apply {
                    draftOrder.lineItems.removeAll {
                        it.productId == productId
                    }
                }
                return removeLineItem(draft)
            } else {
                val deleteResponse = deleteDraftOrder(draft.draftOrder.id)
                return if (deleteResponse.isSuccessful) {
                    if (isFavorite) customer.favoriteID = "" else customer.cartID = ""
                    updateCustomer(customer)
                    SuccessResponse(DraftOrder())
                } else {
                    parseError(deleteResponse.errorBody())
                }
            }
        } else {
            return FailureResponse("Your Inventory is already empty")
        }
    }

    private suspend fun postDraftOrder(
        product: Product,
        customer: Customer,
        quantity: Int = 1,
        isFavorite: Boolean,
    ): NetworkResponse<DraftOrder> {
        val draft =
            Draft(DraftOrder(
                lineItems = arrayListOf(DraftsLineItemConverter.convertToLineItem(product,
                    quantity)),
                customer = DraftCustomerID(getUserIdFromPrefs())
            ))

        val response = remoteSource.postDraftOrder(draft)
        return if (response.isSuccessful) {
            setDraftIdToCustomer(customer, isFavorite, response.body()?.draftOrder?.id)
            SuccessResponse(response.body()?.draftOrder ?: DraftOrder())
        } else {
            parseError(response.errorBody())
        }
    }

    private suspend fun updateDraftOrder(
        product: Product,
        quantity: Int = 1,
        draftId: Long,
    ): NetworkResponse<DraftOrder> {
        val draftProduct = DraftsLineItemConverter.convertToLineItem(product, quantity)
        val draft = getDraft(draftId)?.apply {
            this.draftOrder.lineItems.add(draftProduct)
        }

        val response = remoteSource.updateDraftOrder(draft ?: Draft())
        return if (response.isSuccessful) {
            SuccessResponse(response.body()?.draftOrder ?: DraftOrder())
        } else {
            parseError(response.errorBody())
        }
    }

    private suspend fun removeLineItem(draft: Draft): NetworkResponse<DraftOrder> {
        val response = remoteSource.updateDraftOrder(draft)
        return if (response.isSuccessful) {
            SuccessResponse(response.body()?.draftOrder ?: DraftOrder())
        } else {
            parseError(response.errorBody())
        }
    }

    private suspend fun getDraft(draftId: Long): Draft? {
        return when (val response = getDraftFromApi(draftId)) {
            is FailureResponse -> null
            is SuccessResponse -> response.data
        }
    }

    override suspend fun getDraftFromApi(draftId: Long): NetworkResponse<Draft> {
        val response = remoteSource.getDraftOrder(draftId)
        return if (response.isSuccessful) {
            SuccessResponse(response.body() ?: Draft())
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun getFavoriteItems(): NetworkResponse<Draft> {
        getCustomer()?.let {
            return getItemsData(it.favoriteID)
        } ?: return FailureResponse("Can't find User")
    }

    override suspend fun getCartItems(): NetworkResponse<Draft> {
        getCustomer()?.let {
            return getItemsData(it.cartID)
        } ?: return FailureResponse("Can't find User")
    }

    private suspend fun getItemsData(id: String): NetworkResponse<Draft> {
        return if (id.isEmpty()) {
            SuccessResponse(Draft())
        } else {
            getDraftFromApi(id.toLong())
        }
    }


    private suspend fun getCustomer(): Customer? {
        return when (val response = getCustomerByID()) {
            is FailureResponse -> {
                null
            }
            is SuccessResponse -> {
                response.data
            }
        }
    }

    private suspend fun setDraftIdToCustomer(customer: Customer, isFavorite: Boolean, id: Long?) {
        if (isFavorite)
            customer.favoriteID = "$id"
        else
            customer.cartID = "$id"
        updateCustomer(customer)
    }

    private suspend fun deleteDraftOrder(draftId: Long): Response<Unit> {
        return remoteSource.deleteDraftOrder(draftId)
    }

    private suspend fun updateCustomer(customer: Customer) {
        remoteSource.updateCustomer(customer)
    }
    override suspend fun getAllAddresses(customerId: Long): NetworkResponse<Addresses> {
        val response = remoteSource.getAllAddresses(customerId)
        return if (response.isSuccessful)
            SuccessResponse(data = response.body() ?: Addresses())
        else
            parseError(response.errorBody())
    }

    override suspend fun getAddressDetails(
        customerId: Long,
        addressId: Long,
    ): NetworkResponse<Address> {
        val response = remoteSource.getAddressDetails(customerId, addressId)
        return if (response.isSuccessful)
            SuccessResponse(data = response.body() ?: Address())
        else
            parseError(response.errorBody())
    }

    override suspend fun addAddress(
        customerId: Long,
        address: AddressDto,
    ): NetworkResponse<Address> {
        val response = remoteSource.addAddress(customerId, address)
        return if (response.isSuccessful)
            SuccessResponse(data = response.body() ?: Address())
        else
            parseError(response.errorBody())
    }

    override suspend fun updateAddress(
        customerId: Long,
        addressId: Long,
        newAddress: AddressDto
    ): NetworkResponse<Address> {
        val response = remoteSource.updateAddress(customerId, addressId, newAddress)
        return if (response.isSuccessful)
            SuccessResponse(data = response.body() ?: Address())
        else
            parseError(response.errorBody())
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long,
    ): NetworkResponse<Address> {
        val response = remoteSource.setDefaultAddress(customerId, addressId)
        return if (response.isSuccessful)
            SuccessResponse(data = response.body() ?: Address())
        else
            parseError(response.errorBody())
    }

    override suspend fun deleteAddress(
        customerId: Long,
        addressId: Long,
    ): NetworkResponse<Address> {
        val response = remoteSource.deleteAddress(customerId, addressId)
        return if (response.isSuccessful)
            SuccessResponse(data = response.body() ?: Address())
        else
            parseError(response.errorBody())
    }

    override suspend fun getAllPriceRules(): NetworkResponse<PriceRuleResponse> {
        val response = remoteSource.getAllPriceRules()
        return if (response.isSuccessful)
            SuccessResponse(data = response.body() ?: PriceRuleResponse())
        else
            parseError(response.errorBody())
    }


    override suspend fun getAllProductsByType(productType: String): NetworkResponse<Products> {
        val response = remoteSource.getAllProductsByType(productType)
        return if (response.isSuccessful) {
            SuccessResponse(response.body() ?: Products(emptyList()))
        } else {
            parseError(response.errorBody())
        }

    override suspend fun getDiscountCodes(price_rule_id: Long): NetworkResponse<Discount> {
        val response = remoteSource.getDiscountCodes(price_rule_id)
        return if (response.isSuccessful)
            SuccessResponse(data = response.body() ?: Discount())
        else
            parseError(response.errorBody())

    }

}

private fun parseError(errorBody: ResponseBody?): FailureResponse {
    return errorBody?.let {
        val errorMessage = runCatching {
            JSONObject(it.string()).getString("errors")
        }
        return FailureResponse(errorMessage.getOrDefault("Empty Error"))
    } ?: FailureResponse("Null Error")
}