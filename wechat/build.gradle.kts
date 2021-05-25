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
            "com.github.7449.album.wechat"
        )
    }
}
dependencies {
    compileOnly(project(":ui"))
    compileOnly(project(":scan"))
    compileOnly(project(":core"))

    implementation(Dep.glide)

    /* 和 ui library 依赖保持一致 */
    implementation(Dep.appcompat)
    implementation(Dep.fragment)
    implementation(Dep.viewPager2)
    implementation(Dep.recyclerView)
    implementation(Dep.kotlin)
}
