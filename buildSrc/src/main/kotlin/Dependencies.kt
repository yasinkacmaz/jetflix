object Dependencies {
    const val kotlin = "1.4.10"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlin}"
    const val coil = "dev.chrisbanes.accompanist:accompanist-coil:0.3.3.1"
    const val okHttp = "com.squareup.okhttp3:okhttp:4.9.0"
    const val gson = "com.google.code.gson:gson:2.8.6"
    const val ktlint = "com.pinterest:ktlint:0.39.0"

    object Gradle {
        const val androidBuildPlugin = "com.android.tools.build:gradle:4.2.0-alpha15"
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:2.28-alpha"
        const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin}"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val palette = "androidx.palette:palette:1.0.0"
        const val browser = "androidx.browser:browser:1.2.0"

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
        const val version = "1.0.0-alpha07"
        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val layout = "androidx.compose.foundation:foundation-layout:$version"
        const val ui = "androidx.compose.ui:ui:$version"
        const val material = "androidx.compose.material:material:$version"
        const val icons = "androidx.compose.material:material-icons-extended:$version"
        const val animation = "androidx.compose.animation:animation:$version"
        const val tooling = "androidx.ui:ui-tooling:$version"
        const val test = "androidx.ui:ui-test:$version"
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
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    }

    object Test {
        const val junit = "junit:junit:4.13"
    }
}
