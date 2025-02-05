pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://jitpack.io")
        google()
        mavenCentral()
    }
}
rootProject.name = "Album"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    ":core",
    ":media",
    ":material",
    ":sample",
    ":compat",
    ":wechat"
)