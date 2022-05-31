package com.example.websocket.stocklist.data.net

import android.util.Log
import com.example.websocket.stocklist.data.websocket.InfoServerData
import com.example.websocket.stocklist.data.websocket.NetData
import com.example.websocket.stocklist.data.websocket.Quote
import com.example.websocket.stocklist.domain.CommonData
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request

class StockService(
    private val netClient: OkHttpClient,
    private val moshi: Moshi
) {
    private val quoteDataAdapter = moshi.adapter(Quote::class.java)

    fun getInfo(symbol: String): CommonData {
        return try {
            val request = Request.Builder()
                .url("https://finnhub.io/api/v1/quote?symbol=$symbol&token=c9h4ohiad3iblo2fs8vg")
                .build()
            val responseString = netClient.newCall(request).execute().body?.string()
            Log.d("net","$responseString")
            val data = quoteDataAdapter.fromJson(responseString!!)!!
            data.toCommonData(symbol)
        } catch (e: Exception) {
            Log.d("error handle", "${e.message}")
            throw e
        }
    }
}