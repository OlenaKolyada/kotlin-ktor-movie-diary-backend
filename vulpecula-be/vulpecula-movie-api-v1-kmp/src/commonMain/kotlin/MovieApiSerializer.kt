@file:Suppress("unused")

package com.funkycorgi.vulpecula.movie.api.v1

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import com.funkycorgi.vulpecula.movie.api.v1.models.IRequest
import com.funkycorgi.vulpecula.movie.api.v1.models.IResponse

@OptIn(ExperimentalSerializationApi::class)
@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val apiMovieV1Mapper = Json {
    allowTrailingComma = true
}

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiMovieV1RequestDeserialize(json: String) =
    apiMovieV1Mapper.decodeFromString<IRequest>(json) as T

fun apiMovieV1ResponseSerialize(obj: IResponse): String =
    apiMovieV1Mapper.encodeToString(IResponse.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiMovieV1ResponseDeserialize(json: String) =
    apiMovieV1Mapper.decodeFromString<IResponse>(json) as T

inline fun <reified T : IResponse> apiMovieV1ResponseSimpleDeserialize(json: String) =
    apiMovieV1Mapper.decodeFromString<T>(json)

@Suppress("unused")
fun apiMovieV1RequestSerialize(obj: IRequest): String =
    apiMovieV1Mapper.encodeToString(IRequest.serializer(), obj)

@Suppress("unused")
inline fun <reified T : IRequest> apiMovieV1RequestSimpleSerialize(obj: T): String =
    apiMovieV1Mapper.encodeToString<T>(obj)