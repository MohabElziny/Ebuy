package com.iti.android.team1.ebuy.model.datasource.localsource

import androidx.room.Database
import androidx.room.TypeConverters
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct

@Database(entities = [FavoriteProduct::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class Database {

}