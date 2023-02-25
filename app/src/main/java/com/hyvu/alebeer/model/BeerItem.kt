package com.hyvu.alebeer.model

import com.hyvu.alebeer.data.local.entity.BeerDbEntity
import com.hyvu.alebeer.data.remote.entity.BeerData

class BeerItem(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val price: String,
    var localPath: String,
    var note: String,
    var isSaved: Boolean,
    var saveOffTime: Long
) {

    companion object {
        fun mapData(beerData: BeerData, localPath: String): BeerItem {
            return BeerItem(
                beerData.id,
                beerData.image,
                beerData.name,
                beerData.price,
                localPath,
                "",
                false,
                beerData.sale_off_time
            )
        }

        fun mapData(beerDbEntity: BeerDbEntity, saveOffTime: Long): BeerItem {
            return BeerItem(
                beerDbEntity.id,
                beerDbEntity.imageUrl,
                beerDbEntity.name,
                beerDbEntity.price,
                beerDbEntity.imagePath,
                beerDbEntity.note,
                true,
                saveOffTime
            )
        }
    }

}