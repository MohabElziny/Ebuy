package com.iti.android.team1.ebuy.ui.savedItems.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.DatabaseResult
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavedItemsViewModel(private val repo: IRepository) : ViewModel() {

    private val _allFavorites: MutableStateFlow<DatabaseResult<List<FavoriteProduct>>> =
        MutableStateFlow(DatabaseResult.Loading)
    val allFavorites get() = _allFavorites.asStateFlow()

    private val _updateState: MutableStateFlow<DatabaseResult<Int>> =
        MutableStateFlow(DatabaseResult.Loading)
    val updateState get() = _updateState.asStateFlow()

    fun getFavoritesList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getAllFavoritesProducts() }
            setAllFavoritesResult(result.await())
        }
    }

    fun updateFavoriteProduct(favoriteProduct: FavoriteProduct) {
        if (favoriteProduct.noOfSavedItems in 1..10)
            viewModelScope.launch(Dispatchers.IO) {
                val result = async { repo.updateFavoriteProduct(favoriteProduct) }
                setUpdateState(result.await())
            }
        else
            if (favoriteProduct.noOfSavedItems > 10)
                setUpdateState(DatabaseResponse.Failure("The maximum is 10"))
            else if (favoriteProduct.noOfSavedItems < 1)
                setUpdateState(DatabaseResponse.Failure("The minimum is 1"))
    }

    private fun setUpdateState(result: DatabaseResponse<Int>) {
        when (result) {
            is DatabaseResponse.Failure -> _updateState.value =
                DatabaseResult.Error(result.errorMsg)
            is DatabaseResponse.Success -> _updateState.value = DatabaseResult.Empty
        }
    }


    private fun setAllFavoritesResult(result: List<FavoriteProduct>) {
        if (result.isEmpty())
            _allFavorites.value = DatabaseResult.Empty
        else
            _allFavorites.value = DatabaseResult.Success(data = result)
    }

}