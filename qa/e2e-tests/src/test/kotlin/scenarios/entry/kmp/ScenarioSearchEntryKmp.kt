package scenarios.entry.kmp

import infrastructure.client.Client
import com.funkycorgi.vulpecula.entry.api.kmp.models.*
import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import scenarios.entry.kmp.base.sendAndReceive
import scenarios.entry.kmp.base.someCreateEntry
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchEntryKmp(
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
                    debug = debug,
                    entry = EntryDeleteObject(obj.id, obj.lock),
                )
            ) as EntryDeleteResponse

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}