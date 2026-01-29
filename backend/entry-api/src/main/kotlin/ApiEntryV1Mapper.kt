package com.funkycorgi.vulpecula.entry.api

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.funkycorgi.vulpecula.entry.api.models.IRequest
import com.funkycorgi.vulpecula.entry.api.models.IResponse

val apiEntryV1Mapper = JsonMapper.builder().run {
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    build()
}

@Suppress("unused")
fun apiEntryV1RequestSerialize(request: IRequest): String = apiEntryV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IRequest> apiEntryV1RequestDeserialize(json: String): T =
    apiEntryV1Mapper.readValue(json, IRequest::class.java) as T

@Suppress("unused")
fun apiEntryV1ResponseSerialize(response: IResponse): String = apiEntryV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IResponse> apiEntryV1ResponseDeserialize(json: String): T =
    apiEntryV1Mapper.readValue(json, IResponse::class.java) as T
