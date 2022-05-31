package com.example.websocket.stocklist.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.websocket.stocklist.domain.CommonData

@Entity(
    tableName = "stocks"
)
data class CacheDbEntity(
    @PrimaryKey val id: Int,
    val symbol: String,
    val description: String,
    @ColumnInfo(defaultValue = "0") val  favorites: Int,
    @ColumnInfo(defaultValue = "1.0")val price: Double,
    @ColumnInfo(defaultValue = "1.0")val priceChange: Double,
    @ColumnInfo(defaultValue = "1.0")val priceChangePercent: Double,
    @ColumnInfo(defaultValue = "0") val timestamp: Long
){
    fun toCommonData():CommonData =
        CommonData(symbol,price,priceChange,priceChangePercent,timestamp)
}