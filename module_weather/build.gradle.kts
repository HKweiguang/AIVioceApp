plugins {
    if (ModuleConfig.isApp) {
        id("com.android.application")
    } else {
        id("com.android.library")
    }
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = AppConfig.compileSdk
    buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        if (ModuleConfig.isApp) {
            if (this is com.android.build.api.dsl.ApplicationDefaultConfig) {
                applicationId = ModuleConfig.MODULE_WEATHER
                versionCode = AppConfig.versionCode
                versionName = AppConfig.versionName
            }
        }
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        if (this is com.android.build.api.dsl.LibraryDefaultConfig)
            consumerProguardFiles("consumer-rules.pro")

        // ARouter
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // 依赖操作
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // 动态替换资源
    sourceSets {
        getByName("main") {
            if (ModuleConfig.isApp) {
                manifest.srcFile("src/main/manifest/AndroidManifest.xml")
            } else {
                manifest.srcFile("src/main/AndroidManifest.xml")
            }
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":lib_base"))

    kapt(dependenciesConfig.AROUTER_COMPILER)
}