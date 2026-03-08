package com.funkycorgi.vulpecula.entry.app.rabbit.controllers

import com.funkycorgi.vulpecula.entry.app.rabbit.config.RabbitExchangeConfiguration

interface IRabbitController {
    val exchangeConfig: RabbitExchangeConfiguration
    suspend fun process()
    fun close()
}
