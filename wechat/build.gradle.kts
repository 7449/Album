plugins {
    id(Plugin.library)
    kotlin(Plugin.kotlin_android)
    kotlin(Plugin.kotlin_ext)
}
android {
    compileSdkVersion(Version.compileSdk)
    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }
    androidExtensions { isExperimental = true }
}
dependencies {
    api(project(":ui"))
    compileOnly(Dep.appcompat)
    compileOnly(Dep.material)
    compileOnly(Dep.recyclerView)
    compileOnly(Dep.fragment)
    compileOnly(Dep.glide)
    compileOnly(Dep.kotlin)
}
