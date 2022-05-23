package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Brands

interface IRepository {
    suspend fun getAllBrands() : NetworkResponse<Brands?>

}