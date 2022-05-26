package com.iti.android.team1.ebuy.model.networkresponse

// from network to view model
sealed class NetworkResponse<out R> {
    data class SuccessResponse<out T>(val data: T) : NetworkResponse<T>()
    data class FailureResponse(val errorString: String) : NetworkResponse<Nothing>()
}

// from view model to ui
sealed class ResultState<out R> {
    object Loading : ResultState<Nothing>()
    object EmptyResult : ResultState<Nothing>()
    data class Error(val errorString: String) : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
}