buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(ClassPath.gradle)
        classpath(ClassPath.kotlin)
        classpath(ClassPath.maven)
    }
}
allprojects {
    repositories {
        maven(Maven.jitpack)
        google()
        mavenCentral()
        jcenter()
    }
}