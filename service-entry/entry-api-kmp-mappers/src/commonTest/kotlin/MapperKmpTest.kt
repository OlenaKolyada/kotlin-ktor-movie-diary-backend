import kotlin.test.Test
import com.funkycorgi.vulpecula.entry.api.kmp.models.*
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.*
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import com.funkycorgi.vulpecula.entry.api.kmp.mappers.*
import com.funkycorgi.vulpecula.entry.stubs.EntryStub
import kotlin.test.assertEquals

class MapperKmpTest {
    @Test
    fun fromTransport() {
        val req = EntryCreateRequest(
            debug = EntryDebug(
                mode = EntryRequestDebugMode.STUB,
                stub = EntryRequestDebugStubs.SUCCESS,
            ),
            entry = EntryStub.get().toTransportCreateEntry()
        )
        val expected = EntryStub.prepareResult {
            id = EntryId.NONE
            userId = UserId.NONE
            lock = EntryLock.NONE
            userPermissions.clear()
        }

        val context = EntryContext()
        context.fromTransport(req)

        assertEquals(EntryStubs.SUCCESS, context.stubCase)
        assertEquals(EntryWorkMode.STUB, context.workMode)
        assertEquals(expected, context.entryRequest)
    }

    @Test
    fun toTransport() {
        val context = EntryContext(
            requestId = EntryRequestId("1234"),
            command = EntryCommand.CREATE,
            entryResponse = EntryStub.get(),
            errors = mutableListOf(
                EntryError(
                    code = "err",
                    group = "request",
                    field = "comment",
                    message = "wrong comment",
                )
            ),
            state = EntryState.RUNNING,
        )

        val req = context.toTransportEntry() as EntryCreateResponse

        assertEquals(req.entry, EntryStub.get().toTransportEntry())
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("comment", req.errors?.firstOrNull()?.field)
        assertEquals("wrong comment", req.errors?.firstOrNull()?.message)
    }
}
