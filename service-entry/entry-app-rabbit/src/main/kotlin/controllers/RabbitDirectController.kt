package com.funkycorgi.vulpecula.entry.app.rabbit.controllers

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import com.funkycorgi.vulpecula.entry.api.jvm.entryApiJvmSerializer
import com.funkycorgi.vulpecula.entry.api.jvm.models.IRequest
import com.funkycorgi.vulpecula.entry.app.common.controllerHelper
import com.funkycorgi.vulpecula.entry.app.rabbit.config.EntryAppRabbitSettings
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.helpers.asEntryError
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.api.jvm.mappers.fromTransport
import com.funkycorgi.vulpecula.entry.api.jvm.mappers.toTransportEntry

// наследник RabbitProcessorBase, увязывает транспортную и бизнес-части
class RabbitDirectController(
    private val appSettings: EntryAppRabbitSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfig,
) {
    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = entryApiJvmSerializer.readValue(message.body, IRequest::class.java)
                fromTransport(req)
            },
            {
                val res = toTransportEntry()
                entryApiJvmSerializer.writeValueAsBytes(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
                }
            }
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = EntryContext()
        e.printStackTrace()
        context.state = EntryState.FAILING
        context.errors.add(e.asEntryError())
        val response = context.toTransportEntry()
        entryApiJvmSerializer.writeValueAsBytes(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
        }
    }
}
