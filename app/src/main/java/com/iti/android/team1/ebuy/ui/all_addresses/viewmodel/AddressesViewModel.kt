package com.iti.android.team1.ebuy.ui.all_addresses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Addresses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressesViewModel(private val repo: IRepository) : ViewModel() {
    private val _addAddresses: MutableStateFlow<ResultState<List<Addresses>>> =
        MutableStateFlow(ResultState.Loading)
    val allAddresses get() = _addAddresses.asStateFlow()

    fun getAllAddresses() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }
}