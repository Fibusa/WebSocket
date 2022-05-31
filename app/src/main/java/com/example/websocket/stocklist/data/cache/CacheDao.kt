package com.example.websocket.stocklist.data.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import java.sql.Timestamp

@Dao
interface CacheDao {
    @Query("SELECT * FROM stocks " +
            "WHERE (favorites == 1 AND :searchBy == '') OR (description LIKE '%' || :searchBy || '%' " +
            "AND NOT :searchBy == '') " +
            "ORDER BY id " +
            "LIMIT :limit OFFSET :offset")
    fun getFavoriteStocks(limit: Int, offset: Int, searchBy: String): List<CacheDbEntity>

    @Query("UPDATE stocks SET favorites = :favorite WHERE symbol = :name" )
    fun setFavorite(favorite: Int, name: String)

    @Query("UPDATE stocks SET price = :price" +
            ", priceChange = :priceChange" +
            ", priceChangePercent = :priceChangePercent " +
            ", timestamp = :timestamp" +
            " WHERE symbol = :symbol")
    fun cachedData(symbol: String,
                   price: Double,
                   priceChange: Double,
                   priceChangePercent: Double,
                   timestamp: Long)
    @Query("SELECT favorites FROM stocks WHERE symbol = :symbol" )
    fun getFavorite(symbol: String): Int
}