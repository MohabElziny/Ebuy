package com.iti.android.team1.ebuy.ui.profile_screen.viewmodel

import androidx.lifecycle.ViewModel
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.CustomerRegister
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    private var _customer: MutableStateFlow<ResultState<CustomerRegister>> =
        MutableStateFlow(ResultState.Loading)
    val customer = _customer.asStateFlow()

    fun getCustomerInfo(customer_ID :Long) {

    }
}