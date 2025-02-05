plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    id("kotlin-parcelize")
}
android {
    namespace = "com.gallery.sample"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        targetSdk = libs.versions.targetSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }
    viewBinding { enable = true }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}
dependencies {
    implementation(projects.media)
    implementation(projects.core)
    implementation(projects.compat)
    implementation(projects.material)
    implementation(projects.wechat)

    implementation(libs.material)
    implementation(libs.fragment)
    implementation(libs.activity)
    implementation(libs.appcompat)
    implementation(libs.viewPager2)
    implementation(libs.recyclerView)
    implementation(libs.glide)
    implementation(libs.cropper)
    implementation(libs.cameraview)
    implementation(libs.color)
    implementation(libs.flex)
    implementation(libs.banner)
    implementation(libs.ucrop)
}