buildscript {

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath(libs.gradle)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.google.services)
        classpath(libs.org.jacoco.core)
        classpath(libs.kotlin.allopen)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.ktlint.gradle)
    }
}

plugins {
    java
    idea
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    // Verbose output for usage of deprecated APIs
    tasks.withType<JavaCompile> {
        options.compilerArgs = mutableListOf("-Xlint:deprecation")
    }

    // Prevent wildcard dependencies
    // Code in groovy below
    // https://gist.github.com/JakeWharton/2066f5e4f08fbaaa68fd
    // modified Wharton's code for kts
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

    // Apply sample.gradle with project ext values
    apply(rootProject.file("distribution/keys/sample.gradle"))
}

evaluationDependsOnChildren()

val initialCleanup by tasks.registering {
    val cleanTasks = getProjectTask(rootProject, "clean")
    val uninstallTasks = getProjectTask(rootProject, "uninstallAll")
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

release {
    mustRunAfter(testing)
}

fun getProjectTask(project: Project, taskName: String): MutableSet<Task> {
    val tasks = project.getTasksByName(taskName, true)
    if (tasks.isEmpty()) {
        throw IllegalArgumentException("Task $taskName not found")
    }
    return tasks
}

val continuousIntegration by tasks.registering {
    dependsOn(initialCleanup)
    dependsOn(testing)
    dependsOn(release)
}