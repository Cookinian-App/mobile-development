import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val authApiUrl: String = gradleLocalProperties(rootDir, providers).getProperty("AUTH_API_URL")
val recipeApiUrl: String = gradleLocalProperties(rootDir, providers).getProperty("RECIPE_API_URL")

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id ("androidx.navigation.safeargs")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.bangkitcapstone.cookinian"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bangkitcapstone.cookinian"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            buildConfigField("String", "AUTH_API_URL", authApiUrl)
            buildConfigField("String", "RECIPE_API_URL", recipeApiUrl)
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)

    // Data Store
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.datastore.preferences)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Room
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.runtime.saved.instance.state)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    ksp(libs.androidx.room.compiler)

    // Glide
    implementation(libs.glide)

    // Circle Image View
    implementation(libs.circleimageview)

    // CameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // Paging
    implementation(libs.androidx.paging.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}