package com.iti.android.team1.ebuy.ui.productsScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.data.repository.IRepository

class ProductViewModelFactory(private val repoInterface: IRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java))
            return ProductsViewModel(repoInterface) as T
        else
            throw IllegalArgumentException("Unknown viewModel !")
    }

}