import java.lang.IllegalArgumentException
buildscript {
//    ext.kotlin_version = '1.7.10'
//    ext.android_plugin_version = "7.2.1"
//    ext.kotlin_version = "1.6.10"
//    ext.jacoco_version = "0.8.6"
//    ext.navigation_version = "1.0.0"
//    ext.firebase_bom_version = "28.0.0"
//    ext.firebase_crashlytics_version = "18.2.5"
//    ext.firebase_analytics_version = "20.0.0"
//    ext.google_services_version = "4.3.10"
//    ext.firebase_crashlytics_gradle_version = "2.6.1"
//    ext.hilt_version = "2.40.1"
//    ext.ktlint_version = "10.3.0"
    val kotlin_version = "1.6.10"
    val android_plugin_version = "7.2.1"
    val jacoco_version = "0.8.6"
    val navigation_version = "1.0.0"
    val firebase_bom_version = "28.0.0"
    val firebase_crashlytics_version = "18.2.5"
    val firebase_analytics_version = "20.0.0"
    val google_services_version = "4.3.10"
    val firebase_crashlytics_gradle_version = "2.6.1"
    val hilt_version = "2.40.1"
    val ktlint_version = "10.3.0"

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }


    //Non-core plugins?
    dependencies {
        classpath("com.google.gms:google-services:$google_services_version")
        classpath("com.google.firebase:firebase-crashlytics-gradle:$firebase_crashlytics_gradle_version")
        classpath("com.android.tools.build:gradle:$android_plugin_version")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:$navigation_version")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlin_version")
        classpath("org.jacoco:org.jacoco.core:$jacoco_version")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hilt_version")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:$ktlint_version")
    }
}

plugins {
    java
    idea
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

//    Automatically pull down javadocs and sources (if available)
//    apply(plugin = "idea")
//    idea {
//        module {
//            downloadJavadoc = true
//            downloadSources = true
//        }
//    }

    // Verbose output for usage of deprecated APIs
//    tasks.withType(JavaCompile) {
//        options.compilerArgs << "-Xlint:deprecation"
//    }
    tasks.withType<JavaCompile> {
        options.compilerArgs = mutableListOf("-Xlint:deprecation")
    }
}


// Prevent wildcard dependencies
// https://gist.github.com/JakeWharton/2066f5e4f08fbaaa68fd
//allprojects {
//    afterEvaluate { project ->
//        project.configurations.all {
//            resolutionStrategy.eachDependency { DependencyResolveDetails details ->
//                def requested = details.requested
//                if (requested.version.contains("+")) {
//                    throw new GradleException("Wildcard dependency forbidden: ${requested.group}:${requested.name}:${requested.version}")
//                }
//            }
//        }
//    }
//}

//ext {
    // Build (this implementation assumes values are being provided as arguments, perhaps by a build server)
//    appVersion = "1.0"
//    versionFingerprint = project.hasProperty("fingerprint") ? ("\"" + fingerprint + "\"") : "\"DEV\""
//    versionCode = project.hasProperty("buildNumber") ? Integer.parseInt(buildNumber) : 1
//    versionName = "$appVersion b$versionCode"

    // Build settings that are likely to be reused across different modules
//    minSdkVersion = 29
//    targetSdkVersion = 32
//    compileSdkVersion = 32
//}

    evaluationDependsOnChildren()

//task initialCleanup() {
//    def cleanTasks = getProjectTask(this, "clean")
//    def uninstallTasks = getProjectTask(this, "uninstallAll")
//
//    dependsOn cleanTasks
//    dependsOn uninstallTasks
//}

//task testing() {
//    def appProject = subprojects.find { project -> "app" == project.name }
//
//    def unitTestTasks = getProjectTask(appProject, "testDevDebugUnitTest")
//    def integrationTestTasks = getProjectTask(appProject, "jacocoTestReport")
//
//    dependsOn unitTestTasks
//    dependsOn integrationTestTasks
//
//    integrationTestTasks.each { task -> task.mustRunAfter unitTestTasks }
//}
//
//task release() {
//    def appProject = subprojects.find { project -> "app" == project.name }
//
//    def appTasks = getProjectTask(appProject, "assemble")
//
//    dependsOn appTasks
//}
//
//static def getProjectTask(project, taskName) {
//    def tasks = project.getTasksByName(taskName, true)
//    if (tasks == null || tasks.empty) {
//        throw new IllegalArgumentException("Task " + taskName + " not found")
//    }
//    return tasks
//}

fun getProjectTask(project: Project, taskName: String): MutableSet<Task> {
    val tasks = project.getTasksByName(taskName, true)
    if (tasks == null || tasks.isEmpty()) {
        throw IllegalArgumentException("Task $taskName not found")
    }
    return tasks
}
//
//task continuousIntegration() {
//    dependsOn initialCleanup
//    dependsOn testing
//    dependsOn release
//
//    // InitialCleanup first, then testing, then release
//    release.mustRunAfter testing
//}
