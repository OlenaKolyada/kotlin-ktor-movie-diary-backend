import com.funkycorgi.vulpecula.entry.repo.common.EntryRepoInitialized
import com.funkycorgi.vulpecula.entry.repo.inmemory.EntryRepoInMemory
import com.funkycorgi.vulpecula.entry.repo.tests.*

class EntryRepoInMemoryCreateTest : RepoEntryCreateTest() {
    override val repo = EntryRepoInitialized(
        EntryRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class EntryRepoInMemoryReadTest : RepoEntryReadTest() {
    override val repo = EntryRepoInitialized(
        EntryRepoInMemory(),
        initObjects = initObjects,
    )
}

class EntryRepoInMemoryUpdateTest : RepoEntryUpdateTest() {
    override val repo = EntryRepoInitialized(
        EntryRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}

class EntryRepoInMemoryDeleteTest : RepoEntryDeleteTest() {
    override val repo = EntryRepoInitialized(
        EntryRepoInMemory(),
        initObjects = initObjects,
    )
}

class EntryRepoInMemorySearchTest : RepoEntrySearchTest() {
    override val repo = EntryRepoInitialized(
        EntryRepoInMemory(),
        initObjects = initObjects,
    )
}
