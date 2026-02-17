package infrastructure

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseContainerTest(protected val testEnvironment: AbstractTestEnvironment) {
    @BeforeAll
    fun startEnvironment() {
        testEnvironment.start()
    }
    @AfterAll
    fun stopEnvironment() {
        testEnvironment.stop()
    }
}
