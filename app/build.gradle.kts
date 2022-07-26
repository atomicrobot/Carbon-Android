import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

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
    id("com.google.gms.google-services")
}

val appVersion = "1.0"
val minSdkVersion = 21
val targetSdkVersion = 32
val compileSdkVersion = 32
var versionCode = 1
if (project.hasProperty("buildNumber")) {
    val buildNumber = Integer.parseInt(project.property("buildNumber").toString())
    versionCode = buildNumber
}
val version = versionCode
var versionFingerprint = "\"DEV\""
if (project.hasProperty("fingerprint")) {
    versionFingerprint = "\"" + project.property("fingerprint").toString() + "\""
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    defaultConfig {
        applicationId = "com.atomicrobot.carbon"

        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
        compileSdk = 32
        multiDexEnabled = true

        versionCode = version
        versionName = "\"$appVersion b$version\""

        buildConfigField("String", "VERSION_FINGERPRINT", versionFingerprint)
        /*versionName requires null check while versionFingerprint does not, not sure why this is
        the behavior if someone knows what's up here or wants something to investigate*/
        buildConfigField("String", "VERSION_NAME", versionName!!)

        proguardFiles("proguard-android.txt", "proguard-rules.pro")
        testInstrumentationRunner = "com.atomicrobot.carbon.CustomAppTestRunner"
    }

    signingConfigs {
        // If you are creating signing keys, consider setting up Google Play App Signing!
        // See: https://developer.android.com/studio/publish/app-signing.html#google-play-app-signing
        create("release") {
            apply(rootProject.file("distribution/keys/sample.gradle"))
            storeFile = rootProject.file("sampleKeystore")
            storePassword = "sampleKeystorePassword"
            keyAlias = "sampleKeyAlias"
            keyPassword = "sampleKeyPassword"
        }
        // Use debug.keystore in this project so that debug version works with AR"s Carbon web link
        // setup. You can safely remove this section if you are not using web linking within your app
        getByName("debug") {
            storeFile = rootProject.file("distribution/keys/debug.keystore")
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storePassword = "android"
        }
    }

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
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isTestCoverageEnabled = true
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isTestCoverageEnabled = true
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources {
            excludes += ("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    namespace = "com.atomicrobot.carbon"
    testOptions {
        animationsDisabled = true

        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

kapt {
    correctErrorTypes = true
    generateStubs = true
}

// App dependency versions
val appCompatVersion = "1.4.1"
val composeActVersion = "1.4.0"
val composeFoundationVersion = "1.2.0-beta02"
val composeVersion = "1.1.1"
val composeVmVersion = "2.4.1"
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
Brought in from build.gradle (carbon-android)
 */
val firebaseBomVersion = "28.0.0"
val kotlinVersion = "1.6.10"
val hiltVersion = "2.40.1"
val navigationVersion = "1.0.0"
val jacocoVersion = "0.8.6"

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:$firebaseBomVersion"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Add the Firebase Crashlytics SDK.
    implementation(
        "com.google.firebase:firebase-crashlytics:" +
            "$rootProject.ext.firebase_crashlytics_version"
    )

    // Recommended: Add the Google Analytics SDK.
    implementation(
        "com.google.firebase:firebase-analytics:" +
            "$rootProject.ext.firebase_analytics_version"
    )

    implementation("org.jacoco:org.jacoco.core:$jacocoVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    implementation("androidx.test.ext:junit-ktx:1.1.3")
//    kapt ("androidx.databinding:databinding-compiler:$rootProject.ext.android_plugin_version")
    // Need this because of Kotlin

    // Android/Google libraries
    implementation("androidx.core:core-ktx:$coreVersion")
    implementation(
        "androidx.constraintlayout:constraintlayout:" +
            "$constraintLayoutVersion"
    )
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("androidx.fragment:fragment-ktx:$appCompatVersion")
    implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")
    implementation("androidx.cardview:cardview:$supportVersion")
    implementation("androidx.annotation:annotation:$supportVersion")
    implementation("com.google.android.material:material:$materialVersion")

    implementation(
        "com.google.android.gms:play-services-base:" +
            "$playServicesVersion"
    )

    implementation(
        "androidx.lifecycle:lifecycle-runtime-ktx:" +
            "$lifecycleRuntimeVersion"
    )
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:" +
            "$lifecycleRuntimeVersion"
    )

    // Integration with activities
    implementation("androidx.activity:activity-compose:$composeActVersion")
    // Compose Material Design
    implementation("androidx.compose.material:material:$composeVersion")
    // Animations
    implementation("androidx.compose.animation:animation:$composeVersion")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    // Integration with ViewModels
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:" +
            "$composeVmVersion"
    )
    // UI Tests
    androidTestImplementation(
        "androidx.compose.ui:ui-test-junit4:" +
            "$composeVersion"
    )

    implementation(
        "androidx.compose.foundation:foundation:" +
            "$composeFoundationVersion"
    )

    // App architecture - Dagger core
    implementation("com.google.dagger:dagger:$hiltVersion")
    kapt("com.google.dagger:dagger-compiler:$hiltVersion")

    // Dagger -android
    api("com.google.dagger:dagger-android:$hiltVersion")
    api("com.google.dagger:dagger-android-support:$hiltVersion")
    kapt("com.google.dagger:dagger-android-processor:$hiltVersion")

    // App architecture - Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    androidTestImplementation(
        "com.google.dagger:hilt-android-testing:" +
            "$hiltVersion"
    )
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // JSON
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

    // Navigation
    implementation(
        "android.arch.navigation:navigation-fragment-ktx:" +
            "$navigationVersion"
    )
    implementation("android.arch.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-compose:$navVersion")
    implementation(
        "androidx.hilt:hilt-navigation-compose:" +
            "$composeHiltNavigationVersion"
    )

    // Networking - HTTP
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:$okHttpVersion")

    // Networking - REST
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")

    // Monitoring - Timber (logging)
    implementation("com.jakewharton.timber:timber:$timberVersion")

    // Monitoring - Leak Canary
    debugImplementation(
        "com.squareup.leakcanary:leakcanary-android:" +
            "$leakCanaryVersion"
    )

    // Unit test
    testImplementation("junit:junit:$junitVersion")
    testImplementation("androidx.test:rules:$androidTestSupportVersion")
    testImplementation("androidx.test:core:$androidTestSupportVersion")
    testImplementation("androidx.test.ext:junit:$junitTestVersion")
    testImplementation("org.robolectric:robolectric:$robolectricVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("com.nhaarman:mockito-kotlin-kt1.1:$mockitoKotlinVersion")
    testImplementation("com.squareup.okhttp3:mockwebserver:$okHttpVersion")

    // Android JUnit Runner, JUnit Rules, and Espresso
    androidTestImplementation("androidx.test:runner:$androidTestSupportVersion")
    androidTestImplementation("androidx.test:rules:$androidTestSupportVersion")
    androidTestImplementation("androidx.test:core:$androidTestSupportVersion")
    androidTestImplementation("androidx.test.ext:junit:$junitTestVersion")
    androidTestImplementation(
        "androidx.test.espresso:espresso-core:" +
            "$espressoVersion"
    )
    androidTestImplementation(
        "androidx.test.espresso:espresso-contrib:" +
            "$espressoVersion"
    )

    androidTestImplementation("org.mockito:mockito-android:$mockitoVersion")
    androidTestImplementation(
        "com.nhaarman:mockito-kotlin-kt1.1:" +
            "$mockitoKotlinVersion"
    )
    androidTestImplementation(
        "com.github.fabioCollini:DaggerMock:" +
            "$daggerMockVersion"
    )
}

jacoco {
    toolVersion = jacocoVersion
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
    }
}
// would not build as a private val
val fileFilter = mutableSetOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
    /* Parcelize */
    "**/*Creator.*",
    /* Data binding */
    "**/*Binding*.*",
    "**/BR.**",
    /* Dagger */
    "**/*_MembersInjector.*",
    "**/*_Factory.*",
    "**/*_*Factory.*",
    "**/Dagger*Component*.*",
    "**/Dagger*Subcomponent*.*",
    "**/devsettings/**/*.*"
)
private val classDirectoriesTree = fileTree(project.buildDir) {
    include(
        ""
    )
    exclude(fileFilter)
}

private val sourceDirectoriesTree = fileTree("${project.buildDir}") {
    include(
        "src/main/java/**",
        "src/main/kotlin/**"
    )
}
private val executionDataTree = fileTree(project.buildDir) {
    include(
        "outputs/code_coverage/**/*.ec",
        "jacoco/jacocoTestReportDebug.exec",
        "jacoco/testDebugUnitTest.exec",
        "jacoco/test.exec"
    )
}
fun JacocoReportsContainer.reports() {
    xml.required.set(true)
    html.required.set(true)
}
fun JacocoCoverageVerification.setDirectories() {
    sourceDirectories.setFrom(sourceDirectoriesTree)
    classDirectories.setFrom(classDirectoriesTree)
    executionData.setFrom(executionDataTree)
}
fun JacocoReport.setDirectories() {
    sourceDirectories.setFrom(sourceDirectoriesTree)
    classDirectories.setFrom(classDirectoriesTree)
    executionData.setFrom(executionDataTree)
}

if (tasks.findByName("jacocoTestReport") == null) {

    tasks.register<JacocoReport>("jacocoTestReport") {
        description = "Code coverage report for both Android and Unit tests."
        dependsOn("testDebugUnitTest", "createDebugCoverageReport")
        reports {
            reports()
        }
        setDirectories()
    }
}
if (tasks.findByName("jacocoAndroidCoverageVerification") == null) {
    tasks.register<JacocoCoverageVerification>("jacocoAndroidCoverageVerification") {
        description = "Code coverage verification for Android both Android and Unit tests."
        dependsOn("testDebugUnitTest", "createDebugCoverageReport")
        violationRules {
            rule {
                limit {
                    counter = "INSTRUCTIONAL"
                    value = "COVEREDRATIO"
                    minimum = "0.5".toBigDecimal()
                }
            }
        }
        setDirectories()
    }
}

// Kotlin plugin for testing
allOpen {
    annotation("dagger.Module")
    annotation("com.atomicrobot.carbon.Mockable")
}
apply(plugin = "com.google.gms.google-services")
//

ktlint {
    debug.set(true)
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    disabledRules.set(setOf("final-newline"))
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.SARIF)
    }
    kotlinScriptAdditionalPaths {
        include(fileTree("scripts/"))
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

repositories {
    mavenCentral()
}