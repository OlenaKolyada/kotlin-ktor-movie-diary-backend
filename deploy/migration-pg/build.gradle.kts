import org.testcontainers.containers.ComposeContainer

plugins {
    id("build-docker")
}

group = "com.funkycorgi.vulpecula.migration"
version = "0.1.0"

docker {
    imageName = project.name
    imageTag = project.version.toString()
    buildContext = project.layout.projectDirectory.dir("src/main/liquibase/changelog").toString()
    dockerFile = "../../docker/Dockerfile"
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(libs.testcontainers.core)
    }
}

val pgContainer: ComposeContainer by lazy {
    ComposeContainer(
        file("src/test/compose/docker-compose-pg.yml")
    )
        .withExposedService("psql", 5432)
}

tasks {
    val buildImages by creating {
        dependsOn(dockerBuild)
    }

    val pgDn by creating {
        group = "db"
        doFirst {
            println("Stopping PostgreSQL...")
            pgContainer.stop()
            println("PostgreSQL stopped")
        }
    }

    val pgUp by creating {
        group = "db"
        doFirst {
            println("Starting PostgreSQL...")
            pgContainer.start()
            println("PostgreSQL started at port: ${pgContainer.getServicePort("psql", 5432)}")
        }
        finalizedBy(pgDn)
    }
}
