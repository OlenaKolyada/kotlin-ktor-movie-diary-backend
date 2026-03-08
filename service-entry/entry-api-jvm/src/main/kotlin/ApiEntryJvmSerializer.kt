package com.funkycorgi.vulpecula.entry.api.jvm

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryCreateRequest
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryCreateResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryDeleteRequest
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryDeleteResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryInitResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryReadRequest
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryReadResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntrySearchRequest
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntrySearchResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryUpdateRequest
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryUpdateResponse
import com.funkycorgi.vulpecula.entry.api.jvm.models.IRequest
import com.funkycorgi.vulpecula.entry.api.jvm.models.IResponse

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "responseType", visible = true)
@JsonSubTypes(
    JsonSubTypes.Type(value = EntryCreateResponse::class, name = "create"),
    JsonSubTypes.Type(value = EntryDeleteResponse::class, name = "delete"),
    JsonSubTypes.Type(value = EntryInitResponse::class, name = "init"),
    JsonSubTypes.Type(value = EntryReadResponse::class, name = "read"),
    JsonSubTypes.Type(value = EntrySearchResponse::class, name = "search"),
    JsonSubTypes.Type(value = EntryUpdateResponse::class, name = "update"),
)
private interface IResponseMixIn

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "requestType", visible = true)
@JsonSubTypes(
    JsonSubTypes.Type(value = EntryCreateRequest::class, name = "create"),
    JsonSubTypes.Type(value = EntryReadRequest::class, name = "read"),
    JsonSubTypes.Type(value = EntryUpdateRequest::class, name = "update"),
    JsonSubTypes.Type(value = EntryDeleteRequest::class, name = "delete"),
    JsonSubTypes.Type(value = EntrySearchRequest::class, name = "search"),
)
private interface IRequestMixIn

val entryApiJvmSerializer = JsonMapper.builder().run {
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    serializationInclusion(JsonInclude.Include.NON_NULL)
    build()
}.apply {
    addMixIn(IResponse::class.java, IResponseMixIn::class.java)
    addMixIn(IRequest::class.java, IRequestMixIn::class.java)
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
