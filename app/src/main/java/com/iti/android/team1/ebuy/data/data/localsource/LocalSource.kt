package com.iti.android.team1.ebuy.data.data.localsource

import android.content.Context
import com.iti.android.team1.ebuy.data.data.localsource.prefs.PreferenceProvider

class LocalSource(
    private val context: Context,
    private val prefs: PreferenceProvider = PreferenceProvider.getInstance(context),
) : ILocalSource {

    override fun setUserIdToPrefs(userId: String) = prefs.setUserIdToPrefs(userId)

    override fun setAuthStateToPrefs(state: Boolean) = prefs.setUserAuthStateToPrefs(state)

    override fun getUserIdFromPrefs() = prefs.getUserIdFromPrefs()

    override fun getAuthStateFromPrefs() = prefs.isUserSignedInFromPrefs()

    override fun setFavoritesIdToPrefs(favId: String) = prefs.setFavoritesIdToPrefs(favId)

    override fun setCartIdToPrefs(cartId: String) = prefs.setCartIdToPrefs(cartId)

    override fun getFavoritesIdFromPrefs(): String = prefs.getFavoritesIdFromPrefs()

    override fun getCartIdFromPrefs(): String = prefs.getCartIdFromPrefs()

    override suspend fun setFavoritesNo(favoritesNo: Int) = prefs.setFavoriteNo(favoritesNo)

    override fun getFavoritesNo() = prefs.noOfFavorites

    override suspend fun setCartNo(cartNo: Int) = prefs.setCartNo(cartNo)

    override fun getCartNo() = prefs.noOfCart

    override fun setRunFirstTime() = prefs.setRunFirstTime()

    override fun isRunFirstTime(): Boolean = prefs.isRunFirstTime()


}