package com.iti.android.team1.ebuy.ui.register_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository

class RegisterViewModelFactory(private val repoInterface:IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return RegisterViewModel(repoInterface) as T
    }

}