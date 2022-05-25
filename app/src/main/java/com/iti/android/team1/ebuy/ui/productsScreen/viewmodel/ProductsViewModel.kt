package com.iti.android.team1.ebuy.ui.productsScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import kotlinx.coroutines.Dispatchers

private const val TAG = "ProductsViewModel"

class ProductsViewModel(private val repoInterface: IRepository) : ViewModel() {

    fun fetchBrandData(id: Long) {}

}