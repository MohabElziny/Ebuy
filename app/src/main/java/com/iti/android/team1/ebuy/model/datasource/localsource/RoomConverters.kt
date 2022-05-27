package com.iti.android.team1.ebuy.model.datasource.localsource

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct

class RoomConverters {

    @TypeConverter
    fun fromFavoriteToJson(value: FavoriteProduct) = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToFavorite(value: String) = Gson().fromJson(value, FavoriteProduct::class.java)


}