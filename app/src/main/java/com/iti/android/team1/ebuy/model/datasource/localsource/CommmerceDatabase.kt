package com.iti.android.team1.ebuy.model.datasource.localsource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iti.android.team1.ebuy.model.pojo.CartItem
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct

@Database(entities = [FavoriteProduct::class, CartItem::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class CommerceDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: CommerceDatabase? = null
        fun getDataBase(context: Context): CommerceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    CommerceDatabase::class.java,
                    "commerceDB").build()
                INSTANCE = instance
                instance
            }
        }

    }

    abstract fun cartDao(): CartDao
    abstract fun favoritesDao(): FavoritesDao
}