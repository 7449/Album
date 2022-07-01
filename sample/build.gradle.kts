plugins {
    id(Plugin.application)
    kotlin(Plugin.kotlin_android)
    id(Plugin.kotlin_parcelize)
}
android {
    compileSdk = Version.compileSdk
    defaultConfig {
        applicationId = Version.applicationId
        minSdk = Version.minSdk
        targetSdk = Version.targetSdk
        versionCode = Version.versionCode
        versionName = Version.versionName
        vectorDrawables.useSupportLibrary = true
    }
    buildFeatures.viewBinding = true
}
dependencies {
    DepLib.sample.distinct().forEach { implementation(project(it)) }
    DepList.sample.distinct().forEach { implementation(it) }
}