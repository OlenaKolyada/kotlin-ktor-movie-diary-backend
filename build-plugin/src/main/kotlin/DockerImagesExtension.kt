package com.funkycorgi.vulpecula.plugin

class DockerImagesExtension {
    private val registeredImages = mutableMapOf<String, DockerImageExtension>()

    val images: Map<String, DockerImageExtension> get() = registeredImages
    val registeredNames: Set<String> get() = registeredImages.keys

    fun register(name: String, configure: DockerImageExtension.() -> Unit): DockerImageExtension {
        val ext = DockerImageExtension().apply(configure)
        registeredImages[name] = ext
        return ext
    }
}
