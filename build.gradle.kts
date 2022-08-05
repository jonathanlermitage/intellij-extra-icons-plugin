import com.adarshr.gradle.testlogger.theme.ThemeType
import com.github.benmanes.gradle.versions.reporter.PlainTextReporter
import com.github.benmanes.gradle.versions.reporter.result.Result
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import java.io.StringWriter

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    id("jacoco")
    id("org.jetbrains.intellij") version "1.8.0" // https://github.com/JetBrains/gradle-intellij-plugin https://lp.jetbrains.com/gradle-intellij-plugin/
    id("com.github.ben-manes.versions") version "0.42.0" // https://github.com/ben-manes/gradle-versions-plugin
    id("com.adarshr.test-logger") version "3.2.0" // https://github.com/radarsh/gradle-test-logger-plugin
    id("com.jaredsburrows.license") version "0.9.0" // https://github.com/jaredsburrows/gradle-license-plugin
    id("com.osacky.doctor") version "0.8.1" // https://github.com/runningcode/gradle-doctor/
    id("com.palantir.git-version") version "0.15.0" // https://github.com/palantir/gradle-git-version
    id("biz.lermitage.oga") version "1.1.1"
}

// Import variables from gradle.properties file
val pluginIdeaVersion: String by project
val pluginDownloadIdeaSources: String by project
val pluginInstrumentPluginCode: String by project
val pluginVersion: String by project
val pluginJavaVersion: String by project
val pluginVerifyProductDescriptor: String by project

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

val twelvemonkeysVersion = "3.8.2"
val junitVersion = "5.9.0"

if (pluginVerifyProductDescriptor.toBoolean()) {
    val pluginXmlStr = projectDir.resolve("src/main/resources/META-INF/plugin.xml").readText()
    if (!pluginXmlStr.contains("<product-descriptor")) {
        throw GradleException("plugin.xml: Product Descriptor is missing")
    }
}

logger.quiet("Will use IDEA $pluginIdeaVersion and Java $pluginJavaVersion. Plugin version set to $version.")

group = "lermitage.intellij.extra.icons"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.twelvemonkeys.imageio:imageio-core:$twelvemonkeysVersion") // https://github.com/haraldk/TwelveMonkeys/releases
    // TODO Apache Batik is bundled with IJ and IJ-based IDEs (tested with PyCharm Community). If needed, see how to
    //  integrate org.apache.xmlgraphics:batik-all:1.14 without failing to load org.apache.batik.anim.dom.SAXSVGDocumentFactory
    implementation("com.twelvemonkeys.imageio:imageio-batik:$twelvemonkeysVersion") // SVG support

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    // TODO check JUnit and Gradle updates and remove this workaround asap
    // gradle 7.5 + JUnit workaround for NoClassDefFoundError: org/junit/platform/launcher/LauncherSessionListener
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.0")
}

intellij {
    downloadSources.set(pluginDownloadIdeaSources.toBoolean() && !System.getenv().containsKey("CI"))
    instrumentCode.set(pluginInstrumentPluginCode.toBoolean())
    pluginName.set("Extra Icons")
    plugins.set(listOf("AngularJS"))
    sandboxDir.set("${rootProject.projectDir}/.idea-sandbox/${shortenIdeVersion(pluginIdeaVersion)}")
    updateSinceUntilBuild.set(false)
    version.set(pluginIdeaVersion)
}

testlogger {
    theme = ThemeType.PLAIN
    showSimpleNames = true
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = pluginJavaVersion
        targetCompatibility = pluginJavaVersion
        options.compilerArgs = listOf("-Xlint:deprecation")
        options.encoding = "UTF-8"
    }
    withType<Test> {
        useJUnitPlatform()

        // TODO check JUnit and Gradle updates and remove this workaround asap
        // gradle 7.5 + JUnit workaround https://docs.gradle.org/7.5/userguide/upgrading_version_7.html#removes_implicit_add_opens_for_test_workers
        jvmArgs("--add-opens=java.base/java.io=ALL-UNNAMED")
        jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
        jvmArgs("--add-opens=java.base/java.util=ALL-UNNAMED")
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
        resolutionStrategy {
            componentSelection {
                all {
                    if (isNonStable(candidate.version)) {
                        logger.debug(" - [ ] ${candidate.module}:${candidate.version} candidate rejected")
                        reject("Not stable")
                    } else {
                        logger.debug(" - [X] ${candidate.module}:${candidate.version} candidate accepted")
                    }
                }
            }
        }
        outputFormatter = closureOf<Result> {
            unresolved.dependencies.removeIf {
                val coordinates = "${it.group}:${it.name}"
                coordinates.startsWith("unzipped.com") || coordinates.startsWith("com.jetbrains:ideaI")
            }
            val plainTextReporter = PlainTextReporter(project, revision, gradleReleaseChannel)
            val writer = StringWriter()
            plainTextReporter.write(writer, this)
            logger.quiet(writer.toString().trim())
        }
    }
    runIde {
        jvmArgs("-Xms128m")
        jvmArgs("-Xmx1024m")
        autoReloadPlugins.set(true)
    }
    runPluginVerifier {
        ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
    }
    buildSearchableOptions {
        enabled = false
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
