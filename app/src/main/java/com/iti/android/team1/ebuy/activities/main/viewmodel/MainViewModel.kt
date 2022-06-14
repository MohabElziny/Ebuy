package com.iti.android.team1.ebuy.activities.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.data.repository.IRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repo: IRepository) : ViewModel() {
    private val _carNo: MutableStateFlow<Int> = MutableStateFlow(0)
    val cartNo get() = _carNo.asStateFlow()

    fun getCartNo() =
        viewModelScope.launch(Dispatchers.IO) { getCartNoState() }

    private suspend fun getCartNoState() =
        _carNo.emit(repo.getCartNo())

}