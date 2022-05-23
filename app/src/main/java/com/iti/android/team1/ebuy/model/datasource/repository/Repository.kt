package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.datasource.remotesource.RemoteSource
import com.iti.android.team1.ebuy.model.datasource.remotesource.RetrofitHelper
import com.iti.android.team1.ebuy.model.pojo.Products

class Repository(private val remoteSource:RemoteSource = RetrofitHelper) : IRepository {
    override suspend fun getBrandProducts(brandID: Long): Products? {
        val response = remoteSource.getBrandProducts(brandID)
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("${response.errorBody()}")
        }
    }
}