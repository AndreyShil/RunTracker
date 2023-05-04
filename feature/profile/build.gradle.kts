plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    kotlin("kapt")
}

android {
    namespace = "my.training.feature.profile"
    compileSdk = 33

    defaultConfig {
        minSdk = 23

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
    implementation(project(":core:iconpack"))
    implementation(project(":core:strings"))
    implementation(project(":core:core-api"))

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)

    implementation(libs.androidx.nav.fragment)
    implementation(libs.androidx.nav.ui)

    implementation(libs.google.material)
    implementation(libs.google.dagger)
    kapt(libs.google.daggerCompiler)

    implementation(libs.coil)

    implementation(platform(libs.google.firebaseBom))
    implementation(libs.google.firebaseStorage)

    implementation(libs.github.viewBindingDelegate)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.androidx.junit)
    androidTestImplementation(libs.test.androidx.espresso)
}