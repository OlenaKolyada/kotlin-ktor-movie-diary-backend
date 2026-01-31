package scenarios.entry

import base.client.Client
import com.funkycorgi.vulpecula.entry.api.models.EntryDebug
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance

@Suppress("unused")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ScenariosEntry(
    private val client: Client,
    private val debug: EntryDebug? = null
) {
    @Nested
    internal inner class CreateDelete: ScenarioCreateDeleteEntry(client, debug)
    @Nested
    internal inner class Update: ScenarioUpdateEntry(client, debug)
    @Nested
    internal inner class Read: ScenarioReadEntry(client, debug)
    @Nested
    internal inner class Search: ScenarioSearchEntry(client, debug)
}