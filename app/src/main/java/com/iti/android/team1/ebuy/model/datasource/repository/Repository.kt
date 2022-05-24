package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.datasource.remotesource.RemoteSource
import com.iti.android.team1.ebuy.model.datasource.remotesource.RetrofitHelper
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Products
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse.*
import com.iti.android.team1.ebuy.model.pojo.Brands

class Repository(private val remoteSource: RemoteSource = RetrofitHelper) : IRepository {
    override suspend fun getAllBrands(): NetworkResponse<Brands?> {
        val response = remoteSource.getAllBrands()
        return if (response.isSuccessful) {
            SuccessResponse(response.body())
        } else {
            FailureResponse(response.errorBody().toString())
        }
    }

    override suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products?> {
        val response = remoteSource.getProductsByCollectionID(collectionID)
        return if(response.isSuccessful){
            SuccessResponse(response.body())
        }else{
            FailureResponse(response.errorBody().toString())
        }
    }
}