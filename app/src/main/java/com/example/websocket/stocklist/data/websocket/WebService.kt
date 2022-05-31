package com.example.websocket.stocklist.data.websocket

import android.util.Log
import com.example.websocket.stocklist.data.websocket.CustomWebSocketListener.Companion.NORMAL_CLOSURE_STATUS
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class WebService(
    private val socketOkHttpClient: OkHttpClient
) {

    private var _webSocket: WebSocket? = null


    private var _webSocketListener: CustomWebSocketListener? = null


    fun startObserve(list: List<String>): Flow<SocketUpdate> {
        return try {
            val socket = CustomWebSocketListener(list)
            createSocket(socket)
            socket.socketEventChannel.consumeAsFlow()
        }catch (e:Exception){
            Log.d("error handle","${e.message}")
            flowOf(SocketUpdate.Error(e))
        }
    }


    private fun createSocket(webSocketListener: CustomWebSocketListener) {
        _webSocketListener = webSocketListener
        _webSocket = socketOkHttpClient.newWebSocket(
            Request.Builder().url("wss://ws.finnhub.io?token=c9h4ohiad3iblo2fs8vg").build(),
            webSocketListener
        )
    }

    fun stopObserve(){
        try {
            Log.d("flow","stopCollect")
            _webSocket?.close(NORMAL_CLOSURE_STATUS,null)
            _webSocket = null
            _webSocketListener?.socketEventChannel?.close()
            _webSocketListener = null
        } catch (ex: Exception) {
        }
    }

}