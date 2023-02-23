package com.hyvu.alebeer

import com.hyvu.alebeer.data.remote.BeerApi
import com.hyvu.alebeer.datasource.BeerRemoteDataSource
import com.hyvu.alebeer.repository.BeerRepository
import kotlinx.coroutines.Dispatchers

object Injection {

    private var beerRepo: BeerRepository? = null
    private val LOCK = Any()

    fun provideBeerRepo(): BeerRepository {
        if (beerRepo == null) {
            synchronized(LOCK) {
                beerRepo = BeerRepository(
                    BeerRemoteDataSource(BeerApi.create()),
                    Dispatchers.IO
                )
            }
        }
        return beerRepo!!
    }

}