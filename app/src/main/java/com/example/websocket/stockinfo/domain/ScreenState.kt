package com.example.websocket.stockinfo.domain

sealed class ScreenState {

    object Load: ScreenState()

    data class Error(val e: Throwable): ScreenState()

    data class Success(val data: InfoViewData): ScreenState()
}