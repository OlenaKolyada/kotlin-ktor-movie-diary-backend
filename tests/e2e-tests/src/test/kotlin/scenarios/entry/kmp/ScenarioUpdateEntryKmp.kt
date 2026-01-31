package scenarios.entry.kmp

import base.client.Client
import com.funkycorgi.vulpecula.entry.api.kmp.models.*
import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import scenarios.entry.kmp.base.sendAndReceive
import scenarios.entry.kmp.base.someCreateEntry
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioUpdateEntryKmp(
    private val client: Client,
    private val debug: EntryDebug? = null
) {
    @Test
    fun update() = runBlocking {
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

        val uObj = EntryUpdateObject(
            id = cObj.id,
            lock = cObj.lock,
            movieId = cObj.movieId,
            viewingDate = cObj.viewingDate,
            rating = 8,
            comment = "Обновлённый комментарий"
        )

        val resUpdate = client.sendAndReceive(
            "update",
            EntryUpdateRequest(
                debug = debug,
                entry = uObj,
            )
        ) as EntryUpdateResponse

        assertEquals(ResponseResult.SUCCESS, resUpdate.result)

        val ruObj: EntryResponseObject = resUpdate.entry ?: fail("No entry in Update response")
        assertEquals(obj.movieId, ruObj.movieId)
        assertEquals(obj.viewingDate, ruObj.viewingDate)
        assertEquals(obj.rating, ruObj.rating)
        assertEquals(obj.comment, ruObj.comment)

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