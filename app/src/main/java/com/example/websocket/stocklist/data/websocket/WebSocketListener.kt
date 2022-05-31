package com.example.websocket.stocklist.data.websocket

import android.os.Message
import android.util.Log
import com.example.websocket.core.SocketAbortedException
import com.example.websocket.core.sendOrNothing
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class CustomWebSocketListener(
    private val list: List<String>
) : WebSocketListener() {

    val socketEventChannel: Channel<SocketUpdate> = Channel(1)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        list.forEach { observable -> webSocket.send(observable) }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("flow",text)
            socketEventChannel.sendOrNothing(SocketUpdate.Data(text))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        CoroutineScope(Dispatchers.IO).launch {
            socketEventChannel.sendOrNothing(SocketUpdate.Error(SocketAbortedException()))
        }
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        CoroutineScope(Dispatchers.IO).launch {
        }
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}


sealed class SocketUpdate{
    class Data(val data: String): SocketUpdate()
    class Error(val error: Throwable): SocketUpdate()
}

