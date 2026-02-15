package scenarios.entry.jvm.base

import base.client.Client
import co.touchlab.kermit.Logger
import com.funkycorgi.vulpecula.entry.api.jvm.entryApiJvmRequestSerialize
import com.funkycorgi.vulpecula.entry.api.jvm.entryApiJvmResponseDeserialize
import com.funkycorgi.vulpecula.entry.api.jvm.models.IRequest
import com.funkycorgi.vulpecula.entry.api.jvm.models.IResponse


private val log = Logger

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = entryApiJvmRequestSerialize(request)
    log.i { "Send to entry/jvm/$path\n$requestBody" }

    val responseBody = sendAndReceive("entry/jvm", path, requestBody)
    log.i { "Received\n$responseBody" }

    return entryApiJvmResponseDeserialize(responseBody)
}
