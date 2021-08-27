buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(ClassPath.gradle)
        classpath(ClassPath.kotlin)
        classpath(ClassPath.maven)
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