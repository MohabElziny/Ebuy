package com.iti.android.team1.ebuy.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.data.repository.IRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val repo: IRepository) : ViewModel() {

    fun logOut() = viewModelScope.launch { repo.logOut() }

}