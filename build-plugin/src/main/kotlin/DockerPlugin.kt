package com.funkycorgi.vulpecula.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

class DockerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("docker", DockerExtension::class.java)

        project.tasks.register("dockerBuild", DockerBuildTask::class.java) {
            dockerFile.set(extension.dockerFile)
            imageName.set(extension.imageName.ifBlank { project.name })
            imageTag.set(extension.imageTag)
            buildContext.set(extension.buildContext)
            buildArgs.set(extension.buildArgs)
            noCache.set(extension.noCache)
            removeIntermediateContainers.set(extension.removeIntermediateContainers)
        }

        project.afterEvaluate {
            if (extension.images.registeredNames.isEmpty()) {
                return@afterEvaluate
            }

            for (dockerImageName in extension.images.registeredNames) {
                val ext = extension.images.images[dockerImageName] ?: continue
                val suffix = dockerImageName.replace(Regex("[^A-Za-z0-9]+"), "")
                val taskName = "dockerBuild$suffix"
                val imgName = ext.imageName.takeIf { !it.isNullOrBlank() } ?: project.name

                val taskProvider: TaskProvider<DockerBuildTask> = project.tasks.register(
                    taskName,
                    DockerBuildTask::class.java,
                ) {
                    group = "docker"
                    description = "Builds Docker image: $dockerImageName"
                    dockerFile.set(ext.dockerFile)
                    imageName.set(imgName.lowercase())
                    imageTag.set(ext.imageTag)
                    buildContext.set(ext.buildContext)
                    buildArgs.set(ext.buildArgs)
                    noCache.set(ext.noCache)
                    removeIntermediateContainers.set(extension.removeIntermediateContainers)
                }

                ext.dependsOnTask?.let { dependency ->
                    taskProvider.configure {
                        dependsOn(project.tasks.named(dependency))
                    }
                }
            }
        }
    }
}
