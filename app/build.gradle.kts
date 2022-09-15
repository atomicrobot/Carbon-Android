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
    id("com.google.firebase.crashlytics")
    id("pmd")
    id("checkstyle")
    id("jacoco")
    id("com.google.gms.google-services")
}

var versionCode = 1
if (project.hasProperty("buildNumber")) {
    versionCode = Integer.parseInt(project.property("buildNumber").toString())
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

    compileSdk = ConfigVals.compileSdkVersion

    defaultConfig {
        applicationId = "com.atomicrobot.carbon"

        minSdk = ConfigVals.minSdkVersion
        targetSdk = ConfigVals.targetSdkVersion

        multiDexEnabled = true

        versionCode = version

        buildConfigField("String", "VERSION_FINGERPRINT", versionFingerprint)

        proguardFiles("proguard-android.txt", "proguard-rules.pro")

        testInstrumentationRunner = "com.atomicrobot.carbon.CustomAppTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
            signingConfig = signingConfigs.getByName("release")
        }
    }

    dataBinding {
        isEnabled = true
    }

    testOptions {
        animationsDisabled = true

        unitTests {
            isIncludeAndroidResources = true
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "${Dependencies.composeVersion}"
    }
    packagingOptions {
        resources {
            excludes += ("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    namespace = "com.atomicrobot.carbon"
    lint {
        abortOnError = true
        htmlReport = true
//        lintConfig file("lint.xml")
    }
}

    // App dependency versions
    val appCompatVersion = "1.4.1"
    val supportVersion = "1.0.0"
    val playServicesVersion = "18.0.1"
    val lifecycleVersion = "2.0.0"
    val lifecycleRuntimeVersion = "2.4.1"
    val koinVersion = "3.2.0"
    val retrofitVersion = "2.9.0"
    val okHttpVersion = "4.9.1"
    val moshiVersion = "1.13.0"
    val coreVersion = "1.7.0"
    val constraintLayoutVersion = "2.1.3"
    val recyclerViewVersion = "1.2.1"
    val materialVersion = "1.6.0"
    val rxJavaVersion = "2.2.21"
    val rxAndroidVersion = "2.1.1"
    val timberVersion = "5.0.1"
    val leakCanaryVersion = "2.7"

    // Test dependency versions
    val mockitoVersion = "4.4.0"
    val mockitoKotlinVersion = "1.6.0"
    val robolectricVersion = "4.7.3"
    val androidTestSupportVersion = "1.4.0"
    val espressoVersion = "3.4.0"
    val junitVersion = "4.13.2"
    val junitTestVersion = "1.1.3"


dependencies {
    implementation(platform("com.google.firebase:firebase-bom:${Dependencies.firebaseBomVersion}"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Add the Firebase Crashlytics SDK.
    implementation("com.google.firebase:firebase-crashlytics:${Dependencies.firebaseCrashlyticsVersion}")

    // Recommended: Add the Google Analytics SDK.
    implementation("com.google.firebase:firebase-analytics:${Dependencies.firebaseAnalyticsVersion}")

    implementation("androidx.compose.ui:ui:${Dependencies.composeVersion}")
    // Compose Material Design
    implementation("androidx.compose.material:material:${Dependencies.composeVersion}")
    // Animations
    implementation("androidx.compose.animation:animation:${Dependencies.composeVersion}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Dependencies.composeVersion}")
    // Integration with activities
    implementation("androidx.activity:activity-compose:${Dependencies.composeActVersion}")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Dependencies.composeVersion}")
    // Tooling support (Previews, etc.)
    debugImplementation("androidx.compose.ui:ui-tooling:${Dependencies.composeVersion}")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Dependencies.composeVmVersion}")
    // UI Tests
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Dependencies.composeVersion}")

    implementation("androidx.compose.foundation:foundation:${Dependencies.composeFoundationVersion}")

    /*
     * Starting from Kotlin 1.4 the Kotlin standard library dependency doesn't need to be added
     * explicitly. An implicit dep. w/ the same version as the Kotlin Gradle plugin will
     * implicitly be added.
     *
     * link: https://stackoverflow.com/a/64988522/3591491
     */
    /*implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"*/
    kapt("androidx.databinding:databinding-compiler:${Dependencies.androidPluginVersion}")
    // Need this because of Kotlin

    // Android/Google libraries
    implementation("androidx.core:core-ktx:${Dependencies.coreVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${Dependencies.constraintLayoutVersion}")
    implementation("androidx.appcompat:appcompat:${Dependencies.appCompatVersion}")
    implementation("androidx.recyclerview:recyclerview:${Dependencies.recyclerViewVersion}")
    implementation("androidx.cardview:cardview:${Dependencies.supportVersion}")
    implementation("androidx.annotation:annotation:${Dependencies.supportVersion}")
    implementation("com.google.android.material:material:${Dependencies.materialVersion}")

    implementation("com.google.android.gms:play-services-base:${Dependencies.playServicesVersion}")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Dependencies.lifecycleRuntimeVersion}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Dependencies.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${Dependencies.lifecycleVersion}")

    // App architecture - Koin
    implementation("io.insert-koin:koin-android:${Dependencies.koinVersion}")
    implementation("io.insert-koin:koin-androidx-compose:${Dependencies.koinVersion}")

    // App architecture - RxJava
    implementation("io.reactivex.rxjava2:rxjava:${Dependencies.rxJavaVersion}")
    implementation("io.reactivex.rxjava2:rxandroid:${Dependencies.rxAndroidVersion}")

    // JSON
    implementation("com.squareup.moshi:moshi-kotlin:${Dependencies.moshiVersion}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Dependencies.moshiVersion}")

    // Navigation
    implementation("androidx.navigation:navigation-compose:${Dependencies.composeNavigationVersion}")
    implementation("android.arch.navigation:navigation-fragment-ktx:${Dependencies.navigationVersion}")
    implementation("android.arch.navigation:navigation-ui-ktx:${Dependencies.navigationVersion}")

    // Networking - HTTP
    implementation("com.squareup.okhttp3:okhttp:${Dependencies.okHttpVersion}")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:${Dependencies.okHttpVersion}")

    // Networking - REST
    implementation("com.squareup.retrofit2:retrofit:${Dependencies.retrofitVersion}")
    implementation("com.squareup.retrofit2:adapter-rxjava2:${Dependencies.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-moshi:${Dependencies.retrofitVersion}")

    // Monitoring - Timber (logging)
    implementation("com.jakewharton.timber:timber:${Dependencies.timberVersion}")

    // Monitoring - Leak Canary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Dependencies.leakCanaryVersion}")

    // Unit test
    testImplementation("junit:junit:${Dependencies.junitVersion}")
    testImplementation("androidx.test:rules:${Dependencies.androidTestSupportVersion}")
    testImplementation("androidx.test:core:${Dependencies.androidTestSupportVersion}")
    testImplementation("androidx.test.ext:junit:${Dependencies.junitTestVersion}")
    testImplementation("org.robolectric:robolectric:${Dependencies.robolectricVersion}")
    testImplementation("org.mockito:mockito-core:${Dependencies.mockitoVersion}")
    testImplementation("com.nhaarman:mockito-kotlin-kt1.1:${Dependencies.mockitoKotlinVersion}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Dependencies.okHttpVersion}")


    // Android JUnit Runner, JUnit Rules, and Espresso
    // Android JUnit Runner, JUnit Rules, and Espresso
    androidTestImplementation("androidx.test:runner:${Dependencies.androidTestSupportVersion}")
    androidTestImplementation("androidx.test:rules:${Dependencies.androidTestSupportVersion}")
    androidTestImplementation("androidx.test:core:${Dependencies.androidTestSupportVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Dependencies.junitTestVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Dependencies.espressoVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Dependencies.espressoVersion}")

    androidTestImplementation("org.mockito:mockito-android:${Dependencies.mockitoVersion}")
    androidTestImplementation("com.nhaarman:mockito-kotlin-kt1.1:${Dependencies.mockitoKotlinVersion}")
}

tasks.register<Pmd>("pmd") {

}

tasks.named<Pmd>("pmd").configure {
    dependsOn("assembleDebug")
    ruleSetFiles = files("${project.rootDir}/config/pmd/pmd-ruleset.xml")
    ruleSets = mutableListOf()
    // See http://sourceforge.net/p/pmd/discussion/188193/thread/6e9c6017/ for why this is needed...
    source = fileTree("src/main/java/")
    exclude("**/gen/**")
    reports {
        // html.enabled = true
        // xml.enabled = false
        xml.required.set(false)
        html.required.set(true)
    }
}

jacoco {
    toolVersion = Dependencies.jacocoVersion
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = mutableListOf("jdk.internal.*")
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
        "jacoco/testDevDebugUnitTest.exec",
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
        dependsOn("testDevDebugUnitTest")
        reports {
            reports()
        }
        setDirectories()
    }
}
if (tasks.findByName("jacocoAndroidCoverageVerification") == null) {
    tasks.register<JacocoCoverageVerification>("jacocoAndroidCoverageVerification") {
        description = "Code coverage verification for Android both Android and Unit tests."
        dependsOn("testDevDebugUnitTest")
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
    annotation("com.atomicrobot.carbon.Mockable")
}

apply(plugin = "com.google.gms.google-services")
