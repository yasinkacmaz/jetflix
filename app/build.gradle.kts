import org.jetbrains.kotlin.gradle.plugin.PLUGIN_CLASSPATH_CONFIGURATION_NAME
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
}

//FIXME those mark to detect android tools build version let AS 4.0.X~4.2.X to support Compose
//def android_builder_version = "4.0.1"
//
//ext {
//    android_builder_main_version = Integer.parseInt(android_builder_version.split("\\.")[0])
//    android_builder_mid_version = Integer.parseInt(android_builder_version.split("\\.")[1])
//}

android {
//    this.rootProject.buildscript.configurations.classpath
//        .resolvedConfiguration
//        .firstLevelModuleDependencies.
//        each {
//            def name = it.name
//                    if (name.contains('com.android.tools.build:gradle')) {
//                        def moduleVersion = it.moduleVersion
//                                if (moduleVersion.contains("-")) {
//                                    def alphaversionArray = moduleVersion.split("-")[0]
//                                    def versionArray = alphaversionArray.toString().split("\\.")
//                                    ext.android_builder_main_version = Integer.parseInt(versionArray[0])
//                                    ext.android_builder_mid_version = Integer.parseInt(versionArray[1])
//                                } else {
//                                    version = moduleVersion
//                                    android_builder_version = moduleVersion
//                                    ext.android_builder_main_version = Integer.parseInt(android_builder_version.split("\\.")[0])
//                                    ext.android_builder_mid_version = Integer.parseInt(android_builder_version.split("\\.")[1])
//                                }
//                    }
//        }
    defaultConfig {
        applicationId = "com.yasinkacmaz.jetflix"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        compileSdk = Versions.compileSdk
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    if (ext.android_builder_main_version >= 7 || (ext.android_builder_main_version > 4 && ext.android_builder_mid_version > 1)) {
        buildFeatures {
            compose true
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compose_version
        kotlinCompilerVersion Dependencies.Kotlin.version
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        animationsDisabled = true
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
    add(PLUGIN_CLASSPATH_CONFIGURATION_NAME, "androidx.compose.compiler:compiler:${Dependencies.Compose.compose_version}")
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
    debugImplementation(Dependencies.Compose.tooling)

    // Accompanist
    implementation(Dependencies.Compose.Accompanist.coil)
    implementation(Dependencies.Compose.Accompanist.insets)
    implementation(Dependencies.Compose.Accompanist.pager)

    // Hilt
    implementation(Dependencies.Hilt.android)
    kapt(Dependencies.Hilt.androidCompiler)
    implementation(Dependencies.Hilt.viewmodel)
    kapt(Dependencies.Hilt.viewmodelCompiler)

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
}
