package com.iti.android.team1.ebuy.model.data.repository

import com.iti.android.team1.ebuy.model.data.localsource.ILocalSource
import com.iti.android.team1.ebuy.model.data.remotesource.RemoteSource
import com.iti.android.team1.ebuy.model.data.remotesource.RetrofitHelper
import com.iti.android.team1.ebuy.model.factories.NetworkResponse
import com.iti.android.team1.ebuy.model.factories.NetworkResponse.FailureResponse
import com.iti.android.team1.ebuy.model.factories.NetworkResponse.SuccessResponse
import com.iti.android.team1.ebuy.model.pojo.*
import com.iti.android.team1.ebuy.util.Decoder
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

    override fun decode(input: String): String {
        return decoder.decode(input)
    }

    override fun encode(input: String): String {
        return decoder.encode(input)
    }

    override fun setUserIdToPrefs(userId: Long) =
        localSource.setUserIdToPrefs(encode(userId.toString()))

    override fun setAuthStateToPrefs(state: Boolean) = localSource.setAuthStateToPrefs(state)

    override suspend fun logOut() {
        localSource.apply {
            setAuthStateToPrefs(false)
            setUserIdToPrefs("")
            setCartIdToPrefs("")
            setFavoritesIdToPrefs("")
            setCartNo(0)
            setFavoritesNo(0)
        }
    }

    override fun getUserIdFromPrefs() = decode(localSource.getUserIdFromPrefs()).toLong()

    override fun getAuthStateFromPrefs() = localSource.getAuthStateFromPrefs()
    override fun setFavoritesIdToPrefs(favId: String) {
        localSource.setFavoritesIdToPrefs(encode(favId))
    }

    override fun setCartIdToPrefs(cartId: String) {
        localSource.setCartIdToPrefs(encode(cartId))
    }

    override fun getFavoritesNo() = localSource.getFavoritesNo()

    override suspend fun setFavoritesNo(favoritesNo: Int) = localSource.setFavoritesNo(favoritesNo)

    override fun getCartNo() = localSource.getCartNo()

    override suspend fun setCartNo(cartNo: Int) = localSource.setCartNo(cartNo)

    private fun getFavoritesIdFromPrefs() = decode(localSource.getFavoritesIdFromPrefs())

    override fun getCartIdFromPrefs() = decode(localSource.getCartIdFromPrefs())

    override suspend fun addFavorite(
        product: Product,
    ): NetworkResponse<DraftOrder> {
        val favoriteID = getFavoritesIdFromPrefs()
        setFavoritesNo(getFavoritesNo().value + 1)
        return if (favoriteID.isEmpty()) {
            postDraftOrder(product, isFavorite = true)
        } else {
            updateDraftOrder(product, draftId = favoriteID.toLong())
        }
    }

    override suspend fun removeFromFavorite(productId: Long): NetworkResponse<DraftOrder> {
        setFavoritesNo(getFavoritesNo().value - 1)
        return removeFavoriteOrCartItem(productId, true)
    }

    override suspend fun addCart(product: Product, quantity: Int): NetworkResponse<DraftOrder> {
        val cartId = getCartIdFromPrefs()
        setCartNo(getCartNo().value + 1)
        return if (cartId.isEmpty()) {
            postDraftOrder(product, quantity, false)
        } else {
            updateDraftOrder(product, quantity, cartId.toLong())
        }
    }

    override suspend fun removeFromCart(productId: Long): NetworkResponse<DraftOrder> {
        setCartNo(getCartNo().value - 1)
        return removeFavoriteOrCartItem(productId, false)
    }

    private suspend fun removeFavoriteOrCartItem(
        productId: Long,
        isFavorite: Boolean,
    ): NetworkResponse<DraftOrder> {
        val draft = if (isFavorite) getDraft(getFavoritesIdFromPrefs().toLong()) else getDraft(
            getCartIdFromPrefs().toLong())
        return if (draft?.draftOrder?.lineItems != null) {
            if (draft.draftOrder.lineItems.count() > 1) {
                draft.apply {
                    draftOrder.lineItems.removeIf {
                        it.productId == productId
                    }
                }
                removeLineItem(draft)
            } else {
                deleteLastDraftItem(isFavorite, draft.draftOrder.id)
            }
        } else {
            FailureResponse("Your Inventory is already empty")
        }
    }

    private suspend fun deleteLastDraftItem(
        isFavorite: Boolean,
        draftOrderId: Long,
    ): NetworkResponse<DraftOrder> {
        val deleteResponse = deleteDraftOrder(draftOrderId)
        return if (deleteResponse.isSuccessful) {
            resetFavOrCartInSharedPref(isFavorite)
            val customer = getCustomer()
            if (isFavorite) customer.favoriteID = "" else customer.cartID = ""
            updateCustomer(customer)
            SuccessResponse(DraftOrder())
        } else {
            parseError(deleteResponse.errorBody())
        }
    }

    private suspend fun resetFavOrCartInSharedPref(isFavorite: Boolean) {
        localSource.apply {
            if (isFavorite) {
                setFavoritesIdToPrefs("")
                setFavoritesNo(0)
            } else {
                setCartIdToPrefs("")
                setCartNo(0)
            }
        }
    }

    private suspend fun postDraftOrder(
        product: Product,
        quantity: Int = 1,
        isFavorite: Boolean,
    ): NetworkResponse<DraftOrder> {
        val draft =
            Draft(DraftOrder(
                lineItems = arrayListOf(DraftsLineItemConverter.convertToDraftLineItem(product,
                    quantity)),
                customer = DraftCustomerID(getUserIdFromPrefs())
            ))

        val response = remoteSource.postDraftOrder(draft)
        return if (response.isSuccessful) {
            setDraftIdToCustomer(getCustomer(), isFavorite, response.body()?.draftOrder?.id)
            SuccessResponse(response.body()?.draftOrder ?: DraftOrder())
        } else {
            parseError(response.errorBody())
        }
    }

    override suspend fun updateCart(cartItems: List<CartItem>) {
        val draftId = getCartIdFromPrefs()
        if (draftId.isEmpty()) return
        val draft = getDraft(draftId.toLong())
        remoteSource.updateDraftOrder(updateItems(draft, cartItems) ?: Draft())
    }

    private fun updateItems(draft: Draft?, cartItems: List<CartItem>): Draft? {
        val lineItemsMap = draft?.draftOrder?.lineItems?.associateBy { item ->
            item.productId
        }
        cartItems.forEach { cartItem ->
            lineItemsMap?.get(cartItem.productID).apply {
                this?.quantity = cartItem.customerProductQuantity
            }
        }
        draft?.apply {
            this.draftOrder.lineItems = lineItemsMap?.values!!.toCollection(ArrayList())
        }
        return draft
    }

    private suspend fun updateDraftOrder(
        product: Product,
        quantity: Int = 1,
        draftId: Long,
    ): NetworkResponse<DraftOrder> {
        val draftProduct = DraftsLineItemConverter.convertToDraftLineItem(product, quantity)
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

    override suspend fun getFavoriteItems(): NetworkResponse<Draft> =
        getItemsData(getFavoritesIdFromPrefs())

    override suspend fun getCartItems(): NetworkResponse<Draft> =
        getItemsData(getCartIdFromPrefs())

    override suspend fun postOrder(order: Order): NetworkResponse<Order> {
        order.customer = Customer(id = getUserIdFromPrefs())
        val response = remoteSource.postOrder(order)
        return if (response.isSuccessful) {
            deleteLastDraftItem(false, getCartIdFromPrefs().toLong())
            SuccessResponse(response.body() ?: Order())
        } else {
            parseError(response.errorBody())
        }
    }

    private suspend fun getItemsData(id: String): NetworkResponse<Draft> {
        return if (id.isEmpty()) {
            SuccessResponse(Draft())
        } else {
            getDraftFromApi(id.toLong())
        }
    }

    private suspend fun getCustomer(): Customer {
        return when (val response = getCustomerByID()) {
            is FailureResponse -> {
                Customer()
            }
            is SuccessResponse -> {
                response.data
            }
        }
    }

    private suspend fun setDraftIdToCustomer(customer: Customer, isFavorite: Boolean, id: Long?) {
        if (isFavorite) {
            customer.favoriteID = "$id"
            setFavoritesIdToPrefs("$id")
        } else {
            customer.cartID = "$id"
            setCartIdToPrefs("$id")
        }
        updateCustomer(customer)
    }

    private suspend fun deleteDraftOrder(draftId: Long): Response<Unit> =
        remoteSource.deleteDraftOrder(draftId)

    private suspend fun updateCustomer(customer: Customer) =
        remoteSource.updateCustomer(customer)

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
        newAddress: AddressDto,
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