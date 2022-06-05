package com.iti.android.team1.ebuy.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Brands
import com.iti.android.team1.ebuy.model.pojo.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: IRepository) : ViewModel() {
    private val _brandsResult: MutableStateFlow<ResultState<Brands>> =
        MutableStateFlow(ResultState.Loading)
    val brandsResult: StateFlow<ResultState<Brands>>
        get() = _brandsResult

    private var cachedBrands: Brands? = null

    fun getAllBrands() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                repository.getAllBrands()
            }
            setBrandsResult(result.await())
        }
    }

    private fun setBrandsResult(networkResponse: NetworkResponse<Brands>) {
        when (networkResponse) {
            is NetworkResponse.FailureResponse -> {
                _brandsResult.value = ResultState.Error(networkResponse.errorString)
            }
            is NetworkResponse.SuccessResponse -> {
                networkResponse.data.let {
                    if (it.brands.isNotEmpty()) {
                        cachedBrands = it
                        _brandsResult.value = ResultState.Success(it)
                    } else {
                        _brandsResult.value = ResultState.EmptyResult
                    }
                }
            }
        }
    }

    fun setSearchQuery(newString: String) {
        _brandsResult.value = ResultState.Loading
        val searchBrands = cachedBrands?.brands ?: emptyList()
        viewModelScope.launch(Dispatchers.Default) {
            if (searchBrands.isNotEmpty()) {
                searchBrands.filter { brand ->
                    brand.brandTitle.lowercase().contains(newString.lowercase())
                }.apply {
                    if (this.isNotEmpty())
                        _brandsResult.emit(ResultState.Success(Brands(this)))
                    else
                        _brandsResult.emit(ResultState.EmptyResult)
                }
            }
        }
    }

    fun getBrandsAgain() {
        cachedBrands?.let {
            _brandsResult.value = ResultState.Success(it)
        }
    }
}