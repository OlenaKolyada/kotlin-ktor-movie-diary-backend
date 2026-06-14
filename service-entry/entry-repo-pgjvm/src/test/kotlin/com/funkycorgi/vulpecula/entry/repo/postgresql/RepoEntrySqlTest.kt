package com.funkycorgi.vulpecula.entry.repo.postgresql

import com.benasher44.uuid.uuid4
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import com.funkycorgi.vulpecula.entry.repo.tests.*
import com.funkycorgi.vulpecula.entry.common.models.Entry
import com.funkycorgi.vulpecula.entry.repo.common.EntryRepoInitialized
import com.funkycorgi.vulpecula.entry.repo.common.IRepoEntryInitializable
import java.io.File
import java.time.Duration
import kotlin.test.AfterTest
import kotlin.test.Ignore


private fun IRepoEntryInitializable.clear() {
    val pgRepo = (this as EntryRepoInitialized).repo as RepoEntrySql
    pgRepo.clear()
}

@RunWith(Enclosed::class)
class RepoEntrySqlTest {

    class RepoEntrySqlCreateTest : RepoEntryCreateTest() {
        override val repo = repoUnderTestContainer(
            initObjects,
            randomUuid = { uuidNew.asString() },
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoEntrySqlReadTest : RepoEntryReadTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoEntrySqlUpdateTest : RepoEntryUpdateTest() {
        override val repo = repoUnderTestContainer(
            initObjects,
            randomUuid = { lockNew.asString() },
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoEntrySqlDeleteTest : RepoEntryDeleteTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoEntrySqlSearchTest : RepoEntrySearchTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    @Ignore
    companion object {
        private const val PG_SERVICE = "psql"
        private const val MG_SERVICE = "liquibase"

        private val container: ComposeContainer by lazy {
            val res = this::class.java.classLoader.getResource("docker-compose-pg.yml")
                ?: throw Exception("No resource found")
            val file = File(res.toURI())
            ComposeContainer(file)
                .withExposedService(PG_SERVICE, 5432)
                .withStartupTimeout(Duration.ofSeconds(300))
                .waitingFor(
                    MG_SERVICE,
                    Wait.forLogMessage(".*Liquibase command 'update' was executed successfully.*", 1)
                )
        }

        private const val HOST = "localhost"
        private const val USER = "postgres"
        private const val PASS = "vulpecula-pass"
        private val PORT by lazy {
            container.getServicePort(PG_SERVICE, 5432) ?: 5432
        }

        fun repoUnderTestContainer(
            initObjects: Collection<Entry> = emptyList(),
            randomUuid: () -> String = { uuid4().toString() },
        ): IRepoEntryInitializable = EntryRepoInitialized(
            repo = RepoEntrySql(
                SqlProperties(
                    host = HOST,
                    user = USER,
                    password = PASS,
                    port = PORT,
                ),
                randomUuid = randomUuid
            ),
            initObjects = initObjects,
        )

        @JvmStatic
        @BeforeClass
        fun start() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun finish() {
            container.stop()
        }
    }
}
