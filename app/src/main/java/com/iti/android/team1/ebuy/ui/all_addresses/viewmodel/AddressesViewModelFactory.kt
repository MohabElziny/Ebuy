package com.iti.android.team1.ebuy.ui.all_addresses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.model.data.repository.IRepository

class AddressesViewModelFactory(private val repo: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        AddressesViewModel(repo) as T
}