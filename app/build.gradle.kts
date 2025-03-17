import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.gradleKtlint)
    alias(libs.plugins.hilt)
    alias(libs.plugins.composeCompiler)
    id("kotlin-kapt")
}

android {
    namespace = "com.checkout.components.sampleapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.checkout.components.sampleapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Provide credential from local.properties
        Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }.run {
            buildConfigField(
                "String",
                "SANDBOX_PUBLIC_KEY",
                this["sandbox.components.public_key"].toString(),
            )
            buildConfigField(
                "String",
                "SANDBOX_SECRET_KEY",
                this["sandbox.components.secret_key"].toString(),
            )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = false
    }
}

dependencies {
    // android
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    // compose
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.material)

    debugImplementation(libs.ui.tooling)
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
    // Network
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.adapters)
    implementation(libs.moshi.kotlin)
    implementation(libs.logging.interceptor)
    // Flow SDK
    implementation(libs.checkout.android.components)
}
