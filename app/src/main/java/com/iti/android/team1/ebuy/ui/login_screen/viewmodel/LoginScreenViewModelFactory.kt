package com.iti.android.team1.ebuy.ui.login_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository

class LoginScreenViewModelFactory(private val repo: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginScreenViewModel(repo) as T
    }
}