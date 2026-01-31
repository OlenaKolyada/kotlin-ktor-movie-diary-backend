package scenarios.entry.jvm

import base.client.Client
import com.funkycorgi.vulpecula.entry.api.jvm.models.*
import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import scenarios.entry.jvm.base.sendAndReceive
import scenarios.entry.jvm.base.someCreateEntry
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchEntryJvm(
    private val client: Client,
    private val debug: EntryDebug? = null
) {
    @Test
    fun search() = runBlocking {
        val objs = listOf(
            someCreateEntry,
            someCreateEntry.copy(
                movieId = "movie:tt1375666",
                comment = "Начало - невероятная игра с реальностью"
            ),
            someCreateEntry.copy(
                movieId = "movie:tt1160419",
                comment = "Дюна - эпическая космическая сага"
            )
        ).map { obj ->
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
            cObj
        }

        val sObj = EntrySearchFilter(searchString = "фильм")
        val resSearch = client.sendAndReceive(
            "search",
            EntrySearchRequest(
                requestType = "search",
                debug = debug,
                entryFilter = sObj,
            )
        ) as EntrySearchResponse

        assertEquals(ResponseResult.SUCCESS, resSearch.result)

        val rsObj: List<EntryResponseObject> = resSearch.propertyEntries ?: fail("No entries in Search response")
        val comments = rsObj.map { it.comment }
        assertContains(comments, "Начало - невероятная игра с реальностью")
        assertContains(comments, "Дюна - эпическая космическая сага")

        objs.forEach { obj ->
            val resDelete = client.sendAndReceive(
                "delete", EntryDeleteRequest(
                    requestType = "delete",
                    debug = debug,
                    entry = EntryDeleteObject(obj.id, obj.lock),
                )
            ) as EntryDeleteResponse

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}