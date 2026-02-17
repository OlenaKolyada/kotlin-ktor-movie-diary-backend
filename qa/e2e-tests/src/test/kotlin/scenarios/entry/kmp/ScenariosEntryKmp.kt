package scenarios.entry.kmp

import infrastructure.client.Client
import com.funkycorgi.vulpecula.entry.api.kmp.models.EntryDebug
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance

@Suppress("unused")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ScenariosEntryKmp(
    private val client: Client,
    private val debug: EntryDebug? = null
) {
    @Nested
    internal inner class CreateDeleteEntryKmp: ScenarioCreateDeleteEntryKmp(client, debug)

    @Nested
    internal inner class UpdateEntryKmp: ScenarioUpdateEntryKmp(client, debug)

    @Nested
    internal inner class ReadEntryKmp: ScenarioReadEntryKmp(client, debug)

    @Nested
    internal inner class SearchEntryKmp: ScenarioSearchEntryKmp(client, debug)
}