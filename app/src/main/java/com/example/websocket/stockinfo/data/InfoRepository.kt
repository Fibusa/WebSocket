package com.example.websocket.stockinfo.data

import com.example.websocket.stockinfo.data.net.NetInfoService
import com.example.websocket.stockinfo.domain.ScreenState
import com.example.websocket.stocklist.data.cache.CacheDao
import java.lang.Exception

class InfoRepository(
    private val netRepository: NetInfoService,
    private val cacheRepository: CacheDao
) {

    fun getInfo(symbol: String): ScreenState {
        return try{
            ScreenState.Success(netRepository.getInfo(symbol).toInfoViewData())
        }catch (e:Exception){
            ScreenState.Error(e)
        }
    }

    fun setFavorite(flag: Int, symbol: String){
        cacheRepository.setFavorite(flag,symbol)
    }

    fun getFavorite(symbol: String) = cacheRepository.getFavorite(symbol)
}