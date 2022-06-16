package com.iti.android.team1.ebuy.ui.orders.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.model.data.repository.IRepository

class OrdersViewModelFactory(val repoInterface: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrdersViewModel(repoInterface) as T
    }
}