package scenarios.entry.kmp

import infrastructure.client.Client
import com.funkycorgi.vulpecula.entry.api.kmp.models.*
import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import scenarios.entry.kmp.base.sendAndReceive
import scenarios.entry.kmp.base.someCreateEntry
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioCreateDeleteEntryKmp(
    private val client: Client,
    private val debug: EntryDebug? = null
) {
    @Test
    fun createDelete() = runBlocking {
        val obj = someCreateEntry
        val resCreate = client.sendAndReceive(
            "create", EntryCreateRequest(
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
            "delete", EntryDeleteRequest(
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