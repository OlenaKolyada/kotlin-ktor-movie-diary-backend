package com.funkycorgi.vulpecula.entry.cor

import com.funkycorgi.vulpecula.entry.cor.handlers.CorChain
import com.funkycorgi.vulpecula.entry.cor.handlers.CorWorker
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CorChainTest {
    @Test
    fun `chain should execute workers`() = runTest {
        val createWorker = { title: String ->
            CorWorker<TestContext>(
                title = title,
                blockOn = { status == TestContext.CorStatus.NONE },
                blockHandle = { history += "$title; " }
            )
        }
        val chain = CorChain(
            execs = listOf(createWorker("w1"), createWorker("w2")),
            title = "chain",
        )

        val ctx = TestContext()
        chain.exec(ctx)

        assertEquals("w1; w2; ", ctx.history)
    }
}
