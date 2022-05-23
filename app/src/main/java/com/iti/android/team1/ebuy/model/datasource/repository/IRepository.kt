package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.pojo.Products

interface IRepository {
    suspend fun getBrandProducts(brandID:Long) : Products?
}