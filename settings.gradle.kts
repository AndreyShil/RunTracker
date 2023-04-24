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
rootProject.name = "RunTracker"
include(":app")
include(":feature:auth")
include(":feature:tracker")
include(":feature:stats")
include(":feature:profile")
include(":core:ui")
include(":core:iconpack")
include(":core:core-api")
include(":core:core-impl")
include(":core:strings")
