package com.iti.android.team1.ebuy.model.datasource.localsource.prefs

import android.content.Context
import android.content.SharedPreferences

private const val PREF_NAME = "E_BUY_APP"

private const val USER_ID = "USER_ID_KEY"
private const val IS_USER_SINGED_IN = "IS_USER_SINGED_IN"

class PreferenceProvider(context: Context) {

    private val sharedPref: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isUserSignedInFromPrefs(): Boolean =
        sharedPref.getBoolean(IS_USER_SINGED_IN, false)

    fun setUserAuthStateToPrefs(state: Boolean) =
        sharedPref.edit().putBoolean(IS_USER_SINGED_IN, state).apply()

    fun getUserIdFromPrefs(): Long =
        sharedPref.getLong(USER_ID, 1L)


    fun setUserIdToPrefs(userId: Long) =
        sharedPref.edit().putLong(USER_ID, userId).apply()

}