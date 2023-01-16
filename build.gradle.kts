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
    afterEvaluate {
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

tasks.register("copyGitHooks", Copy::class) {
    description = "Copies the git hooks from /githooks to the .git folder."
    from("${rootDir}/githooks/") {
        include("**/*.sh")
        rename("(.*).sh", "$1")
    }
    into("${rootDir}/.git/hooks")
}

tasks.register("installGitHooks", Exec::class) {
    description = "Installs the pre-commit git hooks from /githooks."
    group = "git hooks"
    workingDir = rootDir
    commandLine = listOf("chmod")
    setArgs(listOf( "-R", "+wrx", ".git/hooks/"))
    dependsOn(setOf("copyGitHooks"))
    doLast {
        logger.info("Git hook installed successfully")
    }
}

afterEvaluate {
    // We install the hook at the first occasion
    tasks["clean"].setDependsOn(setOf("installGitHooks"))
//    tasks["assembleDebug"].setDependsOn(setOf("installGitHooks"))
}
