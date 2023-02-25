package com.hyvu.alebeer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hyvu.alebeer.data.local.entity.BeerDbEntity

@Database(entities = [BeerDbEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun beerDao(): BeerDao

    companion object {
        private const val DB_NAME = "AleBeer"

        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                this.instance = instance
                instance
            }
        }
    }

}