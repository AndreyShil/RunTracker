plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "my.training.feature.stats"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:strings"))
    implementation(project(":core:core-api"))

    implementation(libs.bundles.androidx.main)
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.androidx.idlingResource)

    implementation(libs.google.material)
    implementation(libs.google.dagger)
    kapt(libs.google.daggerCompiler)

    implementation(libs.github.viewBindingDelegate)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.androidx.junit)
    androidTestImplementation(libs.test.androidx.espresso)
}