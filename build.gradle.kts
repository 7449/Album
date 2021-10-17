buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(ClassPath.gradle)
        classpath(ClassPath.kotlin)
    }
}
@Suppress("JcenterRepositoryObsolete") allprojects {
    repositories {
        maven(Maven.jitpack)
        google()
        mavenCentral()
        jcenter()
    }
}