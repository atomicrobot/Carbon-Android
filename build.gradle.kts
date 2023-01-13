import org.gradle.internal.os.OperatingSystem
import java.util.Properties

buildscript {

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Dependencies.androidPluginVersion}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Dependencies.firebaseCrashlyticsGradleVersion}")
        classpath("com.google.gms:google-services:${Dependencies.googleServicesVersion}")
        classpath("org.jacoco:org.jacoco.core:${Dependencies.jacocoVersion}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${Dependencies.kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.kotlinVersion}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${Dependencies.ktlintVersion}")
    }
}

plugins {
    java
    idea
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

/**
 * Git Hooks Tasks
 */
tasks.register("setHooksPath", Exec::class) {
    description = "Sets the project's git config's core.hooksPath to the project's git directory"
    commandLine = listOf("git")
    setArgs(listOf("config", "--local", "core.hooksPath", "$rootDir/.git/hooks"))
}


tasks.register("copyGitHooks", Copy::class) {
    description = "Copy's the git tracked githooks to the project's untracked git/hooks directory"
    from("$rootDir/githooks/")
    include("**/*.sh")
    rename { fileName -> fileName.replace(".sh", "") }
    into("$rootDir/.git/hooks")

    dependsOn(setOf("setHooksPath"))
}

/**
 * This task is specific to unix based systems, we don't have execute permission by default
 * and need to explicitly add it in order for the hooks to run.
 */
tasks.register("installGitHooksOsX", Exec::class) {
    description = "Installs git hooks for OsX machines giving +x permissions to the .git/hooks folder"
    workingDir = rootDir
    commandLine = listOf("chmod")
    setArgs(listOf( "-R", "+wrx", ".git/hooks"))
    dependsOn(setOf("copyGitHooks"))
}

/**
 * In order to better evaluate the cost/effect of pre-push linting we want the ability to selectively
 * enable the git hooks. In order to do this we need the key to not be kept in version control.
 *
 * As such, in order to enable said hooks you MUST create a local.properties file in the buildSrc module
 * and add "shouldInstallGitHooks=true".
 *
 * If you wish to disable the hooks you'll need to do the following:
 * 1. Delete your local.properties file OR set "shouldInstallGitHooks=false"
 * AND
 * 2. Delete the [pre-push] file from .git/hooks OR
 *      run "git config --local --unset core.hooksPath" from a terminal in the project directory
 *
 * Failing to do either of these 2 steps will not stop the hooks from running.
 */
val localProps = rootProject.file("local.properties")
if (localProps.exists()) {
    val props = Properties().apply {
        load(localProps.inputStream())
    }
    val shouldInstallGitHooks: Boolean = props.getProperty("shouldInstallGitHooks").toBoolean()
    if (shouldInstallGitHooks) {
        tasks["build"].dependsOn(
            if (OperatingSystem.current().isWindows) "copyGitHooks" else "installGitHooksOsX"
        )
    }
}