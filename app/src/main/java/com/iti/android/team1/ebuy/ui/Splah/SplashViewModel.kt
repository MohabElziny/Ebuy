package com.iti.android.team1.ebuy.ui.Splah

import androidx.lifecycle.ViewModel
import com.iti.android.team1.ebuy.model.data.localsource.prefs.PreferenceProvider
import com.iti.android.team1.ebuy.model.data.repository.IRepository

class SplashViewModel(val repo: IRepository) : ViewModel() {

    fun isRunFirstTime(): Boolean = repo.isRunFirstTime()

    fun setRunFirstTime() = repo.setRunFirstTime()

}