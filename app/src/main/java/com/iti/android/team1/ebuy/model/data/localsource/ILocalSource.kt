package com.iti.android.team1.ebuy.model.data.localsource

import kotlinx.coroutines.flow.StateFlow

interface ILocalSource {
    fun setUserIdToPrefs(userId: String)
    fun getUserIdFromPrefs(): String
    fun setAuthStateToPrefs(state: Boolean)
    fun getAuthStateFromPrefs(): Boolean
    fun setFavoritesIdToPrefs(favId: String)
    fun setCartIdToPrefs(cartId: String)
    fun getFavoritesIdFromPrefs(): String
    fun getCartIdFromPrefs(): String
    suspend fun setFavoritesNo(favoritesNo: Int)
    fun getFavoritesNo(): StateFlow<Int>
    suspend fun setCartNo(cartNo: Int)
    fun getCartNo(): StateFlow<Int>

    fun isRunFirstTime(): Boolean

    fun setRunFirstTime()

}