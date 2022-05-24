package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Brands
import retrofit2.Response

interface RemoteSource {
    suspend fun getAllBrands() : Response<Brands>

}