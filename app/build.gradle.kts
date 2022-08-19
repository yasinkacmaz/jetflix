import java.nio.charset.Charset
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
}

android {

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compilerVersion
    }

    signingConfigs {
        val propertiesFile = File("signing.properties")
        if (propertiesFile.exists()) {
            val properties = Properties()
            properties.load(propertiesFile.reader(Charset.forName("UTF-8")))
            create("release") {
                storeFile = file(properties.getProperty("KEYSTORE_FILE"))
                storePassword = properties.getProperty("KEYSTORE_PASSWORD")
                keyAlias = properties.getProperty("KEY_ALIAS")
                keyPassword = properties.getProperty("KEY_PASSWORD")
            }
        } else {
            create("release")
        }
    }

    buildTypes {
        getByName("debug") {
            isDefault = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
        getByName("release") {
            isShrinkResources = false
            isMinifyEnabled = false
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        animationsDisabled = true
        emulatorSnapshots {
            enableForTestFailures = true
            maxSnapshotsForTestFailures = 2
            compressSnapshots = false
        }
    }

    packagingOptions.apply {
        resources.excludes.addAll(
            listOf(
                "**/attach_hotspot_windows.dll",
                "META-INF/licenses/**",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
        )
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    // Kotlin
    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.Kotlin.jsonSerialization)

    // AndroidX
    implementation(Dependencies.AndroidX.palette)
    implementation(Dependencies.AndroidX.browser)
    implementation(Dependencies.AndroidX.dataStore)
    implementation(Dependencies.AndroidX.Ktx.core)

    // Compose
    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.layout)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiUtil)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.icons)
    implementation(Dependencies.Compose.animation)
    implementation(Dependencies.Compose.paging)
    implementation(Dependencies.Compose.constraintLayout)
    implementation(Dependencies.Compose.activity)
    implementation(Dependencies.Compose.viewModel)
    implementation(Dependencies.Compose.hiltNavigation)
    implementation(Dependencies.Compose.tooling)

    // Accompanist
    implementation(Dependencies.Compose.Accompanist.pager)
    implementation(Dependencies.Compose.Accompanist.flowLayout)

    // Coil Image
    implementation(Dependencies.coil)

    // Hilt
    implementation(Dependencies.Hilt.android)
    kapt(Dependencies.Hilt.androidCompiler)

    // Network
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.Retrofit.serializationConverter)
    implementation(Dependencies.okHttp)

    // Test
    testImplementation(Dependencies.Test.junit)
    testImplementation(Dependencies.Test.mockk)
    testImplementation(Dependencies.Test.striktAssertion)
    testImplementation(Dependencies.Test.coroutines)
    androidTestImplementation(Dependencies.Compose.uiTest)
    androidTestImplementation(Dependencies.Compose.uiTestJunit)
    androidTestImplementation(Dependencies.Test.junitExt)
}
