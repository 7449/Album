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
    compileOptions { kotlinOptions.freeCompilerArgs += listOf("-module-name", "com.ydevelop.gallery.core") }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//    api(project(":scan"))
    api(Dep.scan)
    compileOnly(Dep.appcompat)
    compileOnly(Dep.fragment)
    compileOnly(Dep.viewPager2)
    compileOnly(Dep.recyclerView)
    compileOnly(Dep.kotlin)
}