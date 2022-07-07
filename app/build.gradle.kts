import org.codehaus.groovy.runtime.ArrayTypeUtils.dimension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.application")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-allopen")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
    id("jacoco")
}
val appVersion = "1.0"
val minSdkVersion = 21
val targetSdkVersion = 32
val compileSdkVersion = 32
//TODO placeholder values
val versionCode = 1
val versionFingerprint = "DEV"
val versionName = "1.0"

android {
//    compileOptions {
//        sourceCompatibility JavaVersion.VERSION_11
//        targetCompatibility JavaVersion.VERSION_11
//    }
//
//    kotlinOptions {
//        jvmTarget = ("11")
//    }
//

//
    defaultConfig {
        applicationId= "com.atomicrobot.carbon"

        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
        compileSdk = 32
//        multiDexEnabled(true)

        versionCode = versionCode

//        buildConfigField("String", "VERSION_FINGERPRINT", versionFingerprint)
//        buildConfigField("String", "VERSION_NAME", "One")

//        proguardFile getDefaultProguardFile("proguard-android.txt")
//        proguardFile ("proguard-rules.pro")
//
//        testInstrumentationRunner ("com.atomicrobot.carbon.CustomAppTestRunner")
    }
//
//    signingConfigs {
//        // If you are creating signing keys, consider setting up Google Play App Signing!
//        // See: https://developer.android.com/studio/publish/app-signing.html#google-play-app-signing
//        release {
//            apply from: rootProject.file("distribution/keys/sample.gradle")
//
//            storeFile rootProject.file(sampleKeystore)
//            storePassword sampleKeystorePassword
//            keyAlias sampleKeyAlias
//            keyPassword sampleKeyPassword
//        }
//        // Use debug.keystore in this project so that debug version works with AR"s Carbon web link
//        // setup. You can safely remove this section if you are not using web linking within your app
//        debug {
//            storeFile rootProject.file("distribution/keys/debug.keystore")
//            keyAlias ("androiddebugkey")
//            keyPassword ("android")
//            storePassword ("android")
//        }
//    }
//
    flavorDimensions("app")
    productFlavors {
        create("dev") {
            dimension = "app"
            applicationId = "com.atomicrobot.carbon.dev"
        }
        create("prod") {
            dimension = "app"
            applicationId = "com.atomicrobot.carbon"
        }
    }
//
//    buildTypes {
//        debug {
//            minifyEnabled false
//            shrinkResources false
//            testCoverageEnabled true
//        }
//
//        release {
//            minifyEnabled true
//            shrinkResources true
//            signingConfig signingConfigs.release
//        }
//    }
//
    buildFeatures {
        dataBinding = true
        compose = true
    }
//
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
//    packagingOptions {
//        resources {
//            excludes += ("/META-INF/{AL2.0,LGPL2.1}")
//        }
//    }
    namespace = "com.atomicrobot.carbon"
//    testOptions {
//        animationsDisabled true
//
//        unitTests {
//            includeAndroidResources = true
//        }
//    }
}

//kapt {
//    correctErrorTypes = true
//    generateStubs = true
//}


// App dependency versions
val appCompatVersion = "1.4.1"
val compose_act_version = "1.4.0"
val compose_foundation_version = "1.2.0-beta02"
val compose_version = "1.1.1"
val compose_vm_version = "2.4.1"
val supportVersion = "1.0.0"
val playServicesVersion = "17.6.0"
val lifecycleVersion = "2.0.0"
val lifecycleRuntimeVersion = "2.4.0"
val retrofitVersion = "2.9.0"
val okHttpVersion = "4.9.1"
val moshiVersion = "1.13.0"
val coreVersion = "1.7.0"
val constraintLayoutVersion = "2.1.2"
val recyclerViewVersion = "1.2.1"
val materialVersion = "1.4.0"
val timberVersion = "5.0.1"
val leakCanaryVersion = "2.7"
val navVersion = "2.4.2"
val composeHiltNavigationVersion = "1.0.0"

// Test dependency versions
val mockitoVersion = "4.1.0"
val mockitoKotlinVersion = "1.6.0"
val robolectricVersion = "4.7.3"
val androidTestSupportVersion = "1.4.0"
val espressoVersion = "3.3.0"
val junitVersion = "4.13.2"
val junitTestVersion = "1.1.3"
val daggerMockVersion = "0.8.4"

/*
TODO brought in from build.gradle (carbon-android)
Figure out what to do with these
 */
val firebase_bom_version = "28.0.0"
val kotlin_version = "1.6.10"
val hilt_version = "2.40.1"
val navigation_version = "1.0.0"



dependencies {
    implementation(platform("com.google.firebase:firebase-bom:${firebase_bom_version}"))
    implementation ("com.google.firebase:firebase-analytics-ktx")

    // Add the Firebase Crashlytics SDK.
    implementation ("com.google.firebase:firebase-crashlytics:$rootProject.ext.firebase_crashlytics_version")

    // Recommended: Add the Google Analytics SDK.
    implementation ("com.google.firebase:firebase-analytics:$rootProject.ext.firebase_analytics_version")


    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
    implementation ("androidx.test.ext:junit-ktx:1.1.3")
//    kapt ("androidx.databinding:databinding-compiler:$rootProject.ext.android_plugin_version")
    // Need this because of Kotlin

    // Android/Google libraries
    implementation ("androidx.core:core-ktx:$coreVersion")
    implementation ("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation ("androidx.appcompat:appcompat:$appCompatVersion")
    implementation ("androidx.fragment:fragment-ktx:$appCompatVersion")
    implementation ("androidx.recyclerview:recyclerview:$recyclerViewVersion")
    implementation ("androidx.cardview:cardview:$supportVersion")
    implementation ("androidx.annotation:annotation:$supportVersion")
    implementation ("com.google.android.material:material:$materialVersion")

    implementation ("com.google.android.gms:play-services-base:$playServicesVersion")

    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleRuntimeVersion")
    implementation ("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation ("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleRuntimeVersion")

    // Integration with activities
    implementation ("androidx.activity:activity-compose:$compose_act_version")
    // Compose Material Design
    implementation ("androidx.compose.material:material:$compose_version")
    // Animations
    implementation ("androidx.compose.animation:animation:$compose_version")
    // Tooling support (Previews, etc.)
    implementation ("androidx.compose.ui:ui-tooling:$compose_version")
    // Integration with ViewModels
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:$compose_vm_version")
    // UI Tests
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:$compose_version")

    implementation ("androidx.compose.foundation:foundation:$compose_foundation_version")

    // App architecture - Hilt
    implementation ("com.google.dagger:hilt-android:$hilt_version")
    kapt ("com.google.dagger:hilt-android-compiler:$hilt_version")
    androidTestImplementation ("com.google.dagger:hilt-android-testing:$hilt_version")
    kaptAndroidTest ("com.google.dagger:hilt-android-compiler:$hilt_version")

    // JSON
    implementation ("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    kapt ("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

    // Navigation
    implementation ("android.arch.navigation:navigation-fragment-ktx:$navigation_version")
    implementation ("android.arch.navigation:navigation-ui-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-compose:$navVersion")
    implementation ("androidx.hilt:hilt-navigation-compose:$composeHiltNavigationVersion")


    // Networking - HTTP
    implementation ("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation ("com.squareup.okhttp3:okhttp-urlconnection:$okHttpVersion")

    // Networking - REST
    implementation ("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation ("com.squareup.retrofit2:converter-moshi:$retrofitVersion")

    // Monitoring - Timber (logging)
    implementation ("com.jakewharton.timber:timber:$timberVersion")

    // Monitoring - Leak Canary
    debugImplementation ("com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion")

    // Unit test
    testImplementation ("junit:junit:$junitVersion")
    testImplementation ("androidx.test:rules:$androidTestSupportVersion")
    testImplementation ("androidx.test:core:$androidTestSupportVersion")
    testImplementation ("androidx.test.ext:junit:$junitTestVersion")
    testImplementation ("org.robolectric:robolectric:$robolectricVersion")
    testImplementation ("org.mockito:mockito-core:$mockitoVersion")
    testImplementation ("com.nhaarman:mockito-kotlin-kt1.1:$mockitoKotlinVersion")
    testImplementation ("com.squareup.okhttp3:mockwebserver:$okHttpVersion")


    // Android JUnit Runner, JUnit Rules, and Espresso
    androidTestImplementation ("androidx.test:runner:$androidTestSupportVersion")
    androidTestImplementation ("androidx.test:rules:$androidTestSupportVersion")
    androidTestImplementation ("androidx.test:core:$androidTestSupportVersion")
    androidTestImplementation ("androidx.test.ext:junit:$junitTestVersion")
    androidTestImplementation ("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:$espressoVersion")

    androidTestImplementation ("org.mockito:mockito-android:$mockitoVersion")
    androidTestImplementation ("com.nhaarman:mockito-kotlin-kt1.1:$mockitoKotlinVersion")
    androidTestImplementation ("com.github.fabioCollini:DaggerMock:$daggerMockVersion")
}

//jacoco {
//    toolVersion = rootProject.ext.jacoco_version
//}
//
//tasks.withType(Test) {
//    jacoco.includeNoLocationClasses = true
//    jacoco.excludes = ["jdk.internal.*"]
//}
//
//task jacocoTestReport(type: JacocoReport, dependsOn: ["testDevDebugUnitTest", "createDevDebugCoverageReport"]) {
//    reports {
//        xml.enabled = true
//        html.enabled = true
//    }
//
//    def excludes = [
//            "**/R.class",
//            '**/R$*.class',
//            "**/BuildConfig.*",
//            "**/Manifest*.*",
//            "**/*Test*.*",
//            "android/**/*.*",
//            /* Parcelize */
//            "**/*Creator.*",
//            /* Data binding */
//            "**/*Binding*.*",
//            "**/BR.**",
//            /* Dagger */
//            "**/*_MembersInjector.*",
//            "**/*_Factory.*",
//            "**/*_*Factory.*",
//            "**/Dagger*Component*.*",
//            "**/Dagger*Subcomponent*.*",
//            "**/devsettings/**/*.*"
//    ]
//
//    getClassDirectories().setFrom(fileTree(
//            // Java generated classes on Android project (debug build)
//            dir: "$buildDir/intermediates/classes/dev/debug",
//            excludes: excludes
//    ) + fileTree(
//            // Kotlin generated classes on Android project (debug build)
//            dir: "$buildDir/tmp/kotlin-classes/devDebug",
//            excludes: excludes
//    ))
//
//    getSourceDirectories().setFrom(files([
//            "src/main/java",
//            "src/main/kotlin"
//    ]))
//
//    getExecutionData().setFrom(fileTree(dir: project.buildDir, includes: [
//            "jacoco/testDevDebugUnitTest.exec",
//            "outputs/code-coverage/connected/**/*coverage.ec"
//    ]))
//}
//
//// Kotlin plugin for testing
//allOpen {
//    annotation("dagger.Module")
//    annotation("com.atomicrobot.carbon.Mockable")
//}
//apply plugin: ("com.google.gms.google-services")
//
//ktlint {
//    debug = true
//    android = true
//    outputToConsole = true
//    outputColorName = ("RED")
//    disabledRules = ["final-newline"]
//    reporters {
//        reporter ("plain")
//        reporter ("checkstyle")
//        reporter ("sarif")
//    }
//    kotlinScriptAdditionalPaths {
//        include fileTree("scripts/")
//    }
//    filter {
//        exclude("**/generated/**")
//        include("**/kotlin/**")
//    }
//}


