plugins {
    id(Plugin.library)
    kotlin(Plugin.kotlin_android)
    id(Plugin.kotlin_parcelize)
}
android {
    compileSdk = Version.minSdk
    defaultConfig {
        minSdk = Version.minSdk
        targetSdk = Version.targetSdk
    }
    compileOptions {
        kotlinOptions.freeCompilerArgs += listOf(Args.moduleName, Args.prefix.plus(project.name))
    }
}
dependencies {
    DepList.scan.forEach { implementation(it) }
}
