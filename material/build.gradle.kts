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
    buildFeatures.viewBinding = true
    compileOptions {
        kotlinOptions.freeCompilerArgs += listOf(
            "-module-name",
            "com.github.7449.album.material"
        )
    }
}
dependencies {
    DepLib.ui.forEach { compileOnly(project(it)) }
    DepList.ui.forEach { implementation(it) }
}