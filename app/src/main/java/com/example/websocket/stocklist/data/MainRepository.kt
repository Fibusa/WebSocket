package com.example.websocket.stocklist.data

import android.util.Log
import com.example.websocket.stocklist.data.websocket.WebSocketDataSource
import com.example.websocket.stocklist.data.cache.CacheDataSource
import com.example.websocket.stocklist.data.net.NetDataSource
import com.example.websocket.stocklist.data.websocket.NetData
import com.example.websocket.stocklist.domain.CommonData
import com.example.websocket.stocklist.domain.MainState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

class MainRepository(
    private val webSocketDataSource: WebSocketDataSource,
    private val cacheDataSource: CacheDataSource,
    private val netDataSource: NetDataSource
) {

    private var lastUpdate: List<CommonData> = listOf()


    suspend fun startObserve(searchBy: String, page: Int) = flow{
        try {
            lastUpdate = cacheDataSource.getDbData(page,searchBy)
            val netUpdate = netDataSource.getInfo(lastUpdate.filter {
                (System.currentTimeMillis()-it.timestamp)/86400000 > 1
            }.map { it.symbol })
            lastUpdate = lastUpdate.filter {
                (System.currentTimeMillis()-it.timestamp)/86400000 <= 1
            } + netUpdate

            emit(MainState.Success(lastUpdate))


            delay(3000)
                webSocketDataSource.startObserve(lastUpdate.map(CommonData::toEvent))
            .collect { serverData ->
                  emit(MainState.Success(updateList(serverData))) }
        } catch (e: Exception) {
            emit(MainState.Error(e.message!!))
        }
    }

    fun stopObserve(){
        CoroutineScope(Dispatchers.IO).launch {
            webSocketDataSource.stopObserve()
            Log.d("exceptions","$lastUpdate")
            cacheDataSource.cacheData(lastUpdate)
        }
    }

    private fun updateList(list: List<NetData>):List<CommonData> {
        for (oldItem in lastUpdate){
            for (newItem in list){
                if(newItem.symbol == oldItem.symbol)
                    oldItem.updateData(newItem)
            }
        }
        return lastUpdate
    }
}