package com.iti.android.team1.ebuy.ui.register_screen

import com.iti.android.team1.ebuy.model.pojo.Customer


sealed class RegisterResult {
    object Loading : RegisterResult()
    data class InvalidData(val error: ErrorType) : RegisterResult()
    class RegisterSuccess(val customer: Customer) : RegisterResult()
    data class RegisterFail(val errorMsg: String) : RegisterResult()
}

enum class ErrorType {
    FirstNameError, LastNameError, EmailError, PasswordError
}