package scenarios.entry.kmp.base

import base.client.Client
import co.touchlab.kermit.Logger
import com.funkycorgi.vulpecula.entry.api.kmp.entryApiKmpRequestSerialize
import com.funkycorgi.vulpecula.entry.api.kmp.entryApiKmpResponseDeserialize
import com.funkycorgi.vulpecula.entry.api.kmp.models.IRequest
import com.funkycorgi.vulpecula.entry.api.kmp.models.IResponse

private val log = Logger

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = entryApiKmpRequestSerialize(request)
    log.w { "Send to entry/kmp/$path\n$requestBody" }

    val responseBody = sendAndReceive("entry/kmp", path, requestBody)
    log.w { "Received\n$responseBody" }

    return entryApiKmpResponseDeserialize(responseBody)
}
