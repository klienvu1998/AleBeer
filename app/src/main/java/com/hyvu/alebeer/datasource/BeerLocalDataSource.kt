package com.hyvu.alebeer.datasource

import com.hyvu.alebeer.data.local.BeerDao
import com.hyvu.alebeer.data.local.entity.BeerDbEntity

class BeerLocalDataSource(
    private val beerDao: BeerDao
) {

    suspend fun getBeers() = beerDao.getBeers()

    suspend fun insertBeer(beer: BeerDbEntity): Long = beerDao.insertBeer(beer)

}