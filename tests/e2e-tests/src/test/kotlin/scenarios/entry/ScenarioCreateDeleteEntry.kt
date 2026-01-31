package scenarios.entry

import base.client.Client
import com.funkycorgi.vulpecula.entry.api.models.EntryCreateRequest
import com.funkycorgi.vulpecula.entry.api.models.EntryCreateResponse
import com.funkycorgi.vulpecula.entry.api.models.EntryDebug
import com.funkycorgi.vulpecula.entry.api.models.EntryDeleteObject
import com.funkycorgi.vulpecula.entry.api.models.EntryDeleteRequest
import com.funkycorgi.vulpecula.entry.api.models.EntryDeleteResponse
import com.funkycorgi.vulpecula.entry.api.models.EntryResponseObject
import com.funkycorgi.vulpecula.entry.api.models.ResponseResult
import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import scenarios.entry.base.sendAndReceive
import scenarios.entry.base.someCreateEntry
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioCreateDeleteEntry(
    private val client: Client,
    private val debug: EntryDebug? = null
) {
    @Test
    fun createDelete() = runBlocking {
        val obj = someCreateEntry
        val resCreate = client.sendAndReceive(
            "entry/create", EntryCreateRequest(
                requestType = "create",
                debug = debug,
                entry = obj,
            )
        ) as EntryCreateResponse

        assertEquals(ResponseResult.SUCCESS, resCreate.result)

        val cObj: EntryResponseObject = resCreate.entry ?: fail("No entry in Create response")
        assertEquals(obj.movieId, cObj.movieId)
        assertEquals(obj.viewingDate, cObj.viewingDate)
        assertEquals(obj.rating, cObj.rating)
        assertEquals(obj.comment, cObj.comment)

        val resDelete = client.sendAndReceive(
            "entry/delete", EntryDeleteRequest(
                requestType = "delete",
                debug = debug,
                entry = EntryDeleteObject(cObj.id, cObj.lock),
            )
        ) as EntryDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)

        val dObj: EntryResponseObject = resDelete.entry ?: fail("No entry in Delete response")
        assertEquals(obj.movieId, dObj.movieId)
        assertEquals(obj.viewingDate, dObj.viewingDate)
        assertEquals(obj.rating, dObj.rating)
        assertEquals(obj.comment, dObj.comment)
    }
}