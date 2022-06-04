package com.iti.android.team1.ebuy.ui.register_screen

import com.iti.android.team1.ebuy.model.pojo.Customer


sealed class AuthResult {
    object Loading : AuthResult()
    data class InvalidData(val error: ErrorType) : AuthResult()
    class RegisterSuccess(val customer: Customer) : AuthResult()
    data class RegisterFail(val errorMsg: String) : AuthResult()
}

enum class ErrorType {
    FirstNameError, LastNameError, EmailError, PasswordError
}