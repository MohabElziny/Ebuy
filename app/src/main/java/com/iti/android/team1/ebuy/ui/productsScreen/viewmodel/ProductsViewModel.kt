package com.iti.android.team1.ebuy.ui.productsScreen.viewmodel

import androidx.lifecycle.ViewModel
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository

private const val TAG = "ProductsViewModel"

class ProductsViewModel(private val repoInterface: IRepository) : ViewModel() {

    fun fetchBrandData(id: Long) {}

}