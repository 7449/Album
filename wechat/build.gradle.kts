plugins {
    id(Plugin.library)
    kotlin(Plugin.kotlin_android)
    id(Plugin.kotlin_parcelize)
}
apply("../maven.gradle")
android {
    namespace = "com.gallery.ui.wechat"
    compileSdk = Version.compileSdk
    defaultConfig {
        minSdk = Version.minSdk
    }
    viewBinding { enable = true }
    compileOptions {
        kotlinOptions.freeCompilerArgs += listOf(Args.moduleName, Args.prefix.plus(project.name))
    }
}
dependencies {
    compileOnly(project(":media"))
    compileOnly(project(":core"))
    compileOnly(project(":compat"))
    implementation(Dep.fragment)
    implementation(Dep.kotlin)
    implementation(Dep.appcompat)
    implementation(Dep.viewPager2)
    implementation(Dep.recyclerView)
    implementation(Dep.glide)
}
