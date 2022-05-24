package com.iti.android.team1.ebuy.model.networkresponse

sealed class NetworkResponse<out R> {
    data class SuccessResponse<out T>(val data: T?) :NetworkResponse<T>()
    data class FailureResponse(val errorString: String?):NetworkResponse<Nothing>()
}