plugins {
    id(Plugin.library)
    id(Plugin.maven)
    kotlin(Plugin.kotlin_android)
    id(Plugin.kotlin_parcelize)
}
android {
    compileSdkVersion(Version.minSdk)
    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }
    compileOptions {
        kotlinOptions.freeCompilerArgs += listOf(
            "-module-name",
            "com.github.7449.album.scan"
        )
    }
}
dependencies {
    DepList.scan.forEach { implementation(it) }
}
