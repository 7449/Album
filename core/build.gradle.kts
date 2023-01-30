plugins {
    id(Plugin.library)
    kotlin(Plugin.kotlin_android)
    id(Plugin.kotlin_parcelize)
}
android {
    namespace = "com.gallery.core"
    compileSdk = Version.compileSdk
    defaultConfig {
        minSdk = Version.minSdk
    }
    compileOptions {
        kotlinOptions.freeCompilerArgs += listOf(Args.moduleName, Args.prefix.plus(project.name))
    }
}
dependencies {
    compileOnly(project(":scan"))
    implementation(Dep.fragment)
    implementation(Dep.kotlin)
    implementation(Dep.appcompat)
    implementation(Dep.viewPager2)
    implementation(Dep.recyclerView)
}