package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.datasource.remotesource.RemoteSource
import com.iti.android.team1.ebuy.model.datasource.remotesource.RetrofitHelper
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse.*
import com.iti.android.team1.ebuy.model.pojo.Brands
import com.iti.android.team1.ebuy.model.pojo.Products
import org.json.JSONObject

class Repository(private val remoteSource: RemoteSource = RetrofitHelper) : IRepository {
    override suspend fun getAllBrands(): NetworkResponse<Brands> {
        val response = remoteSource.getAllBrands()
        return if (response.isSuccessful) {
            SuccessResponse(response.body() ?: Brands(emptyList()))
        } else {
            try {
                val errorMessage = response.errorBody()?.string() ?: "No Error Found"
                val jObjError = JSONObject(errorMessage)
                val messageString: String = jObjError.getString("errors")
                FailureResponse(messageString)
            } catch (e: Exception) {
                FailureResponse(e.message.toString())
            }
        }
    }

    override suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products> {
        val response = remoteSource.getProductsByCollectionID(collectionID)
        return if (response.isSuccessful) {
            SuccessResponse(response.body() ?: Products(emptyList()))
        } else {
            FailureResponse(response.errorBody().toString())
        }
    }
}