package com.funkycorgi.vulpecula.entry.cor

import com.funkycorgi.vulpecula.entry.cor.handlers.CorWorker
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CorWorkerTest {
    @Test
    fun `worker should execute handle`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { history += "w1; " }
        )

        val ctx = TestContext()
        worker.exec(ctx)

        assertEquals("w1; ", ctx.history)
    }

    @Test
    fun `worker should not execute when condition is false`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockOn = { status == TestContext.CorStatus.ERROR },
            blockHandle = { history += "w1; " }
        )

        val ctx = TestContext()
        worker.exec(ctx)

        assertEquals("", ctx.history)
    }

    @Test
    fun `worker should handle exception`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { throw RuntimeException("some error") },
            blockExcept = { e -> history += e.message }
        )

        val ctx = TestContext()
        worker.exec(ctx)

        assertEquals("some error", ctx.history)
    }
}
