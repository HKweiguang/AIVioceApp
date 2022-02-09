plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = AppConfig.compileSdk
    buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        ndk {
            abiFilters.apply {
                add("arm64-v8a")
                add("armeabi-v7a")
            }
        }

        // ARouter
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
    }

    // 签名配置
    signingConfigs {
        register("release") {
            // 别名
            keyAlias = "vioce"
            // 别名密码
            keyPassword = "123456"
            // 密码
            storePassword = "123456"
            // 路径
            storeFile = file("../vioce.jks")
        }
    }

    // 编译类型
    buildTypes {
        getByName("debug") {
            // 自动签名打包
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("release") {
            // 自动签名打包
            signingConfig = signingConfigs.getByName("release")

            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // 输出类型
    applicationVariants.all {
        // 编译类型
        val buildType = this.buildType.name
        outputs.all {
            // 输出APK
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                if (buildType == "release") {
                    this.outputFileName = "AI_V${defaultConfig.versionName}_$buildType.apk"
                }
            }
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":lib_base"))

    if (!ModuleConfig.isApp) {
        implementation(project(":module_app_manager"))
        implementation(project(":module_constellation"))
        implementation(project(":module_developer"))
        implementation(project(":module_joke"))
        implementation(project(":module_map"))
        implementation(project(":module_setting"))
        implementation(project(":module_voice_setting"))
        implementation(project(":module_weather"))
    }

    kapt(DependenciesConfig.AROUTER_COMPILER)
}