plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.avengers.data"
    compileSdk = 34

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

    // implementation("androidx.core:core-ktx:1.9.0")
    implementation(project(":domain"))

    // hilt
    val hiltVersion = "2.48"
    api("com.google.dagger:hilt-android:${hiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${hiltVersion}")

    // retrofit
    val retrofitVersion = "2.9.0"
    api("com.squareup.retrofit2:retrofit:$retrofitVersion")
    api("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // okHttp
    val okHttpVersion = "5.0.0-alpha.2"
    api("com.squareup.okhttp3:okhttp:$okHttpVersion")
    api("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    api("com.squareup.okhttp3:okhttp-urlconnection:$okHttpVersion")

    // datastore preferences
    api("androidx.datastore:datastore-preferences:1.0.0")
    api("androidx.datastore:datastore-core:1.0.0")
}