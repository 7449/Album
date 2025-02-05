plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    id("kotlin-parcelize")
}
android {
    namespace = "com.gallery.compat"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}
dependencies {
    compileOnly(projects.media)
    compileOnly(projects.core)
    implementation(libs.fragment)
    implementation(libs.appcompat)
    implementation(libs.viewPager2)
    implementation(libs.recyclerView)
}