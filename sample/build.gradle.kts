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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
dependencies {
    implementation(project(":media"))
    implementation(project(":core"))
    implementation(project(":compat"))
    implementation(project(":material"))
    implementation(project(":wechat"))

    implementation(Dep.material)
    implementation(Dep.fragment)
    implementation(Dep.activity)
    implementation(Dep.kotlin)
    implementation(Dep.appcompat)
    implementation(Dep.viewPager2)
    implementation(Dep.recyclerView)
    implementation(Dep.glide)
    implementation(Dep.cropper)
    implementation(Dep.cameraview)
    implementation(Dep.color)
    implementation(Dep.flex)
    implementation(Dep.banner)
    implementation(Dep.ucrop)
}