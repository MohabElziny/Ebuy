package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Brands
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitService {
    @GET("smart_collections.json")
    suspend fun getAllBrands() :Response<Brands>
}