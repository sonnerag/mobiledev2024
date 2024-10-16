plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "vn.edu.usth.weather"
    compileSdk = 34

    defaultConfig {
        applicationId = "vn.edu.usth.weather"
        minSdk = 26
        targetSdk = 26
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("androidx.viewpager:viewpager:1.0.0")
    implementation("androidx.fragment:fragment:1.5.5")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}