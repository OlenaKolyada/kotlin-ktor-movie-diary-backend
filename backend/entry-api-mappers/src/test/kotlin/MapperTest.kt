import org.junit.Test
import com.funkycorgi.vulpecula.entry.api.models.EntryCreateRequest
import com.funkycorgi.vulpecula.entry.api.models.EntryCreateResponse
import com.funkycorgi.vulpecula.entry.api.models.EntryDebug
import com.funkycorgi.vulpecula.entry.api.models.EntryRequestDebugMode
import com.funkycorgi.vulpecula.entry.api.models.EntryRequestDebugStubs
import com.funkycorgi.vulpecula.entry.common.EntryContext
import com.funkycorgi.vulpecula.entry.common.models.EntryId
import com.funkycorgi.vulpecula.entry.common.models.EntryLock
import com.funkycorgi.vulpecula.entry.common.models.EntryCommand
import com.funkycorgi.vulpecula.entry.common.models.EntryError
import com.funkycorgi.vulpecula.entry.common.models.EntryRequestId
import com.funkycorgi.vulpecula.entry.common.models.EntryState
import com.funkycorgi.vulpecula.entry.common.models.UserId
import com.funkycorgi.vulpecula.entry.common.models.EntryWorkMode
import com.funkycorgi.vulpecula.entry.common.stubs.EntryStubs
import com.funkycorgi.vulpecula.entry.mappers.fromTransport
import com.funkycorgi.vulpecula.entry.mappers.toTransportEntry
import com.funkycorgi.vulpecula.entry.mappers.toTransportCreateEntry
import com.funkycorgi.vulpecula.entry.stubs.EntryStub
import kotlin.test.assertEquals

class MapperTest {
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
