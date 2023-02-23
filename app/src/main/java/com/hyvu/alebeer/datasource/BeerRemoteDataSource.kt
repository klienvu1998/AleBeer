package com.hyvu.alebeer.datasource

import com.hyvu.alebeer.data.remote.BeerApi

class BeerRemoteDataSource(
    private val beerApi: BeerApi
) {

    suspend fun fetchBeers(page: Int, limit: Int) = beerApi.getBeersInformation(page, limit)

}