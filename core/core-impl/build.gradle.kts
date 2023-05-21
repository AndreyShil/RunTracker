plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.8.10"
}

android {
    namespace = "my.training.core.core_impl"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:core-api"))

    implementation(libs.androidx.securityCrypto)
    implementation(libs.bundles.androidx.main)
    implementation(libs.bundles.androidx.room)
    kapt(libs.androidx.roomCompiler)

    implementation(libs.google.dagger)
    kapt(libs.google.daggerCompiler)

    implementation(platform(libs.google.firebaseBom))
    implementation(libs.google.firebaseStorage)

    implementation(platform(libs.squareup.okhhtp.bom))
    implementation(libs.squareup.okhhtp)
    implementation(libs.squareup.okhhtp.loggingInterceptor)
    implementation(libs.squareup.retrofit)

    implementation(libs.bundles.serialization)
}