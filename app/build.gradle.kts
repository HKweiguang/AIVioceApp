plugins {
    id("com.android.application")
    kotlin("android")
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

        }
        getByName("release") {
            // 自动签名打包
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = false
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
            if(this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
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

    implementation(dependenciesConfig.CORE_KTX)
    implementation(dependenciesConfig.APPCOMPAT)
    implementation(dependenciesConfig.MATERIAL)
    implementation(dependenciesConfig.CONSTRAINTLAYOUT)
}