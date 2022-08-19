object Dependencies {
    const val okHttp = "com.squareup.okhttp3:okhttp:5.0.0-alpha.10"
    const val ktlint = "com.pinterest:ktlint:0.45.2"
    const val coil = "io.coil-kt:coil-compose:2.2.0"
    const val daggerHiltVersion = "2.42"

    object Kotlin {
        const val version = "1.7.10"
        const val jsonSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2"
    }

    object Gradle {
        const val androidBuildPlugin = "com.android.tools.build:gradle:7.2.2"
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$daggerHiltVersion"
        const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"

        object VersionsPlugin {
            const val id = "com.github.ben-manes.versions"
            const val version = "0.42.0"
        }
    }

    object AndroidX {
        const val palette = "androidx.palette:palette:1.0.0"
        const val browser = "androidx.browser:browser:1.4.0"
        const val dataStore = "androidx.datastore:datastore-preferences:1.0.0"

        object Ktx {
            const val core = "androidx.core:core-ktx:1.9.0-beta01"
        }
    }

    object Compose {
        // Compose is combination of 7 Maven Group Ids within androidx.
        // Each Group contains a targeted subset of functionality, each with it's own set of release notes.
        const val version = "1.3.0-alpha03"
        const val compilerVersion = "1.3.0"
        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val layout = "androidx.compose.foundation:foundation-layout:$version"
        const val ui = "androidx.compose.ui:ui:$version"
        const val uiUtil = "androidx.compose.ui:ui-util:$version"
        const val material = "androidx.compose.material:material:$version"
        const val icons = "androidx.compose.material:material-icons-extended:$version"
        const val animation = "androidx.compose.animation:animation:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val uiTest = "androidx.compose.ui:ui-test:$version"
        const val uiTestJunit = "androidx.compose.ui:ui-test-junit4:$version"
        const val paging = "androidx.paging:paging-compose:1.0.0-alpha16"
        const val activity = "androidx.activity:activity-compose:1.6.0-beta01"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0-alpha01"
        const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha03"

        object Accompanist {
            private const val version = "0.26.1-alpha"

            const val pager = "com.google.accompanist:accompanist-pager:$version"
            const val flowLayout = "com.google.accompanist:accompanist-flowlayout:$version"
        }
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:$daggerHiltVersion"
        const val androidCompiler = "com.google.dagger:hilt-android-compiler:$daggerHiltVersion"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
        const val serializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val mockk = "io.mockk:mockk:1.12.3"
        const val striktAssertion = "io.strikt:strikt-core:0.34.1"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1"
        const val junitExt = "androidx.test.ext:junit:1.1.4-alpha07"
    }
}
