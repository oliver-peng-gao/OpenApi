import org.gradle.api.JavaVersion

object Deps {
    object Versions {
        const val androidGradle = "3.6.1"
        const val kotlin = "1.3.71"
        const val dagger = "2.22"
        const val retrofit = "2.8.0"
        const val glide = "4.11.0"
        const val room = "2.2.5"
        const val navigation = "2.3.0-alpha04"
        const val lifecycle = "2.2.0"
        const val espresso = "3.2.0"
        const val dependencyUpdates = "0.28.0"
        val java = JavaVersion.VERSION_1_8
    }

    object Build {
        const val minSdk = 23
        const val targetSdk = 29
        const val compileSdk = 29
        const val buildTools = "29.0.3"
    }

    object Plugins {
        const val gradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val navSafeArgs =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
        const val dependencyUpdates =
            "com.github.ben-manes:gradle-versions-plugin:${Versions.dependencyUpdates}"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    }

    object Android {
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val androidXCore = "androidx.core:core-ktx:1.2.0"
        const val appCompact = "androidx.appcompat:appcompat:1.1.0"
        const val legacySupport = "androidx.legacy:legacy-support-v4:1.0.0"
        const val fragment = "androidx.fragment:fragment:1.2.3"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.2.3"

        object Test {
            const val core = "androidx.test:core:1.2.0"
            const val junit = "androidx.test.ext:junit:1.1.1"
            const val runner = "androidx.test:runner:1.2.0"
            const val orchestrator = "androidx.test:orchestrator:1.2.0"

            object Espresso {
                const val core = "androidx.test.espresso:espresso-core:${Versions.espresso}"
                const val intents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
                const val contrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
            }
        }

        object Room {
            const val runtime = "androidx.room:room-runtime:${Versions.room}"
            const val compile = "androidx.room:room-compiler:${Versions.room}"
            const val room = "androidx.room:room-ktx:${Versions.room}"
            const val roomTesting = "androidx.room:room-testing:${Versions.room}"
        }

        object Navigation {
            const val fragment =
                "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
            const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        }

        object Lifecycle {
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
            const val common = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
        }
    }

    object MaterialDialog {
        const val materialDialog = "com.afollestad.material-dialogs:core:3.3.0"
    }

    object Squareup {
        const val leakcanary = "com.squareup.leakcanary:leakcanary-android:2.2"

        object Retrofit {
            const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
            const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
        }
    }

    object Google {
        const val material = "com.google.android.material:material:1.1.0"

        object Dagger {
            const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
            const val android = "com.google.dagger:dagger-android:${Versions.dagger}"
            const val support = "com.google.dagger:dagger-android-support:${Versions.dagger}"
            const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
            const val processor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
        }

    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }

    object Test {
        const val junit = "junit:junit:4.13"

        object Mockito {
            const val inline = "org.mockito:mockito-inline:3.2.4"
            const val kotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0"
        }
    }
}