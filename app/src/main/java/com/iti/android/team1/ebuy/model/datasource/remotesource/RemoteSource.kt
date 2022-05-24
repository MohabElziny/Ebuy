package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.Products
import retrofit2.Response
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Brands

interface RemoteSource {
    suspend fun getBrandProducts(
        brandID:Long
    ) : Response<Products>

    suspend fun getAllBrands() : Response<Brands>
}