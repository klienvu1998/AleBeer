package com.hyvu.alebeer.model

import com.hyvu.alebeer.data.remote.entity.BeerData

class BeerItem(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val price: String,
    var localPath: String,
    var note: String,
    var isSaved: Boolean
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
                false
            )
        }
    }

}