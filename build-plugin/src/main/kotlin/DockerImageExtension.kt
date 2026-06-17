package com.funkycorgi.vulpecula.plugin

class DockerImageExtension {
    var imageName: String? = null
    var buildContext = "./"
    var dockerFile = "Dockerfile"
    var imageTag = "latest"
    var dependsOnTask: String? = null
    var buildArgs: Map<String, String> = emptyMap()
    var noCache = false
}
