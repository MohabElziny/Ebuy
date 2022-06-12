package com.iti.android.team1.ebuy.ui.savedItems.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.domain.savedItems.ISavedItemsUseCase
import com.iti.android.team1.ebuy.domain.savedItems.SavedItemsUseCase
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.DraftOrder
import com.iti.android.team1.ebuy.model.pojo.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavedItemsViewModel(private val repo: IRepository) : ViewModel() {

    private val _allFavorites: MutableStateFlow<ResultState<List<Product>>> =
        MutableStateFlow(ResultState.Loading)
    val allFavorites get() = _allFavorites.asStateFlow()

    private val _deleteState: MutableLiveData<ResultState<Boolean>> = MutableLiveData()
    val deleteState get() = _deleteState as LiveData<ResultState<Boolean>>

    private val savedItemsUseCase: ISavedItemsUseCase
        get() = SavedItemsUseCase(repo)

    fun getFavoritesList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { savedItemsUseCase.getFavoriteItems() }
            setAllFavoritesResult(result.await())
        }
    }

    private fun setAllFavoritesResult(response: NetworkResponse<List<Product>>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (response) {
                is NetworkResponse.FailureResponse -> _allFavorites.emit(ResultState.Error(response.errorString))
                is NetworkResponse.SuccessResponse -> {
                    if (response.data.isNotEmpty())
                        _allFavorites.emit(ResultState.Success(response.data))
                    else
                        _allFavorites.emit(ResultState.EmptyResult)
                }
            }
        }
    }

    fun deleteFavoriteProduct(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.removeFromFavorite(productId) }
            setDeleteState(result.await())
        }
    }

    private fun setDeleteState(result: NetworkResponse<DraftOrder>) {
        when (result) {
            is NetworkResponse.FailureResponse -> _deleteState.postValue(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> {
                if (result.data.lineItems.isNotEmpty())
                    _deleteState.postValue(ResultState.Success(true))
                else
                    _deleteState.postValue(ResultState.EmptyResult)
            }
        }
    }

}