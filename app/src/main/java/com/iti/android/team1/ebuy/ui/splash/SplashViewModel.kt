package com.iti.android.team1.ebuy.ui.splash

import androidx.lifecycle.ViewModel
import com.iti.android.team1.ebuy.data.data.repository.IRepository

class SplashViewModel(val repo: IRepository) : ViewModel() {

    fun isRunFirstTime(): Boolean = repo.isRunFirstTime()


    fun getAuthStateFromPrefs() = repo.getAuthStateFromPrefs()


}