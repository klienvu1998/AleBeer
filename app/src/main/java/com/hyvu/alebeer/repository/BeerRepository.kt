package com.hyvu.alebeer.repository

import android.util.Log
import com.hyvu.alebeer.Injection
import com.hyvu.alebeer.data.local.entity.BeerDbEntity
import com.hyvu.alebeer.data.remote.BaseResponse
import com.hyvu.alebeer.data.remote.entity.BeerData
import com.hyvu.alebeer.data.remote.entity.BeerResponse
import com.hyvu.alebeer.datasource.BeerLocalDataSource
import com.hyvu.alebeer.datasource.BeerRemoteDataSource
import com.hyvu.alebeer.model.BeerItem
import com.hyvu.alebeer.utils.StringUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import kotlin.collections.ArrayList

class BeerRepository(
    private val beerRemoteDataSource: BeerRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher,
    private val beerLocalDataSource: BeerLocalDataSource
) {

    companion object {
        private const val TAG = "BeerRepository"
        private const val BEERS_PAGE_SIZE = 20
    }

    private val beersItemMap: HashMap<Int, BeerItem> = HashMap()

    suspend fun fetchBeers(page: Int): BaseResponse<BeerResponse> = withContext(ioDispatcher) {
        val response = beerRemoteDataSource.fetchBeers(page, BEERS_PAGE_SIZE)
        if (response.isSuccessful) {
            val loadedData = response.body()
            if (loadedData != null) {
                return@withContext BaseResponse.Success(loadedData)
            }
        }
        return@withContext BaseResponse.Error(Exception(response.message()))
    }

    fun getIfExistLocalItem(id: Int): BeerItem? {
        synchronized(beersItemMap) {
            return beersItemMap[id]
        }
    }

    fun getBeerLocal() = beersItemMap.values.filter { it.isSaved }

    suspend fun getBeersFromDb(): List<BeerDbEntity> = withContext(ioDispatcher) {
        val data = beerLocalDataSource.getBeers()
        data.forEach {
            beersItemMap[it.id] = BeerItem.mapData(it, 0L)
        }
        return@withContext data
    }

    suspend fun insertBeersToDb(beer: BeerItem): Boolean = withContext(ioDispatcher) {
        val path = saveImageToInternal(beer)
        val beersDb = BeerDbEntity.mapData(beer, path)
        val insertedId = beerLocalDataSource.insertBeer(beersDb)
        val isInserted = insertedId != -1L
        if (isInserted) {
            beersItemMap[beer.id] = beer
        }
        beer.isSaved = isInserted
        return@withContext isInserted
    }

    private suspend fun saveImageToInternal(beer: BeerItem): String = withContext(Dispatchers.IO) {
        val directory = File(Injection.getAppContext().filesDir, "images")
        if (!directory.exists()) {
            directory.mkdir()
        }

        var inputStream: InputStream? = null
        var os: OutputStream? = null
        var imagePath = ""
        try {
            val imageFile = File(directory, "${beer.id}.${StringUtils.getExtensionOfUrl(beer.imageUrl)}")
            if (imageFile.exists()) imageFile.delete()
            val url = URL(beer.imageUrl)
            inputStream = url.openStream()
            os = FileOutputStream(imageFile)

            val b = ByteArray(2048)
            var length: Int

            while (inputStream.read(b).also { length = it } != -1) {
                os.write(b, 0, length)
            }
            imagePath = imageFile.path
        } catch (e: Exception) {
            imagePath = ""
            Log.e(TAG, e.message.toString())
        } finally {
            inputStream?.close()
            os?.close()
        }
        return@withContext imagePath
    }

    fun addBeerItems(beerItems: ArrayList<BeerItem>) {
        synchronized(this.beersItemMap) {
            beerItems.forEach {
                this.beersItemMap[it.id] = it
            }
        }
    }

    suspend fun deleteBeerFromDb(item: BeerItem): BeerItem? = withContext(ioDispatcher) {
        try {
            val isDeleted = beerLocalDataSource.deleteBeer(item.id) == 1
            if (isDeleted) {
                val file = File(item.localPath)
                if (file.exists()) file.delete()
                val beerItemResult = beersItemMap[item.id]?.apply {
                    isSaved = false
                    note = ""
                }
                return@withContext beerItemResult
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            return@withContext null
        }
    }

    suspend fun updateNoteFromDb(item: BeerItem): BeerItem? = withContext(ioDispatcher) {
        try {
            val isUpdated = beerLocalDataSource.updateNote(item.id, item.note) == 1
            if (isUpdated) {
                beersItemMap[item.id] = item
                return@withContext item
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            return@withContext null
        }
    }

    suspend fun updateBeerInfo(localItem: BeerItem, serverItem: BeerData): BeerItem = withContext(ioDispatcher) {
        try {
            val imagePath = if (localItem.imageUrl != serverItem.image || localItem.imageUrl.isEmpty()) {
                saveImageToInternal(BeerItem.mapData(serverItem, ""))
            } else {
                localItem.localPath
            }
            beerLocalDataSource.updateBeerInfo(serverItem.id, serverItem.name, serverItem.price, serverItem.image, imagePath)
            return@withContext BeerItem(
                localItem.id,
                serverItem.image,
                serverItem.name,
                serverItem.price,
                imagePath,
                localItem.note,
                localItem.isSaved,
                serverItem.sale_off_time
            )
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            return@withContext localItem
        }
    }

    fun clearData() {
        beersItemMap.clear()
    }

}