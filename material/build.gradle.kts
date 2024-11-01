plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    id("kotlin-parcelize")
}
android {
    namespace = "com.gallery.ui.material"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }
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
    compileOnly(project(":media"))
    compileOnly(project(":core"))
    compileOnly(project(":compat"))
    implementation(libs.fragment)
    implementation(libs.appcompat)
    implementation(libs.viewPager2)
    implementation(libs.recyclerView)
    implementation(libs.glide)
    implementation(libs.cropper)
}