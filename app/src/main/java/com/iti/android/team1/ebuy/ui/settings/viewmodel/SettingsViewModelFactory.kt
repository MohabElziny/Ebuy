package com.iti.android.team1.ebuy.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.data.data.repository.IRepository

class SettingsViewModelFactory(private val repo: IRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SettingsViewModel(repo) as T

}