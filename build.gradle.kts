buildscript {

    val android_plugin_version = "7.2.1"
    val kotlin_version = "1.6.10"
    val jacoco_version = "0.8.6"
    val navigation_version = "1.0.0"
    val google_services_version = "4.3.10"
    val firebase_crashlytics_gradle_version = "2.6.1"
    val ktlint_version = "10.3.0"

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.google.gms:google-services:$google_services_version")
        classpath("com.google.firebase:firebase-crashlytics-gradle:$firebase_crashlytics_gradle_version")
        classpath("com.android.tools.build:gradle:$android_plugin_version")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:$navigation_version")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlin_version")
        classpath("org.jacoco:org.jacoco.core:$jacoco_version")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:$ktlint_version")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    // Automatically pull down javadocs and sources (if available)
//    apply(plugin = "idea")
//    idea {
//        module {
//            isDownloadJavadoc = true
//            isDownloadSources = true
//        }
//    }

    // Verbose output for usage of deprecated APIs
    tasks.withType<JavaCompile> {
        options.compilerArgs = mutableListOf("-Xlint:deprecation")
    }
}

//// Disable predexing (enable on build servers)
//project.ext.preDexLibs = !project.hasProperty('disablePreDex')
//subprojects {
//    project.plugins.whenPluginAdded { plugin ->
//        if ("com.android.build.gradle.AppPlugin".equals(plugin.class.name)) {
//            project.android.dexOptions.preDexLibraries = rootProject.ext.preDexLibs
//        } else if ("com.android.build.gradle.LibraryPlugin".equals(plugin.class.name)) {
//            project.android.dexOptions.preDexLibraries = rootProject.ext.preDexLibs
//        }
//    }
//}

// Prevent wildcard dependencies
// https://gist.github.com/JakeWharton/2066f5e4f08fbaaa68fd
//allprojects {
//    afterEvaluate { project ->
//        project.configurations.all {
//            resolutionStrategy.eachDependency { DependencyResolveDetails details ->
//                def requested = details.requested
//                if (requested.version.contains('+')) {
//                    throw new GradleException("Wildcard dependency forbidden: ${requested.group}:${requested.name}:${requested.version}")
//                }
//            }
//        }
//    }
//}


//
//evaluationDependsOnChildren()
//
//task initialCleanup() {
//    def cleanTasks = getProjectTask(this, 'clean')
//    def uninstallTasks = getProjectTask(this, 'uninstallAll')
//
//    dependsOn cleanTasks
//    dependsOn uninstallTasks
//}
//
//task testing() {
//    def appProject = subprojects.find { project -> 'app' == project.name }
//
//    def unitTestTasks = getProjectTask(appProject, 'testDevDebugUnitTest')
//    def integrationTestTasks = getProjectTask(appProject, 'jacocoTestReport')
//
//    dependsOn unitTestTasks
//    dependsOn integrationTestTasks
//
//    integrationTestTasks.each { task -> task.mustRunAfter unitTestTasks }
//}
//
//task release() {
//    def appProject = subprojects.find { project -> 'app' == project.name }
//
//    def appTasks = getProjectTask(appProject, 'assemble')
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
//
//task continuousIntegration() {
//    dependsOn initialCleanup
//    dependsOn testing
//    dependsOn release
//
//    // Static analysis first, then testing, then release
//    testing.mustRunAfter initialCleanup
//    release.mustRunAfter testing
//}