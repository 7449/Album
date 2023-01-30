plugins {
    id(Plugin.application)
    kotlin(Plugin.kotlin_android)
    id(Plugin.kotlin_parcelize)
}
android {
    namespace = "com.gallery.sample"
    compileSdk = Version.compileSdk
    defaultConfig {
        applicationId = Version.applicationId
        minSdk = Version.minSdk
        targetSdk = Version.targetSdk
        versionCode = Version.versionCode
        versionName = Version.versionName
    }
    viewBinding { enable = true }
}
dependencies {
    implementation(project(":scan"))
    implementation(project(":core"))
    implementation(project(":compat"))
    implementation(project(":material"))
    implementation(project(":wechat"))
    implementation(Dep.fragment)
    implementation(Dep.kotlin)
    implementation(Dep.appcompat)
    implementation(Dep.viewPager2)
    implementation(Dep.recyclerView)
    implementation(Dep.glide)
    implementation(Dep.cropper)
    implementation(Dep.cameraview)
}