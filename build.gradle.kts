import java.io.StringWriter
import com.adarshr.gradle.testlogger.theme.ThemeType
import com.github.benmanes.gradle.versions.reporter.PlainTextReporter
import com.github.benmanes.gradle.versions.reporter.result.Result
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    id("jacoco")
    id("org.jetbrains.intellij") version "1.5.2" // https://github.com/JetBrains/gradle-intellij-plugin and https://lp.jetbrains.com/gradle-intellij-plugin/
    id("com.github.ben-manes.versions") version "0.42.0" // https://github.com/ben-manes/gradle-versions-plugin
    id("com.adarshr.test-logger") version "3.2.0" // https://github.com/radarsh/gradle-test-logger-plugin
    id("biz.lermitage.oga") version "1.1.1"
}

// Import variables from gradle.properties file
val pluginIdeaVersion: String by project
val pluginDownloadIdeaSources: String by project
val pluginInstrumentPluginCode: String by project
val pluginVersion: String by project
val pluginJavaVersion: String by project

val inCI = System.getenv("CI") != null

val twelvemonkeysVersion = "3.8.2"
val junitVersion = "5.8.2"

logger.quiet("Will use IDEA $pluginIdeaVersion and Java $pluginJavaVersion")

group = "lermitage.intellij.extra.icons"
version = pluginVersion

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
}

intellij {
    downloadSources.set(pluginDownloadIdeaSources.toBoolean() && !inCI)
    instrumentCode.set(pluginInstrumentPluginCode.toBoolean())
    pluginName.set("Extra Icons")
    plugins.set(listOf("AngularJS"))
    sandboxDir.set("${rootProject.projectDir}/.idea-sandbox/${shortIdeVersion(pluginIdeaVersion)}")
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
            unresolved.dependencies.removeIf { it.group.toString() == "unzipped.com.jetbrains.plugins" }
            val plainTextReporter = PlainTextReporter(project, revision, gradleReleaseChannel)
            val writer = StringWriter()
            plainTextReporter.write(writer, this)
            logger.quiet(writer.toString().trim())
        }
    }
    runIde {
        jvmArgs("-Xms128m")
        jvmArgs("-Xmx1024m")
        jvmArgs("--add-exports", "java.base/jdk.internal.vm=ALL-UNNAMED")
        // copy over some JVM args from IntelliJ
        jvmArgs("-Dide.no.platform.update=true")
        jvmArgs("-Djdk.attach.allowAttachSelf=true")
        jvmArgs("-Djdk.module.illegalAccess.silent=true")
        jvmArgs("-Dsun.io.useCanonCaches=false")
        jvmArgs("-XX:+UseG1GC")
        jvmArgs("-XX:CICompilerCount=2")
        jvmArgs("-XX:ReservedCodeCacheSize=512m")
        jvmArgs("-XX:SoftRefLRUPolicyMSPerMB=50")

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
fun shortIdeVersion(version: String): String {
    val matcher = Regex("[A-Za-z]+[\\-]?[0-9]+[\\.]{1}[0-9]+")
    return try {
        matcher.findAll(version).map { it.value }.toList()[0]
    } catch (e: Exception) {
        logger.warn("Failed to shorten IDE version $version", e)
        version
    }
}
