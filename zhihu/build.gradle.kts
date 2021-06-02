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
            "com.github.7449.album.ui.zhihu"
        )
    }
}
dependencies {
    DepLib.zhihu.forEach { compileOnly(project(it)) }
    DepList.zhihu.forEach { implementation(it) }
}