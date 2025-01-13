plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.kotlin.allopen) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
    jacoco
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