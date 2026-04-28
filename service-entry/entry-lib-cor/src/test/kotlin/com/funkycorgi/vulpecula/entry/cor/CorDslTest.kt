package com.funkycorgi.vulpecula.entry.cor

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CorDslTest {
    private suspend fun execute(dsl: ICorExecDsl<TestContext>): TestContext {
        val ctx = TestContext()
        dsl.build().exec(ctx)
        return ctx
    }

    @Test
    fun `handle should execute`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { history += "w1; " }
            }
        }

        assertEquals("w1; ", execute(chain).history)
    }

    @Test
    fun `on should check condition`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                on { status == TestContext.CorStatus.ERROR }
                handle { history += "w1; " }
            }
            worker {
                on { status == TestContext.CorStatus.NONE }
                handle {
                    history += "w2; "
                    status = TestContext.CorStatus.FAILING
                }
            }
            worker {
                on { status == TestContext.CorStatus.FAILING }
                handle { history += "w3; " }
            }
        }

        assertEquals("w2; w3; ", execute(chain).history)
    }

    @Test
    fun `except should execute when exception is thrown`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { throw RuntimeException("some error") }
                except { history += it.message }
            }
        }

        assertEquals("some error", execute(chain).history)
    }

    @Test
    fun `should throw when exception is not handled`() = runTest {
        val chain = rootChain<TestContext> {
            worker("throw") {
                throw RuntimeException("some error")
            }
        }

        assertFails {
            execute(chain)
        }
    }

    @Test
    fun `nested chain should execute`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                on { status == TestContext.CorStatus.NONE }
                handle { status = TestContext.CorStatus.RUNNING }
            }
            chain {
                on { status == TestContext.CorStatus.RUNNING }
                worker("increment") {
                    value += 4
                }
            }
        }

        assertEquals(4, execute(chain).value)
    }
}
