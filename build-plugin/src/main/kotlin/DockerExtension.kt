package com.funkycorgi.vulpecula.plugin

open class DockerExtension {
    var dockerFile = "Dockerfile"
    var imageName = ""
    var imageTag = "latest"
    var buildContext = "./"
    var buildArgs: Map<String, String> = emptyMap()
    var noCache = false
    var removeIntermediateContainers = false

    val images = DockerImagesExtension()
}
