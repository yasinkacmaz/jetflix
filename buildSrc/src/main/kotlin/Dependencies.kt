object Dependencies {
    const val coil = "dev.chrisbanes.accompanist:accompanist-coil:0.5.1"
    const val insets = "dev.chrisbanes.accompanist:accompanist-insets:0.5.1"
    const val okHttp = "com.squareup.okhttp3:okhttp:4.9.0"
    const val ktlint = "com.pinterest:ktlint:0.39.0"

    object Kotlin {
        const val version = "1.4.30"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${version}"
        const val jsonSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:1.4.21"
    }

    object Gradle {
        const val androidBuildPlugin = "com.android.tools.build:gradle:7.0.0-alpha06"
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:2.28-alpha"
        const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerializationPlugin= "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
    }

    object AndroidX {
        const val palette = "androidx.palette:palette:1.0.0"
        const val browser = "androidx.browser:browser:1.2.0"
        const val dataStore = "androidx.datastore:datastore-preferences:1.0.0-alpha04"

        object Ktx {
            private const val lifecycleVersion = "2.2.0"

            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
            const val core = "androidx.core:core-ktx:1.5.0-alpha02"
        }
    }

    object Compose {
        // Compose is combination of 7 Maven Group Ids within androidx.
        // Each Group contains a targeted subset of functionality, each with it's own set of release notes.
        const val version = "1.0.0-alpha12"
        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val layout = "androidx.compose.foundation:foundation-layout:$version"
        const val ui = "androidx.compose.ui:ui:$version"
        const val uiUtil = "androidx.compose.ui:ui-util:$version"
        const val material = "androidx.compose.material:material:$version"
        const val icons = "androidx.compose.material:material-icons-extended:$version"
        const val animation = "androidx.compose.animation:animation:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val paging = "androidx.paging:paging-compose:1.0.0-alpha07"
        const val uiTest = "androidx.compose.ui:ui-test:$version"
        const val uiTestJunit = "androidx.compose.ui:ui-test-junit4:$version"
        const val activity = "androidx.activity:activity-compose:1.3.0-alpha02"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha01"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha02"
    }

    object Hilt {
        private const val daggerHiltVersion = "2.29.1-alpha"
        private const val daggerHiltViewModelVersion = "1.0.0-alpha02"

        const val android = "com.google.dagger:hilt-android:$daggerHiltVersion"
        const val androidCompiler = "com.google.dagger:hilt-android-compiler:$daggerHiltVersion"
        const val viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:$daggerHiltViewModelVersion"
        const val viewmodelCompiler = "androidx.hilt:hilt-compiler:$daggerHiltViewModelVersion"
    }

    object Retrofit {
        private const val retrofitVersion = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val serializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Test {
        const val junit = "junit:junit:4.13"
        const val mockk = "io.mockk:mockk:1.10.3"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2"
        const val strikt = "io.strikt:strikt-core:0.28.0"
    }
}
