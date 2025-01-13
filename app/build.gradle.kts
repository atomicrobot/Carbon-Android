plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
    jacoco
    pmd
}

// Version variables
var appVersion = "1.0"
var buildNumber = 1
var versionFingerprint = "\"DEV\""

/**
 * This implementation assumes versions being provided as arguments, perhaps by a build server
 */
if (project.hasProperty("buildNumber")) {
    buildNumber = Integer.parseInt(project.property("buildNumber").toString())
}

if (project.hasProperty("fingerprint")) {
    versionFingerprint = "\"${project.property("fingerprint")}\""
}

/*
/**
 * Could also consider setting up and using system environment variables from the build server
 * Test by adding these system environment variables to your local machine
 */
if (System.getenv("BITRISE_BUILD_NUMBER") != null) {
    buildNumber = Integer.parseInt(System.getenv("BITRISE_BUILD_NUMBER"))
}

if (System.getenv("BITRISE_VERSION_FINGERPRINT") != null) {
    versionFingerprint = "\"${System.getenv("BITRISE_VERSION_FINGERPRINT")}\""
}
*/

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.atomicrobot.carbon"

        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        multiDexEnabled = true

        versionCode = buildNumber
        versionName = "$appVersion b$buildNumber"

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
            storeFile = rootProject.file(rootProject.extra.get("sampleKeystore") as String)
            storePassword = rootProject.extra.get("sampleKeystorePassword") as String
            keyAlias = rootProject.extra.get("sampleKeyAlias") as String
            keyPassword = rootProject.extra.get("sampleKeyPassword") as String
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

    flavorDimensions.add("app")
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
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
        }
    }

    dataBinding {
        enable = true
    }

    testOptions {
        animationsDisabled = true

        unitTests {
            isIncludeAndroidResources = true
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
    packaging {
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

dependencies {

    annotationProcessor(libs.androidx.room.compiler)

    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.mlkit.vision)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.play.services.base)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.barcode.scanning)

    implementation(libs.timber)

    implementation(libs.moshi.kotlin)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.moshi)
    implementation(libs.retrofit)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.core)
    implementation(libs.rxandroid)

    ksp(libs.androidx.room.compiler)
    ksp(libs.moshi.kotlin.codegen)

    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.android)

    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.leakcanary.android)
    debugImplementation(libs.logging.interceptor)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.rules)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockwebserver)
}

tasks.register<Pmd>("pmd") {}

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
    toolVersion = libs.versions.jacoco.get()
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = mutableListOf("jdk.internal.*")
    }
}
// would not build as a private val
val fileFilter =
    mutableSetOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        // Parcelize
        "**/*Creator.*",
        // Data binding
        "**/*Binding*.*",
        "**/BR.**",
        // Dagger
        "**/*_MembersInjector.*",
        "**/*_Factory.*",
        "**/*_*Factory.*",
        "**/Dagger*Component*.*",
        "**/Dagger*Subcomponent*.*",
        "**/devsettings/**/*.*",
    )
private val classDirectoriesTree =
    fileTree(layout.buildDirectory) {
        include(
            "",
        )
        exclude(fileFilter)
    }

private val sourceDirectoriesTree =
    fileTree("${layout.buildDirectory}") {
        include(
            "src/main/java/**",
            "src/main/kotlin/**",
        )
    }
private val executionDataTree =
    fileTree(layout.buildDirectory) {
        include(
            "outputs/code_coverage/**/*.ec",
            "jacoco/jacocoTestReportDebug.exec",
            "jacoco/testDevDebugUnitTest.exec",
            "jacoco/test.exec",
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
