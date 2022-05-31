package com.example.websocket.stocklist.data.cache

import android.util.Log
import com.example.websocket.stocklist.domain.CommonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CacheDataSource(
    private val cacheDao: CacheDao
) {

    fun getDbData(page: Int, searchBy: String):List<CommonData> {
        val offset = page * PAGE_SIZE
        val data =  cacheDao.getFavoriteStocks(PAGE_SIZE, offset,searchBy).map(CacheDbEntity::toCommonData)
        Log.d("float","${data.size}")
        return data
    }

    fun cacheData(list: List<CommonData>){
        Log.d("net","cached")
        list.forEach { cacheDao.cachedData(
            symbol = it.symbol,
            price = it.price,
            priceChange = it.priceChange,
            priceChangePercent = it.priceChangePercent,
            timestamp = System.currentTimeMillis()) }
    }

    companion object {
        const val PAGE_SIZE = 6
    }
}