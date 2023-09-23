import java.nio.charset.Charset
import java.util.Properties

plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.yasinkacmaz.jetflix"
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
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
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packagingOptions.apply {
        resources.excludes.addAll(
            listOf(
                "**/attach_hotspot_windows.dll",
                "META-INF/licenses/**",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
            ),
        )
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.bundles.androidX)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.io)
    implementation(libs.coil)
    implementation(libs.hilt)
    ksp(libs.hiltCompiler)
    debugImplementation(libs.compose.testManifest)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.androidTest)
}
