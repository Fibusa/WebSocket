package com.example.websocket.stocklist.data.net

import android.util.Log
import com.example.websocket.stocklist.domain.CommonData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


class NetDataSource(private val stockService: StockService) {

    suspend fun getInfo(symbolList: List<String>)
    = coroutineScope{
            symbolList.map { symbol ->
                async{ stockService.getInfo(symbol) }
            }.map{ it.await() }
    }
}
