plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.billbill_template"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.billbill_template"
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.cardview:cardview:1.0.0")
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(kotlin("script-runtime"))

    //Gson
    implementation("com.google.code.gson:gson:2.8.8")

    //Indicator
    implementation("me.relex:circleindicator:2.1.6")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit에 Gson 컨버터 추가
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Retrofit에 Coroutine 지원 추가
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")


    // Kotlin Coroutines Core 라이브러리
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    // Android에서 Coroutine을 사용하기 위한 Android 라이브러리
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // OkHttp and Logging Interceptor
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")  // Glide 컴파일러 추가

}