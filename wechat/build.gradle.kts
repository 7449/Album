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
//    api(Dep.ui)
    compileOnly(Dep.glide)

    /* 和 ui library 依赖保持一致 */
    compileOnly(Dep.appcompat)
    compileOnly(Dep.fragment)
    compileOnly(Dep.viewPager2)
    compileOnly(Dep.recyclerView)
    compileOnly(Dep.kotlin)
}
