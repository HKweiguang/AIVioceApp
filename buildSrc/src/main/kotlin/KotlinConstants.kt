object KotlinConstants {
    // Gradle 版本
    const val gradle_version = "7.0.4"
    // Kotlin 版本
    const val kotlin_version = "1.6.10"
}

object AppConfig {
    const val compileSdk = 31
    const val buildToolsVersion = "31.0.0"
    const val applicationId = "com.shimmer.aivioceapp"
    const val minSdk = 21
    const val targetSdk = 31
    const val versionCode = 1
    const val versionName = "1.0"
}

object dependenciesConfig {
    const val CORE_KTX = "androidx.core:core-ktx:1.7.0"
    const val APPCOMPAT = "androidx.appcompat:appcompat:1.4.0"
    const val MATERIAL = "com.google.android.material:material:1.4.0"
    const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:2.1.2"

    //EventBus
    const val EVENTBUS = "org.greenrobot:eventbus:3.3.1"

    //ARouter
    const val AROUTER = "com.alibaba:arouter-api:1.5.2"
    const val AROUTER_COMPILER = "com.alibaba:arouter-compiler:1.5.2"

    //Permissions
    const val PERMISSION = "com.yanzhenjie:permission:2.0.3"

    //Retrofit
    const val RETROFIT = "com.squareup.retrofit2:retrofit:2.8.1"
    const val RETROFIT_GSON = "com.squareup.retrofit2:converter-gson:2.8.1"

    //Lottie
    const val LOTTIE = "com.airbnb.android:lottie:3.4.0"

    //刷新
    const val REFRESH_KERNEL = "com.scwang.smart:refresh-layout-kernel:2.0.1"
    const val REFRESH_HEADER = "com.scwang.smart:refresh-header-classics:2.0.1"
    const val REFRESH_FOOT = "com.scwang.smart:refresh-footer-classics:2.0.1"

    //图表
    const val CHART = "com.github.PhilJay:MPAndroidChart:v3.1.0"
}

object ModuleConfig {

    const val isApp = false

    const val MODULE_APP_MANAGER = "com.shimmer.module_app_manager"
    const val MODULE_CONSTELLATION = "com.shimmer.module_constellation"
    const val MODULE_DEVELOPER = "com.shimmer.module_developer"
    const val MODULE_JOKE = "com.shimmer.module_joke"
    const val MODULE_MAP = "com.shimmer.module_map"
    const val MODULE_SETTING = "com.shimmer.module_setting"
    const val MODULE_VOICE_SETTING = "com.shimmer.module_voice_setting"
    const val MODULE_WEATHER = "com.shimmer.module_weather"
}