package scenarios.entry.jvm

import infrastructure.client.Client
import com.funkycorgi.vulpecula.entry.api.jvm.models.*
import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import scenarios.entry.jvm.base.sendAndReceive
import scenarios.entry.jvm.base.someCreateEntry
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioReadEntryJvm(
    private val client: Client,
    private val debug: EntryDebug? = null
) {
    @Test
    fun read() = runBlocking {
        val obj = someCreateEntry
        val resCreate = client.sendAndReceive(
            "create", EntryCreateRequest(
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

        val rObj = EntryReadObject(
            id = cObj.id,
        )
        val resRead = client.sendAndReceive(
            "read",
            EntryReadRequest(
                requestType = "read",
                debug = debug,
                entry = rObj,
            )
        ) as EntryReadResponse

        assertEquals(ResponseResult.SUCCESS, resRead.result)

        val rrObj: EntryResponseObject = resRead.entry ?: fail("No entry in Read response")
        assertEquals(obj.movieId, rrObj.movieId)
        assertEquals(obj.viewingDate, rrObj.viewingDate)
        assertEquals(obj.rating, rrObj.rating)
        assertEquals(obj.comment, rrObj.comment)

        val resDelete = client.sendAndReceive(
            "delete", EntryDeleteRequest(
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