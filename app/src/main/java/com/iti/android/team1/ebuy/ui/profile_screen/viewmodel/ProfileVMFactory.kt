package com.iti.android.team1.ebuy.ui.profile_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.android.team1.ebuy.data.data.repository.IRepository

class ProfileVMFactory(val myRepo:IRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(myRepo = myRepo) as T
    }
}