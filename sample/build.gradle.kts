import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.application")
    id("org.jmailen.kotlinter")
}

kotlin {
    android()
    jvm("desktop")
    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":axis"))
                implementation(project(":selection"))
                implementation(project(":scale"))
                implementation(project(":shape"))
                implementation(kotlin("stdlib"))
                implementation(libs.coroutines.core)
                implementation(libs.datetime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(project(":compose"))
                implementation(project(":element-view"))
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.lifecycle.runtime)
                implementation(libs.coroutines.android)
                implementation(libs.material)
                implementation(compose.foundation)
                implementation(compose.preview)
                implementation(compose.runtime)
                implementation(compose.ui)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(project(":compose"))
                implementation(compose.desktop.currentOs)
                implementation(compose.preview)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(project(":element-view"))
                implementation(libs.coroutines.js)
                implementation(compose.web.core) // required because of the compose plugin, but unused.
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

android {
    compileSdkVersion(libs.versions.android.compile.get())

    defaultConfig {
        minSdkVersion(libs.versions.android.min.get())
        versionCode = 1
        versionName = "sample"
    }

    buildFeatures {
        resValues = true
        viewBinding = true
    }

    lintOptions {
        // Good habits for a real app, but trying to keep the sample project minimal.
        disable += "AllowBackup"
        disable += "MissingApplicationIcon"
        disable += "Overdraw"
        // False positives for some reason
        disable += "MissingClass"
        disable += "UnusedResources"
    }

    sourceSets {
        getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

compose.desktop {
    application {
        mainClass = "ApplicationKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "jvm"
            packageVersion = "1.0.0"
        }
    }
}
