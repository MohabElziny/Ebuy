package com.iti.android.team1.ebuy.ui.savedItems.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.DatabaseResult
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class SavedItemsViewModel(private val repo: IRepository) : ViewModel() {

    private val _allFavorites: MutableStateFlow<DatabaseResult<List<FavoriteProduct>>> =
        MutableStateFlow(DatabaseResult.Loading)
    val allFavorites get() = _allFavorites.asStateFlow()

    private val _deleteState: MutableStateFlow<DatabaseResult<Int?>> =
        MutableStateFlow(DatabaseResult.Loading)
    val deleteState get() = _deleteState.asStateFlow()

    fun getFavoritesList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getFlowFavoriteProducts() }
            setAllFavoritesResult(result.await())
        }
    }

    private fun setAllFavoritesResult(result: Flow<List<FavoriteProduct>>) {
        viewModelScope.launch(Dispatchers.IO) {
            result.buffer().collect { data ->
                if (data.isEmpty())
                    _allFavorites.emit(DatabaseResult.Empty)
                else
                    _allFavorites.emit(DatabaseResult.Success(data))
            }
        }

    }

    fun deleteFavoriteProduct(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.deleteProductFromFavorite(productId) }
            setDeleteState(result.await())
        }
    }

    private fun setDeleteState(result: DatabaseResponse<Int?>) {
        when (result) {
            is DatabaseResponse.Failure -> _deleteState.value =
                DatabaseResult.Error(result.errorMsg)
            is DatabaseResponse.Success -> {
                _deleteState.value = DatabaseResult.Empty
            }
        }
    }

}