import infrastructure.BaseContainerTest
import infrastructure.client.Client
import infrastructure.client.RestClient
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryDebug as EntryDebugJvm
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryRequestDebugMode as EntryRequestDebugModeJvm
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryDebug as EntryDebugKmp
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryRequestDebugMode as EntryRequestDebugModeKmp
import environments.WiremockTestEnvironment
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import scenarios.entry.jvm.ScenariosEntryJvm
import scenarios.entry.kmp.ScenariosEntryKmp

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestWireMock: BaseContainerTest(WiremockTestEnvironment) {
    private val client: Client = RestClient(testEnvironment)
    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class EntryJvmScenario: ScenariosEntryJvm(
        client, EntryDebugJvm(mode = EntryRequestDebugModeJvm.PROD)
    )

    @Nested
    internal inner class EntryKmpScenario: ScenariosEntryKmp(
        client, EntryDebugKmp(mode = EntryRequestDebugModeKmp.PROD)
    )

}