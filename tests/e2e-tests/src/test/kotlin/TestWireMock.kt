import base.BaseContainerTest
import base.client.Client
import base.client.RestClient
import com.funkycorgi.vulpecula.entry.api.models.EntryDebug
import com.funkycorgi.vulpecula.entry.api.models.EntryRequestDebugMode
import docker.WiremockDockerCompose
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import scenarios.entry.ScenariosEntry

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestWireMock: BaseContainerTest(WiremockDockerCompose) {
    private val client: Client = RestClient(compose)
    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class EntryScenarios: ScenariosEntry(client, EntryDebug(mode = EntryRequestDebugMode.PROD))

}