package com.iti.android.team1.ebuy.model.factories


sealed class DatabaseResponse<out R> {
    data class Success<out T>(val data: T) : DatabaseResponse<T>()
    data class Failure(val errorMsg: String) : DatabaseResponse<Nothing>()
}

sealed class DatabaseResult<out R> {
    object Loading : DatabaseResult<Nothing>()
    object Empty : DatabaseResult<Nothing>()
    data class Error(val errorMsg: String) : DatabaseResult<Nothing>()
    data class Success<out T>(val data: T) : DatabaseResult<T>()
}