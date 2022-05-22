package com.iti.android.team1.ebuy.dataSource.remoteSource

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://c48655414af1ada2cd256a6b5ee391be:" +
        "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@mobile-ismailia.myshopify.com/admin/api/2022-04/"

private val gson = Gson()

private val retrofit = Retrofit.Builder().apply {
    addConverterFactory(GsonConverterFactory.create())
    this.baseUrl(baseUrl)
}.build()

object RetrofitHelper {
    val retrofitService by lazy { retrofit.create(RetrofitService::class.java)}
}