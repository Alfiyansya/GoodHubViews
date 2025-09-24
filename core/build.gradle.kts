import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.alfiansyah.goodhubviews.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        val properties = Properties().apply{
            load(project.rootProject.file("local.properties").inputStream())
        }
        buildConfigField("String", "BASE_URL", "\"${properties.getProperty("BASE_URL")}\"")
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    api(libs.recyclerview)
    api(libs.material)
    api(libs.glide)

    // Room bundles
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    androidTestImplementation(libs.androidx.room.testing)

    // Networking (OkHttp & Moshi)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.moshi.kotlin)

    // Retrofit (HTTP Client)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)

    // Coroutines bundles
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.lifecycle)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Chucker HANYA ke build 'debug'
    debugImplementation(libs.chucker)
    testImplementation(libs.junit)
    // Chuchker versi 'no-op' (kosong) HANYA ke build 'release'
    releaseImplementation(libs.chucker.no.op)
}
