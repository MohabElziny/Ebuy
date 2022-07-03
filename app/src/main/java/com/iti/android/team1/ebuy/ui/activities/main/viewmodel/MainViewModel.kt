package com.iti.android.team1.ebuy.ui.activities.main.viewmodel

import androidx.lifecycle.ViewModel
import com.iti.android.team1.ebuy.data.data.repository.IRepository

class MainViewModel(private val repo: IRepository) : ViewModel() {
    val cartNo get() = repo.getCartNo()

}