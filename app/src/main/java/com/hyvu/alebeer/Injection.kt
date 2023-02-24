package com.hyvu.alebeer

import android.content.Context
import com.hyvu.alebeer.data.local.AppDatabase
import com.hyvu.alebeer.data.remote.BeerApi
import com.hyvu.alebeer.datasource.BeerLocalDataSource
import com.hyvu.alebeer.datasource.BeerRemoteDataSource
import com.hyvu.alebeer.repository.BeerRepository
import kotlinx.coroutines.Dispatchers

object Injection {

    private var beerRepo: BeerRepository? = null
    private val LOCK = Any()
    private lateinit var applicationContext: Context

    fun setAppContext(context: Context) {
        this.applicationContext = context
    }

    fun getAppContext() = applicationContext

    fun provideBeerRepo(): BeerRepository {
        if (beerRepo == null) {
            synchronized(LOCK) {
                beerRepo = BeerRepository(
                    BeerRemoteDataSource(BeerApi.create()),
                    Dispatchers.IO,
                    BeerLocalDataSource(AppDatabase.getDatabase(applicationContext).beerDao())
                )
            }
        }
        return beerRepo!!
    }

}