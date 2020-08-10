plugins {
    id(Plugin.library)
    kotlin(Plugin.kotlin_android)
    kotlin(Plugin.kotlin_ext)
}
apply(from = "../gradle/UPLOAD.gradle")
android {
    compileSdkVersion(Version.compileSdk)
    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }
    androidExtensions { isExperimental = true }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//    api(project(":scan"))
    api(Dep.scan)
    api(Dep.kotlinx)
    api(Dep.viewHolder)
    compileOnly(Dep.fragment)
    compileOnly(Dep.kotlin)
    compileOnly(Dep.recyclerView)
    compileOnly(Dep.viewPager2)
    compileOnly(Dep.uCrop)
}