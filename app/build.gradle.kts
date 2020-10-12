import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        applicationId("com.yasinkacmaz.jetflix")
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        compileSdkVersion(Versions.compileSdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Dependencies.kotlin
        kotlinCompilerExtensionVersion = Dependencies.Compose.version
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }


    tasks.withType(KotlinCompile::class).configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xallow-jvm-ir-dependencies", "-Xskip-prerelease-check")
        }
    }
}

androidExtensions {
    isExperimental = true
}

kapt {
    correctErrorTypes = true
}

dependencies {
    // Kotlin
    implementation(Dependencies.kotlinStdLib)

    // AndroidX
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.Ktx.core)
    implementation(Dependencies.AndroidX.Ktx.viewmodel)
    implementation(Dependencies.AndroidX.Ktx.livedata)

    // Compose
    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.layout)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.icons)
    implementation(Dependencies.Compose.animation)
    implementation(Dependencies.Compose.tooling)

    // Hilt
    implementation(Dependencies.Hilt.android)
    kapt(Dependencies.Hilt.androidCompiler)
    implementation(Dependencies.Hilt.viewmodel)
    kapt(Dependencies.Hilt.viewmodelCompiler)

    // Network
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.Retrofit.gsonConverter)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.gson)

    // Image
    implementation(Dependencies.coil)

    // Test
    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.AndroidTest.junitExtension)
    androidTestImplementation(Dependencies.AndroidTest.espresso)
}
