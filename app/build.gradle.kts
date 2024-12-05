import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id ("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
}

fun getLocalProperty(key: String): String? {
    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    localPropertiesFile.inputStream().use { properties.load(it) }
    return properties.getProperty(key)
}

android {
    namespace = "com.example.nutripal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.nutripal"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val buildConfigFields = mapOf(
            "GOOGLE_CLIENT_ID" to getLocalProperty("GOOGLE_CLIENT_ID"),
            "GEMINI_BASE_URL" to getLocalProperty("GEMINI_BASE_URL"),
            "GEMINI_API_KEY" to getLocalProperty("GEMINI_API_KEY"),
            "NEWS_BASE_URL" to getLocalProperty("NEWS_BASE_URL"),
            "NEWS_API_KEY" to getLocalProperty("NEWS_API_KEY")
        )

        buildConfigFields.forEach { (key, value) ->
            buildConfigField("String", key, "\"$value\"")
        }

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
        buildConfig = true
        mlModelBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation (libs.androidx.foundation)

    //tflite
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)

    implementation(libs.play.services.auth.v2000)
    implementation(libs.androidx.runner)
    implementation(libs.androidx.espresso.core)
    implementation(libs.play.services.vision.common)
    implementation(libs.play.services.auth.v2000)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.tensorflow.lite.gpu)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.ui)
    implementation(libs.material.icons.extended)

    //injection
    implementation ("com.google.dagger:hilt-android:2.52")
    kapt ("com.google.dagger:hilt-compiler:2.52")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation(libs.androidx.navigation.compose)


    implementation(libs.androidx.ui.v140)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.coil.compose)

    //auth
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation (libs.google.firebase.auth.ktx)
    implementation (libs.firebase.core)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.play.services.auth.v2000)

    implementation (libs.androidx.material)

    //status bar
    implementation (libs.google.accompanist.systemuicontroller)

    //api call
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)

    //splash screen
    implementation (libs.androidx.core.splashscreen)

    //ocr
    implementation (libs.play.services.mlkit.text.recognition)

    //camerax
    val cameraxVersion = "1.3.1"
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")

    //cropping
    implementation(libs.ucrop)
    implementation("com.vanniktech:android-image-cropper:4.6.0")
}

kapt {
    correctErrorTypes = true
}