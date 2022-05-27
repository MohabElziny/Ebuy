package com.iti.android.team1.ebuy.model.datasource.localsource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct

@Database(entities = [FavoriteProduct::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class CommerceDatabase : RoomDatabase() {
    companion object {
        @Volatile
        var Instance: CommerceDatabase? = null
        fun getDataBase(context: Context): CommerceDatabase {
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    CommerceDatabase::class.java,
                    "commerceDB").build()
                Instance = instance
                instance
            }
        }

    }


    abstract fun favoritesDao(): FavoritesDao
}