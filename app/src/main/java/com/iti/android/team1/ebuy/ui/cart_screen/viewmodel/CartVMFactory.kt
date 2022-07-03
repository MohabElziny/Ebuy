package com.iti.android.team1.ebuy.ui.cart_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.data.repository.IRepository

class CartVMFactory(val myRepo:IRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(myRepo) as T
    }
}