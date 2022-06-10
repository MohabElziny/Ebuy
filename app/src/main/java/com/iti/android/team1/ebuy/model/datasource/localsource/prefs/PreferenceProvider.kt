package com.iti.android.team1.ebuy.model.datasource.localsource.prefs

import android.content.Context
import android.content.SharedPreferences

private const val PREF_NAME = "E_BUY_APP"

private const val USER_ID = "USER_ID_KEY"
private const val IS_USER_SINGED_IN = "IS_USER_SINGED_IN"
private const val FAVORITES_ID = "FAVORITES_ID_KEY"
private const val CART_ID = "CART_ID_KEY"

class PreferenceProvider private constructor() {

    companion object {

        private lateinit var sharedPref: SharedPreferences

        @Volatile
        private var preferenceProvider: PreferenceProvider? = null

        fun getInstance(context: Context): PreferenceProvider {

            return preferenceProvider ?: synchronized(this) {
                if (!::sharedPref.isInitialized)
                    sharedPref =
                        context.applicationContext.getSharedPreferences(PREF_NAME,
                            Context.MODE_PRIVATE)
                val instance = PreferenceProvider()
                preferenceProvider = instance
                instance
            }
        }
    }

    fun isUserSignedInFromPrefs(): Boolean =
        sharedPref.getBoolean(IS_USER_SINGED_IN, false)

    fun setUserAuthStateToPrefs(state: Boolean) =
        sharedPref.edit().putBoolean(IS_USER_SINGED_IN, state).apply()

    fun getUserIdFromPrefs(): String =
        sharedPref.getString(USER_ID, "").toString()

    fun setUserIdToPrefs(userId: String) =
        sharedPref.edit().putString(USER_ID, userId).apply()

    fun getFavoritesIdFromPrefs(): String =
        sharedPref.getString(FAVORITES_ID, "").toString()

    fun setFavoritesIdToPrefs(favId: String) =
        sharedPref.edit().putString(FAVORITES_ID, favId).apply()

    fun getCartIdFromPrefs(): String =
        sharedPref.getString(CART_ID, "").toString()

    fun setCartIdToPrefs(cartId: String) =
        sharedPref.edit().putString(CART_ID, cartId).apply()
}