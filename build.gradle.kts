import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("java")
    id("idea")
    id("org.jetbrains.intellij") version "0.5.0" // https://github.com/JetBrains/gradle-intellij-plugin
    id("com.github.ben-manes.versions") version "0.33.0" // https://github.com/ben-manes/gradle-versions-plugin
}

// Import variables from gradle.properties file
val pluginIdeaVersion: String by project
val pluginDownloadIdeaSources: String by project
val pluginInstrumentPluginCode: String by project
val pluginVersion: String by project
val pluginJavaVersion: String by project
val pluginIdeaSandbox: String by project
val pluginEnableBuildSearchableOptions: String by project

group = "lermitage.intellij.extra.icons"
version = pluginVersion

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

intellij {
    downloadSources = pluginDownloadIdeaSources.toBoolean()
    instrumentCode = pluginInstrumentPluginCode.toBoolean()
    pluginName = "Extra Icons"
    setPlugins("AngularJS")
    sandboxDirectory = "${rootProject.projectDir}/${pluginIdeaSandbox}-${pluginIdeaVersion}"
    updateSinceUntilBuild = false
    version = pluginIdeaVersion
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = pluginJavaVersion
        targetCompatibility = pluginJavaVersion
    }
    withType<Test> {
        useJUnitPlatform()
    }
    withType<DependencyUpdatesTask> {
        checkForGradleUpdate = true
        gradleReleaseChannel = "current"
        outputFormatter = "plain"
        outputDir = "build"
        reportfileName = "dependencyUpdatesReport"
        revision = "release"
    }
    runIde {
        jvmArgs = listOf("-Xms768m", "-Xmx2048m")
    }
    buildSearchableOptions {
        enabled = pluginEnableBuildSearchableOptions.toBoolean()
    }
}

fun isNonStable(version: String): Boolean {
    if (listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().endsWith(it) }) {
        return false
    }
    return listOf("alpha", "Alpha", "ALPHA", "b", "beta", "Beta", "BETA", "rc", "RC", "M", "EA", "pr", "atlassian").any {
        "(?i).*[.-]${it}[.\\d-]*$".toRegex().matches(version)
    }
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version)) {
                    println(" - [ ] ${candidate.module}:${candidate.version} candidate rejected")
                    reject("Not stable")
                } else {
                    println(" - [X] ${candidate.module}:${candidate.version} candidate accepted")
                }
            }
        }
    }
}
