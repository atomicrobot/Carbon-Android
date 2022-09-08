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
    defaultConfig {
        applicationId = "com.atomicrobot.carbon"

        minSdk = ConfigVals.minSdkVersion
        targetSdk = ConfigVals.targetSdkVersion
        compileSdk = ConfigVals.compileSdkVersion
        multiDexEnabled = true
        versionCode = version
        versionName = "\"${ConfigVals.appVersion} b$version\""

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
}

dependencies {
    implementation(
        platform("com.google.firebase:firebase-bom:${Dependencies.firebase_bom_version}")
    )
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Add the Firebase Crashlytics SDK.
    implementation(
        "com.google.firebase:firebase-crashlytics:${Dependencies.firebase_crashlytics_version}"
    )

    // Recommended: Add the Google Analytics SDK.
    implementation(
        "com.google.firebase:firebase-analytics:${Dependencies.firebase_analytics_version}"
    )

    implementation("org.jacoco:org.jacoco.core:${Dependencies.jacoco_version}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Dependencies.kotlin_version}")
    implementation("androidx.test.ext:junit-ktx:1.1.3")

    // Android/Google libraries
    implementation("androidx.core:core-ktx:${Dependencies.coreVersion}")
    implementation(
        "androidx.constraintlayout:constraintlayout:${Dependencies.constraintLayoutVersion}"
    )
    implementation("androidx.appcompat:appcompat:${Dependencies.appCompatVersion}")
    implementation("androidx.fragment:fragment-ktx:${Dependencies.appCompatVersion}")
    implementation("androidx.recyclerview:recyclerview:${Dependencies.recyclerViewVersion}")
    implementation("androidx.cardview:cardview:${Dependencies.supportVersion}")
    implementation("androidx.annotation:annotation:${Dependencies.supportVersion}")
    implementation("com.google.android.material:material:${Dependencies.materialVersion}")

    implementation("com.google.android.gms:play-services-base:${Dependencies.playServicesVersion}")

    implementation(
        "androidx.lifecycle:lifecycle-runtime-ktx:${Dependencies.lifecycleRuntimeVersion}"
    )
    implementation("androidx.lifecycle:lifecycle-extensions:${Dependencies.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${Dependencies.lifecycleVersion}")
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Dependencies.lifecycleRuntimeVersion}"
    )

    // Integration with activities
    implementation("androidx.activity:activity-compose:${Dependencies.composeActVersion}")
    // Compose Material Design
    implementation("androidx.compose.material:material:${Dependencies.composeVersion}")
    // Animations
    implementation("androidx.compose.animation:animation:${Dependencies.composeVersion}")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:${Dependencies.composeVersion}")
    // Integration with ViewModels
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Dependencies.composeVmVersion}"
    )
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Dependencies.composeVersion}")

    implementation(
        "androidx.compose.foundation:foundation:${Dependencies.composeFoundationVersion}"
    )

    // App architecture - Hilt
    implementation("com.google.dagger:hilt-android:${Dependencies.hilt_version}")
    kapt("com.google.dagger:hilt-android-compiler:${Dependencies.hilt_version}")
    androidTestImplementation("com.google.dagger:hilt-android-testing:${Dependencies.hilt_version}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${Dependencies.hilt_version}")

    // JSON
    implementation("com.squareup.moshi:moshi-kotlin:${Dependencies.moshiVersion}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Dependencies.moshiVersion}")

    // Navigation
    implementation(
        "android.arch.navigation:navigation-fragment-ktx:${Dependencies.navigation_version}"
    )
    implementation("android.arch.navigation:navigation-ui-ktx:${Dependencies.navigation_version}")
    implementation("androidx.navigation:navigation-compose:${Dependencies.androidNavVersion}")
    implementation(
        "androidx.hilt:hilt-navigation-compose:${Dependencies.composeHiltNavigationVersion}"
    )

    // Networking - HTTP
    implementation("com.squareup.okhttp3:okhttp:${Dependencies.okHttpVersion}")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:${Dependencies.okHttpVersion}")

    // Networking - REST
    implementation("com.squareup.retrofit2:retrofit:${Dependencies.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-moshi:${Dependencies.retrofitVersion}")

    // Monitoring - Timber (logging)
    implementation("com.jakewharton.timber:timber:${Dependencies.timberVersion}")

    // Monitoring - Leak Canary
    debugImplementation(
        "com.squareup.leakcanary:leakcanary-android:${Dependencies.leakCanaryVersion}"
    )

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
    androidTestImplementation("androidx.test:runner:${Dependencies.androidTestSupportVersion}")
    androidTestImplementation("androidx.test:rules:${Dependencies.androidTestSupportVersion}")
    androidTestImplementation("androidx.test:core:${Dependencies.androidTestSupportVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Dependencies.junitTestVersion}")
    androidTestImplementation(
        "androidx.test.espresso:espresso-core:${Dependencies.espressoVersion}"
    )
    androidTestImplementation(
        "androidx.test.espresso:espresso-contrib:${Dependencies.espressoVersion}"
    )

    androidTestImplementation("org.mockito:mockito-android:${Dependencies.mockitoVersion}")
    androidTestImplementation(
        "com.nhaarman:mockito-kotlin-kt1.1:${Dependencies.mockitoKotlinVersion}"
    )
    androidTestImplementation(
        "com.github.fabioCollini:DaggerMock:${Dependencies.daggerMockVersion}"
    )
}

jacoco {
    toolVersion = Dependencies.jacoco_version
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