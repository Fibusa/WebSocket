package com.example.websocket.stocklist.data.websocket

import android.util.Log
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.*
import java.io.IOException

class WebSocketDataSource(
    private val service: WebService,
    private val moshi: Moshi) {


    private val serverDataAdapter = moshi.adapter(ServerData::class.java)
    private val eventDataAdapter = moshi.adapter(Event::class.java)

    fun startObserve(list: List<Event>): Flow<List<NetData>> {
        Log.d("search","webSocketStarts")
        return service.startObserve(list.map { eventDataAdapter.toJson(it) })
            .transform {
            json -> fromJson(json).data?.let{ data ->
                emit(data.distinctBy { it.s }
                    .map(Data::toNetData)) }
        }
    }

    fun stopObserve(){
        service.stopObserve()
    }

    private fun fromJson(socketUpdate: SocketUpdate): ServerData {
        return when(socketUpdate){
            is SocketUpdate.Data -> serverDataAdapter.fromJson(socketUpdate.data)!!
            is SocketUpdate.Error -> {
                Log.d("exceptions","${socketUpdate.error}")
                throw IOException()}
        }
    }
}





