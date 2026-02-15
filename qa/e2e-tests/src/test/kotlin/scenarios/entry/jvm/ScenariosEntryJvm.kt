package scenarios.entry.jvm

import base.client.Client
import com.funkycorgi.vulpecula.entry.api.jvm.models.EntryDebug
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance

@Suppress("unused")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ScenariosEntryJvm(
    private val client: Client,
    private val debug: EntryDebug? = null
) {
    @Nested
    internal inner class CreateDeleteEntryJvm: ScenarioCreateDeleteEntryJvm(client, debug)

    @Nested
    internal inner class UpdateEntryJvm: ScenarioUpdateEntryJvm(client, debug)

    @Nested
    internal inner class ReadEntryJvm: ScenarioReadEntryJvm(client, debug)

    @Nested
    internal inner class SearchEntryJvm: ScenarioSearchEntryJvm(client, debug)
}