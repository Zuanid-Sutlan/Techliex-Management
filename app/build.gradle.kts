plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                
                implementation(libs.gitlive.firebase.firestore)
                implementation(project.dependencies.platform("com.google.firebase:firebase-bom:33.2.0"))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.androidx.datastore.preferences)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.okhttp)
                
                implementation(libs.jetbrains.lifecycle.viewmodel)
                implementation(libs.jetbrains.navigation.compose)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.coroutines.swing)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.lifecycle.runtime.ktx)
                implementation(libs.androidx.activity.compose)
                
                implementation(libs.koin.android)
                implementation(libs.koin.androidx.compose)
                
                implementation(libs.lottie.compose)
            }
        }
    }
}

android {
    namespace = "com.techliexai.management"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.techliexai.management"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Only test dependencies in the top-level block if they aren't multiplatform
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
kotlin {
    sourceSets {
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            languageSettings.optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
        }
    }
}
