buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.application")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
    id("jacoco")
    id("kotlin-allopen")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("org.jlleitschuh.gradle.ktlint")
    id("pmd")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.composeVersion
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

dependencies {

    annotationProcessor("androidx.room:room-compiler:${Dependencies.roomVersion}")

    implementation("androidx.camera:camera-camera2:${Dependencies.cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${Dependencies.cameraxVersion}")
    implementation("androidx.camera:camera-mlkit-vision:${Dependencies.cameraxMlkitVersion}")
    implementation("androidx.compose.material:material:${Dependencies.composeVersion}")
    implementation("androidx.compose.material:material-icons-extended:${Dependencies.composeVersion}")
    implementation("androidx.compose.ui:ui:${Dependencies.composeVersion}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Dependencies.composeVersion}")
    implementation("androidx.constraintlayout:constraintlayout-compose:${Dependencies.composeConstraintVersion}")
    implementation("androidx.core:core-ktx:${Dependencies.coreVersion}")
    implementation("androidx.core:core-splashscreen:${Dependencies.splashVersion}")
    implementation("androidx.navigation:navigation-compose:${Dependencies.composeNavigationVersion}")
    implementation("androidx.room:room-ktx:${Dependencies.roomVersion}")
    implementation("androidx.room:room-runtime:${Dependencies.roomVersion}")

    implementation("com.google.accompanist:accompanist-systemuicontroller:${Dependencies.googleAccompanistVersion}")
    implementation("com.google.android.gms:play-services-base:${Dependencies.playServicesVersion}")
    implementation(platform("com.google.firebase:firebase-bom:${Dependencies.firebaseBomVersion}"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.mlkit:barcode-scanning:${Dependencies.mlBarcodeScannerVersion}")

    implementation("com.jakewharton.timber:timber:${Dependencies.timberVersion}")

    implementation("com.squareup.moshi:moshi-kotlin:${Dependencies.moshiVersion}")
    implementation("com.squareup.okhttp3:okhttp:${Dependencies.okHttpVersion}")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:${Dependencies.okHttpVersion}")
    implementation("com.squareup.retrofit2:adapter-rxjava2:${Dependencies.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-moshi:${Dependencies.retrofitVersion}")
    implementation("com.squareup.retrofit2:retrofit:${Dependencies.retrofitVersion}")

    implementation("io.insert-koin:koin-android:${Dependencies.koinVersion}")
    implementation("io.insert-koin:koin-androidx-compose:${Dependencies.koinVersion}")
    implementation("io.noties.markwon:core:${Dependencies.markwonVersion}")
    implementation("io.reactivex.rxjava2:rxandroid:${Dependencies.rxAndroidVersion}")

    kapt("androidx.room:room-compiler:${Dependencies.roomVersion}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Dependencies.moshiVersion}")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Dependencies.composeVersion}")
    androidTestImplementation("androidx.test:core:${Dependencies.androidTestSupportVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Dependencies.espressoVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Dependencies.espressoVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Dependencies.junitTestVersion}")
    androidTestImplementation("androidx.test:rules:${Dependencies.androidTestSupportVersion}")
    androidTestImplementation("androidx.test:runner:${Dependencies.androidTestSupportVersion}")
    androidTestImplementation("com.nhaarman:mockito-kotlin-kt1.1:${Dependencies.mockitoKotlinVersion}")
    androidTestImplementation("org.mockito:mockito-android:${Dependencies.mockitoVersion}")

    debugImplementation("androidx.compose.ui:ui-test-manifest:${Dependencies.composeVersion}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Dependencies.composeVersion}")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Dependencies.leakCanaryVersion}")

    testImplementation("junit:junit:${Dependencies.junitVersion}")
    testImplementation("androidx.test:rules:${Dependencies.androidTestSupportVersion}")
    testImplementation("androidx.test:core:${Dependencies.androidTestSupportVersion}")
    testImplementation("androidx.test.ext:junit:${Dependencies.junitTestVersion}")
    testImplementation("org.mockito:mockito-core:${Dependencies.mockitoVersion}")
    testImplementation("com.nhaarman:mockito-kotlin-kt1.1:${Dependencies.mockitoKotlinVersion}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Dependencies.okHttpVersion}")
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
