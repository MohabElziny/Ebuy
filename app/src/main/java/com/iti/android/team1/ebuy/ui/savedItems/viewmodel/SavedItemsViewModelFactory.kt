package com.iti.android.team1.ebuy.ui.savedItems.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.data.data.repository.IRepository

class SavedItemsViewModelFactory(private val repository: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedItemsViewModel::class.java))
            return SavedItemsViewModel(repository) as T
        else
            throw IllegalArgumentException("View Model class Not Found")
    }
}