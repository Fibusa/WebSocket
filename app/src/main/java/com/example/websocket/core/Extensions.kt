package com.example.websocket.core

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException

suspend fun <E> Channel<E>.sendOrNothing(e: E) {
    try {
        this.send(e)
    }
    catch (closedException: ClosedSendChannelException) { }
}