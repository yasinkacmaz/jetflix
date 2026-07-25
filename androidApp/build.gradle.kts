import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val applicationName = "com.yasinkacmaz.jetflix"
val applicationVersion = project.findProperty("app.version.name")?.toString()
    ?: error("app.version.name is not defined in gradle.properties")
val applicationVersionCode = project.findProperty("app.version.code")?.toString()?.toIntOrNull()
    ?: error("app.version.code is not defined in gradle.properties")

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

android {
    namespace = applicationName
    compileSdk = 37

    defaultConfig {
        applicationId = applicationName
        minSdk = 28
        targetSdk = 37
        versionCode = applicationVersionCode
        versionName = applicationVersion
    }

    signingConfigs {
        create("release")
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.composeApp)
}
