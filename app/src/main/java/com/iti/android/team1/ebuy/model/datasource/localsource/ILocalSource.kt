package com.iti.android.team1.ebuy.model.datasource.localsource

import com.iti.android.team1.ebuy.model.pojo.CartItem
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.flow.Flow

interface ILocalSource {
    fun setUserIdToPrefs(userId: String)
    fun getUserIdFromPrefs(): String
    fun setAuthStateToPrefs(state: Boolean)
    fun getAuthStateFromPrefs(): Boolean
    fun setFavoritesIdToPrefs(favId: String)
    fun setCartIdToPrefs(cartId: String)
    fun getFavoritesIdFromPrefs(): String
    fun getCartIdFromPrefs(): String
}