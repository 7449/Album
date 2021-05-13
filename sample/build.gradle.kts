plugins {
    id(Plugin.application)
    kotlin(Plugin.kotlin_android)
    id(Plugin.kotlin_parcelize)
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
    buildFeatures.viewBinding = true
}
dependencies {
    implementation(project(":wechat"))
    implementation(project(":ui"))
    implementation(project(":core"))
    implementation(project(":scan"))

    implementation(Dep.glide)
    implementation(Dep.material)
    implementation(Dep.cropper)
    implementation(Dep.uCrop)
    implementation(Dep.cameraview)

    implementation(Dep.kotlin)

    implementation(Dep.appcompat)
    implementation(Dep.fragment)
    implementation(Dep.viewPager2)
    implementation(Dep.recyclerView)
}