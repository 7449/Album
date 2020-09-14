plugins {
    id(Plugin.application)
    kotlin(Plugin.kotlin_android)
    kotlin(Plugin.kotlin_ext)
}
android {
    compileSdkVersion(Version.compileSdk)
    defaultConfig {
        applicationId = Version.applicationId
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
        versionCode = Version.versionCode
        versionName = Version.versionName
        vectorDrawables.useSupportLibrary = true
    }
}
dependencies {
    implementation(project(":wechat"))
    implementation(Dep.material)
    implementation(Dep.appcompat)
    implementation(Dep.fragment)
    implementation(Dep.viewPager2)
    implementation(Dep.glide)
    implementation(Dep.cameraview)
    implementation(Dep.cropper)
    implementation(Dep.uCrop)
    implementation(Dep.kotlin)
}