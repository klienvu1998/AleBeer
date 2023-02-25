package com.hyvu.alebeer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hyvu.alebeer.data.local.entity.BeerDbEntity

@Dao
interface BeerDao {
    @Query("SELECT * FROM beer")
    suspend fun getBeers(): List<BeerDbEntity>

    @Query("SELECT * FROM beer WHERE id = :id")
    fun getBeerById(id: String): BeerDbEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeer(beer: BeerDbEntity): Long

    @Query("DELETE FROM beer WHERE id = :id")
    suspend fun deleteBeer(id: Int): Int

    @Query("UPDATE beer SET note = :note WHERE id = :id")
    suspend fun updateNote(id: Int, note: String): Int
}