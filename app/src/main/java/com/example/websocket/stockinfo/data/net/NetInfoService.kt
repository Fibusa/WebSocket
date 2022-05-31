package com.example.websocket.stockinfo.data.net

import android.util.Log
import com.example.websocket.stocklist.data.websocket.InfoServerData
import com.squareup.moshi.Moshi
import okhttp3.*

class NetInfoService(
    private val netClient: OkHttpClient,
    private val moshi: Moshi
) {
    private val infoJsonAdapter = moshi.adapter(InfoServerData::class.java)

    fun getInfo(symbol: String): InfoServerData {
        return try {
            val request = Request.Builder()
                .url("https://finnhub.io/api/v1/stock/profile2?symbol=$symbol&token=c9h4ohiad3iblo2fs8vg")
                .build()
            val responseString = netClient.newCall(request).execute().body?.string()
            infoJsonAdapter.fromJson(responseString!!)!!
        } catch (e: Exception) {
            Log.d("error handle", "${e.message}")
            throw e
        }
    }
}