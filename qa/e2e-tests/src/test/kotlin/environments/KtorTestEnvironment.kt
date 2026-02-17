package environments

import infrastructure.AbstractTestEnvironment

object KtorTestEnvironment : AbstractTestEnvironment(
    "app-ktor_1", 8080, "docker-compose-ktor.yml"
)
