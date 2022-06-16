package com.iti.android.team1.ebuy.ui.add_address.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.model.data.repository.IRepository

class AddViewModelFactory(private val repo: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = AddAddressViewModel(repo) as T
}