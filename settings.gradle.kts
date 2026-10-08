pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.public.kotlin/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.public.kotlin/public/p/compose/release")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.public.kotlin/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.public.kotlin/public/p/compose/release")
    }
}

rootProject.name = "Management"
include(":shared")
