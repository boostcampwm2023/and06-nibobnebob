plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {

    implementation("javax.inject:javax.inject:1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    // okHttp
    val okHttpVersion = "5.0.0-alpha.2"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:$okHttpVersion")
}