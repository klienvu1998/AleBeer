package com.hyvu.alebeer.repository

import com.hyvu.alebeer.data.remote.BaseResponse
import com.hyvu.alebeer.datasource.BeerRemoteDataSource
import com.hyvu.alebeer.data.remote.entity.BeerData
import com.hyvu.alebeer.data.remote.entity.BeerResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BeerRepository(
    private val beerRemoteDataSource: BeerRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) {

    companion object {
        private const val BEERS_PAGE_SIZE = 20
    }

    private val beers: ArrayList<BeerData> = ArrayList()

    suspend fun fetchBeers(page: Int): BaseResponse<BeerResponse> = withContext(ioDispatcher) {
        val response = beerRemoteDataSource.fetchBeers(page, BEERS_PAGE_SIZE)
        if (response.isSuccessful) {
            val loadedData = response.body()
            if (loadedData != null) {
                beers.addAll(loadedData.data)
                return@withContext BaseResponse.Success(loadedData)
            }
        }
        return@withContext BaseResponse.Error(Exception(response.message()))
    }

    fun getBeers(): List<BeerData> = beers


}