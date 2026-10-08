plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.kotlinx.serialization.json)
                implementation("io.insert-koin:koin-core:4.2.2")
                implementation("io.insert-koin:koin-compose:4.2.2")
                implementation(libs.coil.compose)
                implementation(libs.coil.network.okhttp)
                implementation(libs.androidx.compose.material.icons.extended)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("io.insert-koin:koin-core:4.2.2")
                implementation("io.insert-koin:koin-compose:4.2.2")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.techliexai.management.MainKt"
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )
            packageName = "Management"
            packageVersion = "1.0.0"
        }
    }
}
