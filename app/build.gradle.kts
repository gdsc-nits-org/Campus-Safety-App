plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("kotlin-kapt")

    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.campussafetyapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.campussafetyapp"
        minSdk = 24
        targetSdk = 35
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
        dataBinding=true

    }
}

dependencies {


    // Core Room functionality
    implementation(libs.androidx.room.runtime)

    // Kotlin extensions for Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.core.i18n)
    implementation(libs.androidx.ui.text.android)

    // Annotation processor for Room (required)
    kapt(libs.androidx.room.compiler)

    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")


    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.room.runtime)
    kapt("androidx.room:room-compiler:2.6.1") // Required for annotation processing
    implementation("androidx.room:room-ktx:2.6.1")


    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")
    implementation("androidx.fragment:fragment-ktx:1.6.1")

    val  paging_version = "2.1.2"

    implementation("androidx.paging:paging-runtime:$paging_version") // For Kotlin use paging-runtime-ktx

    // alternatively - without Android dependencies for testing
    testImplementation("androidx.paging:paging-common:$paging_version") // For Kotlin use paging-common-ktx

    // optional - RxJava support
    implementation("androidx.paging:paging-rxjava2:$paging_version") // For Kotlin use paging-rxjava2-ktx

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")

    // Horizontal Calendar View Library
    implementation(libs.view)
   // implementation("com.kizitonwose.calendarview:calendarview:2.0.0")

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.osmdroid)
    implementation(libs.volley)
}