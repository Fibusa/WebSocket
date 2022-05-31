package com.example.websocket.stocklist.data.websocket

import com.example.websocket.stockinfo.domain.InfoViewData
import com.example.websocket.stocklist.domain.CommonData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServerData(
    val data: List<Data>?,
    val type: String
)

@JsonClass(generateAdapter = true)
data class Data(
    val c: List<Int>?,
    val p: Double,
    val s: String,
    val t: Long?,
    val v: Double
){
    fun toNetData(): NetData {
        return NetData(s,p)
    }
}


@JsonClass(generateAdapter = true)
data class Event(
    val symbol:String,
    val type: String = "subscribe")

data class NetData(
    val symbol: String,
    val price: Double)


@JsonClass(generateAdapter = true)
data class InfoServerData(
    val country: String?,
    val currency: String?,
    val exchange: String?,
    val finnhubIndustry: String?,
    val ipo: String?,
    val logo: String,
    val marketCapitalization: Double,
    val name: String,
    val phone: String,
    val shareOutstanding: Double,
    val ticker: String,
    val weburl: String
    ){
    fun toInfoViewData() = InfoViewData(
        country,
        currency,
        exchange,
        name,
        logo,
        ticker,
        industry = finnhubIndustry)
}

@JsonClass(generateAdapter = true)
data class Quote(
    val c: Double?,
    val d: Double?,
    val dp: Double?,
    val h: Double?,
    val l: Double?,
    val o: Double?,
    val pc: Double?,
    val t: Long){

    fun toCommonData(s:String) = CommonData(s,c!!,d!!,dp!!,System.currentTimeMillis())
}
