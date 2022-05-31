package com.example.websocket.stocklist.domain

import com.example.websocket.stocklist.data.websocket.Event
import com.example.websocket.stocklist.data.websocket.NetData
import java.sql.Timestamp

data class CommonData(
    var symbol: String,
    var price: Double,
    var priceChange: Double,
    var priceChangePercent: Double,
    var timestamp: Long
    ){

    fun toEvent(): Event {
        return Event(symbol = symbol)
    }

    fun updateData(newData: NetData){
        priceChange += price - newData.price
        priceChangePercent += priceChange/price
        price = newData.price
    }
}
