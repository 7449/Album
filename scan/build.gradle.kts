plugins {
    id(Plugin.library)
    kotlin(Plugin.kotlin_android)
    kotlin(Plugin.kotlin_ext)
}
apply(from = "../gradle/UPLOAD.gradle")
android {
    compileSdkVersion(Version.minSdk)
    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }
    androidExtensions { isExperimental = true }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    compileOnly(Dep.kotlinx)
    compileOnly(Dep.fragment)
    compileOnly(Dep.kotlin)
}