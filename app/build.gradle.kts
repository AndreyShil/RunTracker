import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "my.training.runtracker"
    compileSdk = 33

    defaultConfig {
        applicationId = "my.training.runtracker"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "my.training.runtracker.CustomRunner"

        resValue(
            type = "string",
            name = "map_key",
            value = gradleLocalProperties(rootDir).getProperty("map.api.key") ?: ""
        )
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

    implementation(project(":feature:auth"))
    implementation(project(":feature:tracker"))
    implementation(project(":feature:stats"))
    implementation(project(":feature:profile"))
    implementation(project(":core:ui"))
    implementation(project(":core:iconpack"))
    implementation(project(":core:strings"))
    implementation(project(":core:core-provider"))
    implementation(project(":core:core-api"))

    implementation(libs.bundles.androidx.main)
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.androidx.splashScreen)
    implementation(libs.androidx.idlingResource)

    implementation(libs.google.material)
    implementation(libs.google.dagger)
    kapt(libs.google.daggerCompiler)

    implementation(libs.yandex.maps)

    debugImplementation(libs.test.androidx.fragment.testing)
    androidTestImplementation(libs.bundles.test.ui)
    kaptAndroidTest(libs.google.daggerCompiler)
}