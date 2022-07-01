plugins {
    id(Plugin.library)
    kotlin(Plugin.kotlin_android)
    id(Plugin.kotlin_parcelize)
}
android {
    compileSdk = Version.compileSdk
    defaultConfig {
        minSdk = Version.minSdk
        targetSdk = Version.targetSdk
    }
    compileOptions {
        kotlinOptions.freeCompilerArgs += listOf(Args.moduleName, Args.prefix.plus(project.name))
    }
}
dependencies {
    DepLib.compat.forEach { compileOnly(project(it)) }
    DepList.compat.forEach { implementation(it) }
}