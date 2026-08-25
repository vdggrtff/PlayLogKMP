import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    android {
       namespace = "com.vdggrtf.playlog.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    
    sourceSets {
        // 1. 💥 ОБЩИЙ КОТЕЛ (Тут живет наша бизнес-логика и UI)
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)

            // KTOR: Общая логика и парсинг JSON
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // KOIN: Общий DI
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.supabase.gotrue)
            implementation(libs.supabase.postgrest)

            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.datastore.preferences.core)
            implementation(libs.okio)

            implementation(libs.navigation.compose)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.material.icons.extended)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
        }
        // 2. 💥 ANDROID (Использует мощный OkHttp)
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.room.paging)
            implementation(libs.koin.android)
        }
        // 3. 💥 IOS (Использует нативный эппловский Darwin)
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        // 4. 💥 DESKTOP (Linux/Windows/Mac - тоже юзаем OkHttp)
        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutinesSwing)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

// 1. Читаем файл local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

// 2. Генерируем безопасный мультиплатформенный BuildConfig
buildConfig {
    // Указываем пакет, где сгенерируется класс
    packageName("com.vdggrtf.playlog")

    // Достаем ключи из файла. Если их нет (например, на сервере GitHub Actions), ставим пустую строку
    val rawgApiKey = localProperties.getProperty("RAWG_API_KEY") ?: ""
    val rawgUrlKey = localProperties.getProperty("RAWG_URL") ?: ""
    val supabaseUrlKey = localProperties.getProperty("SUPABASE_URL") ?: ""
    val supabaseAnonKey = localProperties.getProperty("SUPABASE_ANON_KEY") ?: ""

    val geminiApiKey = localProperties.getProperty("GEMINI_API_KEY") ?: ""

    val raUser = localProperties.getProperty("RA_USER") ?: ""

    val raApiKey = localProperties.getProperty("RA_API_KEY") ?: ""

    val igdbClientId = localProperties.getProperty("IGDB_CLIENT_ID") ?: ""

    val igdbClientSecret = localProperties.getProperty("IGDB_CLIENT_SECRET") ?: ""

    // Генерируем константы
    buildConfigField("String", "RAWG_API_KEY", "\"$rawgApiKey\"")
    buildConfigField("String", "RAWG_URL", "\"$rawgUrlKey\"")
    buildConfigField("String", "SUPABASE_URL", "\"$supabaseUrlKey\"")
    buildConfigField("String", "SUPABASE_ANON_KEY", "\"$supabaseAnonKey\"")
    buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")
    buildConfigField("String", "RA_USER", "\"$raUser\"")
    buildConfigField("String", "RA_API_KEY", "\"$raApiKey\"")
    buildConfigField("String", "IGDB_CLIENT_ID", "\"$igdbClientId\"")
    buildConfigField("String", "IGDB_CLIENT_SECRET", "\"$igdbClientSecret\"")
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    /*add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)*/
    add("kspCommonMainMetadata", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
