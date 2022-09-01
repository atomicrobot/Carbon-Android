import java.lang.IllegalArgumentException
buildscript {

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    //Non-core plugins?
    dependencies {
        classpath("com.google.gms:google-services:${Dependencies.google_services_version}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Dependencies.firebase_crashlytics_gradle_version}")
        classpath("com.android.tools.build:gradle:${Dependencies.android_plugin_version}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.kotlin_version}")
        classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:${Dependencies.navigation_version}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${Dependencies.kotlin_version}")
        classpath("org.jacoco:org.jacoco.core:${Dependencies.jacoco_version}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Dependencies.hilt_version}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${Dependencies.ktlint_version}")
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
    apply(plugin = "idea")
    idea {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }

    // Verbose output for usage of deprecated APIs
    tasks.withType<JavaCompile> {
        options.compilerArgs = mutableListOf("-Xlint:deprecation")
    }
}

// Prevent wildcard dependencies
// Code in groovy below
// https://gist.github.com/JakeWharton/2066f5e4f08fbaaa68fd
// modified Wharton's code for kts
allprojects {
    afterEvaluate() {
        project.configurations.all {
            resolutionStrategy.eachDependency {
                if (requested.version!!.contains("+")) {
                    throw GradleException("Wildcard dependency forbidden: ${requested.group}:" +
                            "${requested.name}:${requested.version}")
                }
            }
        }
    }
}

evaluationDependsOnChildren()

val initialCleanup by tasks.registering {
    val cleanTasks = getProjectTask(rootProject.project("Carbon-Android"), "clean")
    val uninstallTasks = getProjectTask(rootProject.project("Carbon-Android"), "uninstallAll")
    dependsOn(cleanTasks)
    dependsOn(uninstallTasks)
}

val testing by tasks.registering  {
    val appProject = subprojects.find { project -> "app" == project.name }

    val unitTestTasks = getProjectTask(appProject!!, "testDevDebugUnitTest")
    val integrationTestTasks = getProjectTask(appProject, "jacocoTestReport")

    dependsOn(unitTestTasks)
    dependsOn(integrationTestTasks)

    integrationTestTasks.forEach { task -> task.mustRunAfter(unitTestTasks) }
}

val release by tasks.registering {
    val appProject = subprojects.find { project -> "app" == project.name }

    val appTasks = getProjectTask(appProject!!, "assemble")

    dependsOn(appTasks)
}

fun getProjectTask(project: Project, taskName: String): MutableSet<Task> {
    val tasks = project.getTasksByName(taskName, true)
    if (tasks == null || tasks.isEmpty()) {
        throw IllegalArgumentException("Task $taskName not found")
    }
    return tasks
}

val continuousIntegration by tasks.registering {
    dependsOn(initialCleanup)
    dependsOn(testing)
    dependsOn(release)

//     InitialCleanup first, then testing, then release
//    release {
//        mustRunAfter(testing)
//    }
}
