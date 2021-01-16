import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("kotlin-android")
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

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        testOptions {
            animationsDisabled = true
        }
    }

    packagingOptions.apply {
        exclude("**/attach_hotspot_windows.dll")
        exclude("META-INF/licenses/**")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.apply {
        jvmTarget = "1.8"
        useIR = true
        freeCompilerArgs = listOf(
            *freeCompilerArgs.toTypedArray(),
            "-Xallow-jvm-ir-dependencies",
            "-Xskip-prerelease-check",
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    // Kotlin
    implementation(Dependencies.kotlinStdLib)

    // AndroidX
    implementation(Dependencies.AndroidX.palette)
    implementation(Dependencies.AndroidX.browser)
    implementation(Dependencies.AndroidX.dataStore)
    implementation(Dependencies.AndroidX.Ktx.core)
    implementation(Dependencies.AndroidX.Ktx.viewmodel)
    implementation(Dependencies.AndroidX.Ktx.livedata)
    implementation(Dependencies.AndroidX.Ktx.activity)

    // Compose
    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.layout)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiUtil)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.icons)
    implementation(Dependencies.Compose.animation)
    implementation(Dependencies.Compose.tooling)
    implementation(Dependencies.Compose.paging)

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
    testImplementation(Dependencies.Test.mockk)
    testImplementation(Dependencies.Test.coroutines)
    testImplementation(Dependencies.Test.strikt)
    androidTestImplementation(Dependencies.Compose.uiTest)
    androidTestImplementation(Dependencies.Compose.uiTestJunit)
}
