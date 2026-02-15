package com.funkycorgi.vulpecula.entry.api.jvm

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.funkycorgi.vulpecula.entry.api.jvm.models.IRequest
import com.funkycorgi.vulpecula.entry.api.jvm.models.IResponse

val entryApiJvmSerializer = JsonMapper.builder().run {
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    build()
}

@Suppress("unused")
fun entryApiJvmRequestSerialize(request: IRequest): String =
    entryApiJvmSerializer.writeValueAsString(request)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IRequest> entryApiJvmRequestDeserialize(json: String): T =
    entryApiJvmSerializer.readValue(json, IRequest::class.java) as T

@Suppress("unused")
fun entryApiJvmResponseSerialize(response: IResponse): String =
    entryApiJvmSerializer.writeValueAsString(response)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IResponse> entryApiJvmResponseDeserialize(json: String): T =
    entryApiJvmSerializer.readValue(json, IResponse::class.java) as T
