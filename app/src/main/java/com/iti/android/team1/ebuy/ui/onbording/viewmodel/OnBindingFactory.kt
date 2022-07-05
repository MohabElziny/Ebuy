package com.iti.android.team1.ebuy.ui.onbording.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.data.repository.IRepository

class OnBindingFactory(val repo: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OnBoardingViewModel(repo) as T
    }
}