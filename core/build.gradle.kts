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
            "com.github.7449.album.core"
        )
    }
}
dependencies {
    compileOnly(project(":scan"))
    implementation(Dep.appcompat)
    implementation(Dep.fragment)
    implementation(Dep.viewPager2)
    implementation(Dep.recyclerView)
    implementation(Dep.kotlin)
}