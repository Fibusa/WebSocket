package com.example.websocket

import android.app.Application
import androidx.room.Room
import com.example.websocket.stockinfo.data.InfoRepository
import com.example.websocket.stockinfo.data.net.NetInfoService
import com.example.websocket.stockinfo.domain.InfoViewModel
import com.example.websocket.stocklist.data.MainRepository
import com.example.websocket.stocklist.data.cache.AppDatabase
import com.example.websocket.stocklist.data.cache.CacheDataSource
import com.example.websocket.stocklist.data.net.NetDataSource
import com.example.websocket.stocklist.data.net.StockService
import com.example.websocket.stocklist.data.websocket.WebSocketDataSource
import com.example.websocket.stocklist.data.websocket.WebService
import com.example.websocket.stocklist.domain.MainViewModel
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient

class StockApplication: Application() {

    lateinit var mainViewModel: MainViewModel

    lateinit var infoViewModel: InfoViewModel

    override fun onCreate() {

        val okHttpClient = OkHttpClient.Builder().build()
        val room = Room.databaseBuilder(this, AppDatabase::class.java, "database.db")
            .createFromAsset("stock_database.db").build()

        val moshi = Moshi.Builder().build()

        val webSocketDataSource = WebSocketDataSource(WebService(okHttpClient),moshi)

        val cacheDataSource = CacheDataSource(room.getUsersDao())

        val netDataSource = NetDataSource(StockService(okHttpClient,moshi))

        val repository = MainRepository(webSocketDataSource,cacheDataSource,netDataSource)
        mainViewModel = MainViewModel(repository)

        infoViewModel = InfoViewModel(
            InfoRepository(
                NetInfoService(okHttpClient,moshi),room.getUsersDao()))
        super.onCreate()
    }
}