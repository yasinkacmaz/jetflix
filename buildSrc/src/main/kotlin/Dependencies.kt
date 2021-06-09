object Dependencies {
    const val okHttp = "com.squareup.okhttp3:okhttp:5.0.0-alpha.2"
    const val ktlint = "com.pinterest:ktlint:0.41.0"
    const val daggerHiltVersion = "2.35.1"

    object Kotlin {
        const val version = "1.5.10"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${version}"
        const val jsonSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0"
    }

    object Gradle {
        const val androidBuildPlugin = "com.android.tools.build:gradle:7.0.0-alpha15"
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$daggerHiltVersion"
        const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"

        object VersionsPlugin {
            const val id = "com.github.ben-manes.versions"
            const val version = "0.38.0"
        }
    }

    object AndroidX {
        const val palette = "androidx.palette:palette:1.0.0"
        const val browser = "androidx.browser:browser:1.3.0"
        const val dataStore = "androidx.datastore:datastore-preferences:1.0.0-beta01"

        object Ktx {
            const val core = "androidx.core:core-ktx:1.6.0-alpha03"
        }
    }

    object Compose {
        // Compose is combination of 7 Maven Group Ids within androidx.
        // Each Group contains a targeted subset of functionality, each with it's own set of release notes.
        const val compose_version = "1.0.0-beta08"
        const val compose_activity_version = "1.3.0-beta01"
        const val compose_appcompat_version = "1.3.0"
        const val compose_constraintlayout_version = "1.0.0-alpha07"
        const val compose_navigation_version = "1.0.0-alpha10"
        const val compose_lifecycle_version = "1.0.0-alpha04"
        const val compose_accompanist = "0.8.0"
        const val paging_compose_version = "1.0.0-alpha09"
        const val compose_utils_chrisbanes_coil = "0.6.2"
        const val compose_utils_coil = "1.2.2"

        const val runtime = "androidx.compose.runtime:runtime:$compose_version"
        const val foundation = "androidx.compose.foundation:foundation:$compose_version"
        const val layout = "androidx.compose.foundation:foundation-layout:$compose_version"
        const val ui = "androidx.compose.ui:ui:$compose_version"
        const val uiUtil = "androidx.compose.ui:ui-util:$compose_version"
        const val material = "androidx.compose.material:material:$compose_version"
        const val icons = "androidx.compose.material:material-icons-extended:$compose_version"
        const val animation = "androidx.compose.animation:animation:$compose_version"
        const val tooling = "androidx.compose.ui:ui-tooling:$compose_version"
        const val uiTest = "androidx.compose.ui:ui-test:$compose_version"
        const val uiTestJunit = "androidx.compose.ui:ui-test-junit4:$compose_version"
        const val paging = "androidx.paging:paging-compose:$paging_compose_version"
        const val activity = "androidx.activity:activity-compose:$compose_activity_version"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha04"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha06"

        object Accompanist {
            private const val libraryName = "com.google.accompanist:accompanist"
            private const val version = "0.9.1"

            const val coil = "$libraryName-coil:$version"
            const val insets = "$libraryName-insets:$version"
            const val pager = "$libraryName-pager:$version"
        }
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:$daggerHiltVersion"
        const val androidCompiler = "com.google.dagger:hilt-android-compiler:$daggerHiltVersion"
        const val viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
        const val viewmodelCompiler = "androidx.hilt:hilt-compiler:1.0.0"
    }

    object Retrofit {
        private const val retrofitVersion = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val serializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val mockk = "io.mockk:mockk:1.11.0"
        const val striktAssertion = "io.strikt:strikt-core:0.30.0"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2"
    }
}
