package com.iti.android.team1.ebuy.model.datasource.remotesource

import com.iti.android.team1.ebuy.model.pojo.Products
import retrofit2.Response

interface RemoteSource {
    suspend fun getBrandProducts(
        brandID:Long
    ) : Response<Products>
}