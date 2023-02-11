import com.adarshr.gradle.testlogger.theme.ThemeType
import com.github.benmanes.gradle.versions.reporter.PlainTextReporter
import com.github.benmanes.gradle.versions.reporter.result.Result
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import org.apache.commons.io.FileUtils
import org.jetbrains.changelog.Changelog
import org.jetbrains.intellij.tasks.RunPluginVerifierTask
import java.util.EnumSet

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    id("jacoco")
    id("org.jetbrains.intellij") version "1.13.0" // https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.changelog") version "2.0.0" // https://github.com/JetBrains/gradle-changelog-plugin
    id("com.github.ben-manes.versions") version "0.45.0" // https://github.com/ben-manes/gradle-versions-plugin
    id("com.adarshr.test-logger") version "3.2.0" // https://github.com/radarsh/gradle-test-logger-plugin
    id("com.jaredsburrows.license") version "0.9.0" // https://github.com/jaredsburrows/gradle-license-plugin
    id("com.osacky.doctor") version "0.8.1" // https://github.com/runningcode/gradle-doctor/
    id("com.palantir.git-version") version "1.0.0" // https://github.com/palantir/gradle-git-version
    id("biz.lermitage.oga") version "1.1.1"
}

val pluginXmlFile = projectDir.resolve("src/main/resources/META-INF/plugin.xml")
val pluginXmlFileBackup = projectDir.resolve("src/main/resources/META-INF/plugin.original.xml")

// Import variables from gradle.properties file
val pluginIdeaVersion: String by project
val pluginDownloadIdeaSources: String by project
val pluginVersion: String by project
val pluginJavaVersion: String by project
val pluginVerifyProductDescriptor: String by project
val testLoggerStyle: String by project
val pluginNeedsLicense: String by project

version = if (pluginVersion == "auto") {
    val versionDetails: Closure<VersionDetails> by extra
    val lastTag = versionDetails().lastTag
    if (lastTag.startsWith("v", ignoreCase = true)) {
        lastTag.substring(1)
    } else {
        lastTag
    }
} else {
    pluginVersion
}

logger.quiet("Will use IDEA $pluginIdeaVersion and Java $pluginJavaVersion. Plugin version set to $version.")

group = "lermitage.intellij.extra.icons"

repositories {
    mavenCentral()
}

val twelvemonkeysVersion = "3.9.4"
val junitVersion = "5.9.2"
val junitPlatformLauncher = "1.9.2"

dependencies {
    implementation("com.twelvemonkeys.imageio:imageio-core:$twelvemonkeysVersion") // https://github.com/haraldk/TwelveMonkeys/releases
    implementation("com.twelvemonkeys.imageio:imageio-batik:$twelvemonkeysVersion") // SVG support

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitPlatformLauncher")
}

intellij {
    downloadSources.set(pluginDownloadIdeaSources.toBoolean() && !System.getenv().containsKey("CI"))
    instrumentCode.set(true)
    pluginName.set("Extra Icons")
    sandboxDir.set("${rootProject.projectDir}/.idea-sandbox/${shortenIdeVersion(pluginIdeaVersion)}")
    updateSinceUntilBuild.set(false)
    version.set(pluginIdeaVersion)
}

changelog {
    headerParserRegex.set("(.*)".toRegex())
    itemPrefix.set("*")
}

testlogger {
    try {
        theme = ThemeType.valueOf(testLoggerStyle)
    } catch (e: Exception) {
        theme = ThemeType.PLAIN
        logger.warn("Invalid testLoggerRichStyle value '$testLoggerStyle', " +
            "will use PLAIN style instead. Accepted values are PLAIN, STANDARD and MOCHA.")
    }
    showSimpleNames = true
}

tasks {
    register("verifyProductDescriptor") {
        // Ensure generated plugin requires a paid license
        doLast {
            val pluginXmlStr = pluginXmlFile.readText()
            if (!pluginXmlStr.contains("<product-descriptor")) {
                throw GradleException("plugin.xml: Product Descriptor is missing")
            }
        }
    }
    register("removeLicenseRestrictionFromPluginXml") {
        // Remove paid license requirement
        doLast {
            logger.warn("----------------------------------------------------------------")
            logger.warn("/!\\ Will build a plugin which doesn't ask for a paid license /!\\")
            logger.warn("----------------------------------------------------------------")
            var pluginXmlStr = pluginXmlFile.readText()
            pluginXmlStr = pluginXmlStr.replace(Regex(
                "<product-descriptor code=\"PEXTRAICONS\" release-date=\"\\d+\" release-version=\"\\d+\"/>"),
                "")
            pluginXmlFileBackup.delete()
            FileUtils.moveFile(pluginXmlFile, pluginXmlFileBackup)
            FileUtils.write(pluginXmlFile, pluginXmlStr, "UTF-8")
            logger.debug("Saved a copy of $pluginXmlFile to $pluginXmlFileBackup")
        }
    }
    register("restorePluginXml") {
        // Task removeLicenseRestrictionFromPluginXml worked with a modified version of plugin.xml file -> restore original file
        doLast {
            FileUtils.copyFile(pluginXmlFileBackup, pluginXmlFile)
            pluginXmlFileBackup.delete()
            logger.debug("Restored original $pluginXmlFile from $pluginXmlFileBackup")
        }
    }
    register("renameDistributionNoLicense") {
        // Rename generated plugin file to mention the fact that no paid license is needed
        doLast {
            val baseName = "build/distributions/Extra Icons-$version"
            val noLicFile = projectDir.resolve("${baseName}-no-license.zip")
            noLicFile.delete()
            FileUtils.moveFile(projectDir.resolve("${baseName}.zip"), noLicFile)
        }
    }
    withType<JavaCompile> {
        sourceCompatibility = pluginJavaVersion
        targetCompatibility = pluginJavaVersion
        options.compilerArgs = listOf("-Xlint:deprecation")
        options.encoding = "UTF-8"
    }
    withType<Test> {
        useJUnitPlatform()
    }
    jacocoTestReport {
        reports {
            dependsOn(test)
            xml.required.set(false)
            csv.required.set(false)
            html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
        }
    }
    withType<DependencyUpdatesTask> {
        checkForGradleUpdate = true
        gradleReleaseChannel = "current"
        revision = "release"
        rejectVersionIf {
            isNonStable(candidate.version)
        }
        outputFormatter = closureOf<Result> {
            unresolved.dependencies.removeIf {
                val coordinates = "${it.group}:${it.name}"
                coordinates.startsWith("unzipped.com") || coordinates.startsWith("com.jetbrains:ideaI")
            }
            PlainTextReporter(project, revision, gradleReleaseChannel)
                .write(System.out, this)
        }
    }
    runIde {
        jvmArgs("-Xms128m")
        jvmArgs("-Xmx1024m")
        autoReloadPlugins.set(false)
        // If any warning or error with missing --add-opens, wait for the next gradle-intellij-plugin's update that should sync
        // with https://raw.githubusercontent.com/JetBrains/intellij-community/master/plugins/devkit/devkit-core/src/run/OpenedPackages.txt
        // or do it manually
    }
    runPluginVerifier {
        // https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html#runpluginverifier-task
        ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
        failureLevel.set(
            EnumSet.complementOf(
                EnumSet.of(
                    // TODO enable COMPATIBILITY_PROBLEMS when this issue is fixed: https://youtrack.jetbrains.com/issue/MP-4724/intellij-plugin-verifier-Plugin-AngularJS-doesnt-have-a-build-compatible-with-IC-222373954-in-Marketplace
                    //RunPluginVerifierTask.FailureLevel.COMPATIBILITY_PROBLEMS,
                    RunPluginVerifierTask.FailureLevel.OVERRIDE_ONLY_API_USAGES,
                    RunPluginVerifierTask.FailureLevel.NON_EXTENDABLE_API_USAGES,
                    RunPluginVerifierTask.FailureLevel.PLUGIN_STRUCTURE_WARNINGS,
                    RunPluginVerifierTask.FailureLevel.INVALID_PLUGIN
                )
            )
        )
    }
    buildSearchableOptions {
        enabled = false
    }
    patchPluginXml {
        if (!pluginNeedsLicense.toBoolean()) {
            dependsOn("removeLicenseRestrictionFromPluginXml")
        } else {
            if (pluginVerifyProductDescriptor.toBoolean()) {
                dependsOn("verifyProductDescriptor")
            }
        }
        changeNotes.set(provider {
            with(changelog) {
                renderItem(getLatest(), Changelog.OutputType.HTML)
            }
        })
    }
    buildPlugin {
        if (!pluginNeedsLicense.toBoolean()) {
            finalizedBy("restorePluginXml", "renameDistributionNoLicense")
        }
    }
}

// Nota: I know dependency locking is totally useless in this project since there is no transitive
// dependencies, but I wanted to experiment this feature :-)
dependencyLocking { // https://docs.gradle.org/current/userguide/dependency_locking.html
    lockMode.set(LockMode.LENIENT)
    // allow build with multiple IDE versions
    ignoredDependencies.add("com.jetbrains:ideaIU")
    ignoredDependencies.add("unzipped.com.jetbrains.plugins:*")
}

configurations {
    compileClasspath {
        resolutionStrategy.activateDependencyLocking()
    }
    runtimeClasspath {
        resolutionStrategy.activateDependencyLocking()
    }
    annotationProcessor {
        resolutionStrategy.activateDependencyLocking()
    }
    testCompileClasspath {
        resolutionStrategy.disableDependencyVerification()
    }
    testRuntimeClasspath {
        resolutionStrategy.disableDependencyVerification()
    }
}

doctor {
    warnWhenNotUsingParallelGC.set(false)
}

fun isNonStable(version: String): Boolean {
    if (listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().endsWith(it) }) {
        return false
    }
    return listOf("alpha", "Alpha", "ALPHA", "b", "beta", "Beta", "BETA", "rc", "RC", "M", "EA", "pr", "atlassian").any {
        "(?i).*[.-]${it}[.\\d-]*$".toRegex().matches(version)
    }
}

/** Return an IDE version string without the optional PATCH number.
 * In other words, replace IDE-MAJOR-MINOR(-PATCH) by IDE-MAJOR-MINOR. */
fun shortenIdeVersion(version: String): String {
    if (version.contains("SNAPSHOT", ignoreCase = true)) {
        return version
    }
    val matcher = Regex("[A-Za-z]+[\\-]?[0-9]+[\\.]{1}[0-9]+")
    return try {
        matcher.findAll(version).map { it.value }.toList()[0]
    } catch (e: Exception) {
        logger.warn("Failed to shorten IDE version $version: ${e.message}")
        version
    }
}
