plugins {
    id(Plugin.library)
    id(Plugin.maven)
    kotlin(Plugin.kotlin_android)
    id(Plugin.kotlin_parcelize)
}
android {
    compileSdkVersion(Version.compileSdk)
    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }
    compileOptions {
        kotlinOptions.freeCompilerArgs += listOf(
            "-module-name",
            "com.github.7449.album.compat"
        )
    }
}
dependencies {
    DepLib.compat.forEach { compileOnly(project(it)) }
    DepList.core.forEach { implementation(it) }
}