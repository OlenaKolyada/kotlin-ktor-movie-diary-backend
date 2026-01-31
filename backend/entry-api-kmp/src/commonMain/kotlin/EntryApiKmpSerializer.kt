@file:Suppress("unused")

package com.funkycorgi.vulpecula.entry.api.kmp

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import com.funkycorgi.vulpecula.entry.api.kmp.models.IRequest
import com.funkycorgi.vulpecula.entry.api.kmp.models.IResponse

@OptIn(ExperimentalSerializationApi::class)
@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val entryApiKmpMapper = Json {
    allowTrailingComma = true
}

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> entryApiKmpRequestDeserialize(json: String) =
    entryApiKmpMapper.decodeFromString<IRequest>(json) as T

fun entryApiKmpResponseSerialize(obj: IResponse): String =
    entryApiKmpMapper.encodeToString(IResponse.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> entryApiKmpResponseDeserialize(json: String) =
    entryApiKmpMapper.decodeFromString<IResponse>(json) as T

inline fun <reified T : IResponse> entryApiKmpResponseSimpleDeserialize(json: String) =
    entryApiKmpMapper.decodeFromString<T>(json)

@Suppress("unused")
fun entryApiKmpRequestSerialize(obj: IRequest): String =
    entryApiKmpMapper.encodeToString(IRequest.serializer(), obj)

@Suppress("unused")
inline fun <reified T : IRequest> entryApiKmpRequestSimpleSerialize(obj: T): String =
    entryApiKmpMapper.encodeToString<T>(obj)