import com.android.build.gradle.LibraryExtension

plugins {
    id("com.android.library")
    kotlin("android")
}

buildscript {

    repositories {
        google()
        gradlePluginPortal()
    }

}

configure<LibraryExtension> {

    compileSdkVersion(28)

    defaultConfig {
        versionCode = 1
        versionName = "1.0"
        minSdkVersion(21)
        targetSdkVersion(28)

        testApplicationId = "net.apptronic.core.android.test"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments = mapOf(
            "disableAnalytics" to "true",
            "clearPackageData" to "true"
        )

        resConfigs("en")
    }

}

repositories {
    jcenter()
}

dependencies {
    val kotlinVersion = "1.3.21"
    implementation(kotlin("stdlib:$kotlinVersion"))
    implementation(kotlin("reflect:$kotlinVersion"))

    "implementation"(project(":lib"))

    compileOnly("androidx.core:core:1.0.1")
    compileOnly("androidx.appcompat:appcompat:1.0.2")
    compileOnly("com.google.android.material:material:1.0.0")
}