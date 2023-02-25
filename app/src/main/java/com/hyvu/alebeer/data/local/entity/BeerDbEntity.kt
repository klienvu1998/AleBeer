package com.hyvu.alebeer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hyvu.alebeer.model.BeerItem

@Entity(tableName = "beer")
data class BeerDbEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "price")
    val price: String,

    @ColumnInfo(name = "image_path")
    val imagePath: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "note")
    val note: String
) {

    companion object {
        fun mapData(beerItem: BeerItem, imagePath: String): BeerDbEntity {
            return BeerDbEntity(
                beerItem.id,
                beerItem.name,
                beerItem.price,
                imagePath,
                beerItem.imageUrl,
                beerItem.note
            )
        }
    }

}