plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = AppConfig.compileSdk
    buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        // ARouter
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }

        consumerProguardFile("consumer-rules.pro")
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(project(":lib_network"))
    api(project(":lib_voice"))

    api(DependenciesConfig.CORE_KTX)
    api(DependenciesConfig.APPCOMPAT)
    api(DependenciesConfig.MATERIAL)
    api(DependenciesConfig.CONSTRAINTLAYOUT)

    api(DependenciesConfig.EVENTBUS)

    api(DependenciesConfig.AROUTER)
    kapt(DependenciesConfig.AROUTER_COMPILER)

    api(DependenciesConfig.PERMISSION)

    api(DependenciesConfig.RETROFIT)
    api(DependenciesConfig.RETROFIT_GSON)

    api(DependenciesConfig.VIEWPAGER)
    //Lottie
    api(DependenciesConfig.LOTTIE)
    //刷新
    api(DependenciesConfig.REFRESH_KERNEL)
    api(DependenciesConfig.REFRESH_HEADER)
    api(DependenciesConfig.REFRESH_FOOT)
    //图表
    api(DependenciesConfig.CHART)
}
