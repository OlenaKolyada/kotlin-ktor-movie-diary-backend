package docker

import base.AbstractDockerCompose

object KtorDockerCompose : AbstractDockerCompose(
    "app-ktor_1", 8080, "docker-compose-ktor.yml"
)
