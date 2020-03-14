plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Deps.Build.compileSdk)
    buildToolsVersion(Deps.Build.buildTools)

    defaultConfig {
        applicationId = "com.olivergao.openapi"
        minSdkVersion(Deps.Build.minSdk)
        targetSdkVersion(Deps.Build.targetSdk)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true",
                    "dagger.gradle.incremental" to "true"
                )
            }
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
                proguardFile(file("../config/proguard/proguard-rules.txt"))
            }
        }

        compileOptions {
            sourceCompatibility = Deps.Versions.java
            targetCompatibility = Deps.Versions.java
        }

        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
}

dependencies {
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Android.androidXCore)
    implementation(Deps.Android.appCompact)
    implementation(Deps.Android.constraintlayout)
    implementation(Deps.Android.legacySupport)
    implementation(Deps.Android.fragment)
    implementation(Deps.Google.material)
    implementation(Deps.Squareup.leakcanary)

    //test
    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Android.Test.junit)
    androidTestImplementation(Deps.Android.Test.Espresso.core)

    //navigation
    implementation(Deps.Google.Navigation.fragment)
    implementation(Deps.Google.Navigation.ui)

    //room
    implementation(Deps.Google.Room.runtime)
    implementation(Deps.Google.Room.room)
    kapt(Deps.Google.Room.compile)
    testImplementation(Deps.Google.Room.roomTesting)

    //lifecycle
    implementation(Deps.Google.Lifecycle.viewModel)
    implementation(Deps.Google.Lifecycle.liveData)
    implementation(Deps.Google.Lifecycle.common)

    //Dagger
    implementation(Deps.Google.Dagger.dagger)
    kapt(Deps.Google.Dagger.compiler)

    implementation(Deps.Google.Dagger.android)
    implementation(Deps.Google.Dagger.support)
    kapt(Deps.Google.Dagger.processor)

    //glide
    implementation(Deps.Glide.glide)
    kapt(Deps.Glide.compiler)

    //Retrofit2
    implementation(Deps.Squareup.Retrofit.retrofit)
    implementation(Deps.Squareup.Retrofit.gsonConverter)
}