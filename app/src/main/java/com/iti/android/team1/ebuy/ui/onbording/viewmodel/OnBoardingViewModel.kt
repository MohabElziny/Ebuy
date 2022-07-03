package com.iti.android.team1.ebuy.ui.onbording.viewmodel

import androidx.lifecycle.ViewModel
import com.iti.android.team1.ebuy.data.data.repository.IRepository

class OnBoardingViewModel(private val repo: IRepository) : ViewModel(){

    fun setRunFirstTime() = repo.setRunFirstTime()
}