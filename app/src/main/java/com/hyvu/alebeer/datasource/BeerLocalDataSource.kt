package com.hyvu.alebeer.datasource

import com.hyvu.alebeer.data.local.BeerDao
import com.hyvu.alebeer.data.local.entity.BeerDbEntity

class BeerLocalDataSource(
    private val beerDao: BeerDao
) {

    suspend fun getBeers() = beerDao.getBeers()

    suspend fun insertBeer(beer: BeerDbEntity): Long = beerDao.insertBeer(beer)
    suspend fun deleteBeer(id: Int) = beerDao.deleteBeer(id)
    suspend fun updateNote(id: Int, note: String) = beerDao.updateNote(id, note)

}