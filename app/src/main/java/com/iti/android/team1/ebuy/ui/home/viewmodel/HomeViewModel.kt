package com.iti.android.team1.ebuy.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.data.repository.IRepository
import com.iti.android.team1.ebuy.data.factories.NetworkResponse
import com.iti.android.team1.ebuy.data.factories.ResultState
import com.iti.android.team1.ebuy.data.pojo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: IRepository) : ViewModel() {
    private val _brandsResult: MutableStateFlow<ResultState<Brands>> =
        MutableStateFlow(ResultState.Loading)
    val brandsResult: StateFlow<ResultState<Brands>>
        get() = _brandsResult

    private var cachedBrands: Brands? = null

    private val _discountCodeList: MutableStateFlow<ResultState<List<DiscountCodes>>> =
        MutableStateFlow(ResultState.Loading)
    val discountCodeList get() = _discountCodeList.asStateFlow()

    fun getAllDiscount() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                repository.getAllPriceRules()
            }
            setPriceRules(result.await())
        }
    }

    private suspend fun setPriceRules(response: NetworkResponse<PriceRuleResponse>) {
        when (response) {
            is NetworkResponse.FailureResponse -> {
                _discountCodeList.emit(ResultState.Loading)

            }
            is NetworkResponse.SuccessResponse -> {
                if (response.data.priceRules.isEmpty()) {
                    _discountCodeList.emit(ResultState.EmptyResult)
                } else {
                    requestDiscountCodes(response.data.priceRules)
                }
            }
        }
    }

    private suspend fun requestDiscountCodes(priceRules: ArrayList<PriceRules>) {
        _discountCodeList.emit(ResultState.Loading)
        val codeList = mutableListOf<DiscountCodes>()
        priceRules.forEach {

            val result = viewModelScope.async {
                repository.getDiscountCodes(it.id ?: 0)
            }
            updateCodeList(result.await(), codeList)
            if (it == priceRules.last())
                _discountCodeList.emit(ResultState.Success(codeList))

        }
    }


    private suspend fun updateCodeList(
        response: NetworkResponse<Discount>,
        codeList: MutableList<DiscountCodes>,
    ) {
        when (response) {
            is NetworkResponse.FailureResponse -> {
                _discountCodeList.emit(ResultState.Success(codeList))
            }
            is NetworkResponse.SuccessResponse -> {
                if (response.data.discountCodes.isEmpty()) {
                    _discountCodeList.emit((ResultState.Success(codeList)))
                } else {
                    codeList.add(response.data.discountCodes.get(0))
                }
            }
        }
    }

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
        if (newString.isNotEmpty()) {
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
    }

    fun getBrandsAgain() {
        cachedBrands?.let {
            _brandsResult.value = ResultState.Success(it)
        }
    }

    fun onDestroyView() {
        cachedBrands = null
    }
}