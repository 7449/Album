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
        jcenter()
    }
}
include(
        ":core",
        ":scan",
        ":material",
        ":sample",
        ":compat",
        ":wechat"
)