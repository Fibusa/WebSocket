package com.example.websocket.stocklist.domain

sealed class MainState {

    object Load: MainState()

    data class Success(val data: List<CommonData>): MainState()

    data class Error(val e: String): MainState()
}