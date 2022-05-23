package com.iti.android.team1.ebuy.model.datasource.repository

import com.iti.android.team1.ebuy.model.datasource.remotesource.RemoteSource
import com.iti.android.team1.ebuy.model.datasource.remotesource.RetrofitHelper

class Repository(private val remoteSource:RemoteSource = RetrofitHelper) : IRepository {
}