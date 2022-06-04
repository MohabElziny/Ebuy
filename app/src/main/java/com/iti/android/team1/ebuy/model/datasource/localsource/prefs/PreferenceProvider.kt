package com.iti.android.team1.ebuy.model.datasource.localsource.prefs

import android.content.Context
import android.content.SharedPreferences

private const val PREF_NAME = "E_BUY_APP"

private const val USER_ID = "USER_ID_KEY"
private const val IS_USER_SINGED_IN = "IS_USER_SINGED_IN"

class PreferenceProvider private constructor() {

    companion object {

        private lateinit var sharedPref: SharedPreferences
        private lateinit var preferenceProvider: PreferenceProvider

        fun getInstance(context: Context): PreferenceProvider {
            sharedPref =
                context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            preferenceProvider = PreferenceProvider()

            return preferenceProvider
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

}