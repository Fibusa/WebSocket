package com.example.websocket.stockinfo.domain

import java.lang.Exception

data class InfoViewData(
    val country: String?,
    val currency: String?,
    val exchange: String?,
    val name: String,
    val logo: String,
    val ticker: String,
    val industry: String?
    )
