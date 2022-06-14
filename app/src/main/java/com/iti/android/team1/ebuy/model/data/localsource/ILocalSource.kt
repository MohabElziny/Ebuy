package com.iti.android.team1.ebuy.model.data.localsource

interface ILocalSource {
    fun setUserIdToPrefs(userId: String)
    fun getUserIdFromPrefs(): String
    fun setAuthStateToPrefs(state: Boolean)
    fun getAuthStateFromPrefs(): Boolean
    fun setFavoritesIdToPrefs(favId: String)
    fun setCartIdToPrefs(cartId: String)
    fun getFavoritesIdFromPrefs(): String
    fun getCartIdFromPrefs(): String
    fun setFavroitesNo(favoritesNo: Int)
    fun getFavoritesNo(): Int
    fun setCartNo(cartNo: Int)
    fun getCartNo(): Int
}