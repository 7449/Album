plugins {
    id(Plugin.library)
//    id(Plugin.maven)
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
            "com.github.7449.album.ui.qq"
        )
    }
}
dependencies {
    DepLib.qq.forEach { compileOnly(project(it)) }
    DepList.qq.forEach { implementation(it) }
}