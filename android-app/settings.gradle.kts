pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LifeLoggerAI"

include(
    ":app",
    ":core:common",
    ":core:model",
    ":core:database",
    ":core:datastore",
    ":core:network",
    ":core:domain",
    ":core:data",
    ":core:ui",
    ":feature:auth",
    ":feature:timeline"
)
