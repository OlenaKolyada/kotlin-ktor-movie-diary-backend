package environments

import infrastructure.AbstractTestEnvironment

object WiremockTestEnvironment : AbstractTestEnvironment(
    "app-wiremock", 8080, "docker-compose-wiremock.yml"
)
