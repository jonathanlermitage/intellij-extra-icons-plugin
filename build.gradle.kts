import com.adarshr.gradle.testlogger.theme.ThemeType
import com.github.benmanes.gradle.versions.reporter.PlainTextReporter
import com.github.benmanes.gradle.versions.reporter.result.Result
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import org.apache.commons.io.FileUtils
import org.jetbrains.changelog.Changelog
import org.w3c.dom.Document
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.15.0" // https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.changelog") version "2.2.0" // https://github.com/JetBrains/gradle-changelog-plugin
    id("com.github.ben-manes.versions") version "0.48.0" // https://github.com/ben-manes/gradle-versions-plugin
    id("com.adarshr.test-logger") version "3.2.0" // https://github.com/radarsh/gradle-test-logger-plugin
    id("com.palantir.git-version") version "3.0.0" // https://github.com/palantir/gradle-git-version
    id("com.github.andygoossens.modernizer") version "1.8.0" // https://github.com/andygoossens/gradle-modernizer-plugin
    id("biz.lermitage.oga") version "1.1.1" // https://github.com/jonathanlermitage/oga-gradle-plugin
}

val pluginXmlFile = projectDir.resolve("src/main/resources/META-INF/plugin.xml")
val pluginXmlFileBackup = projectDir.resolve("src/main/resources/META-INF/plugin.original.xml")

// Import variables from gradle.properties file
val pluginDownloadIdeaSources: String by project
val pluginVersion: String by project
val pluginJavaVersion: String by project
val pluginVerifyProductDescriptor: String by project
val testLoggerStyle: String by project
val pluginNeedsLicense: String by project
val pluginLanguage: String by project
val pluginCountry: String by project
val pluginEnableDebugLogs: String by project
val pluginClearSandboxedIDESystemLogsBeforeRun: String by project
val pluginIdeaVersion = detectBestIdeVersion()

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

logger.quiet("Will use IDEA $pluginIdeaVersion and Java $pluginJavaVersion. Plugin version set to $version")

group = "lermitage.intellij.extra.icons"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.0"
val junitPlatformLauncher = "1.10.0"

dependencies {
    implementation("com.github.weisj:jsvg:1.1.0") // https://github.com/weisJ/jsvg

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

modernizer {
    includeTestClasses = true
    // Find exclusion names at https://github.com/gaul/modernizer-maven-plugin/blob/master/modernizer-maven-plugin/src/main/resources/modernizer.xml
    exclusions = setOf("java/util/Optional.get:()Ljava/lang/Object;")
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
            if (logger.isDebugEnabled) {
                logger.debug("Saved a copy of {} to {}", pluginXmlFile, pluginXmlFileBackup)
            }
        }
    }
    register("restorePluginXml") {
        // Task removeLicenseRestrictionFromPluginXml worked with a modified version of plugin.xml file -> restore original file
        doLast {
            FileUtils.copyFile(pluginXmlFileBackup, pluginXmlFile)
            pluginXmlFileBackup.delete()
            if (logger.isDebugEnabled) {
                logger.debug("Restored original {} from {}", pluginXmlFile, pluginXmlFileBackup)
            }
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
    register("clearSandboxedIDESystemLogs") {
        doFirst {
            if (pluginClearSandboxedIDESystemLogsBeforeRun.toBoolean()) {
                val sandboxLogDir = File("${rootProject.projectDir}/.idea-sandbox/${shortenIdeVersion(pluginIdeaVersion)}/system/log/")
                if (sandboxLogDir.exists() && sandboxLogDir.isDirectory) {
                    FileUtils.deleteDirectory(sandboxLogDir)
                    logger.quiet("Deleted sandboxed IDE's log folder $sandboxLogDir")
                }
            }
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

        // avoid JBUIScale "Must be precomputed" error, because IDE is not started (LoadingState.APP_STARTED.isOccurred is false)
        jvmArgs("-Djava.awt.headless=true")
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
        dependsOn("clearSandboxedIDESystemLogs")

        maxHeapSize = "1g" // https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html

        if (pluginLanguage.isNotBlank()) {
            jvmArgs("-Duser.language=$pluginLanguage")
        }
        if (pluginCountry.isNotBlank()) {
            jvmArgs("-Duser.country=$pluginCountry")
        }
        if (System.getProperty("extra-icons.enable.chinese.ui", "false") == "true") {
            jvmArgs("-Dextra-icons.enable.chinese.ui=true")
        }

        // force detection of slow operations in EDT when playing with sandboxed IDE (SlowOperations.assertSlowOperationsAreAllowed)
        jvmArgs("-Dide.slow.operations.assertion=true")

        if (pluginEnableDebugLogs.toBoolean()) {
            systemProperties(
                "idea.log.debug.categories" to "#lermitage.intellij.extra.icons"
            )
        }

        autoReloadPlugins.set(false)

        // If any warning or error with missing --add-opens, wait for the next gradle-intellij-plugin's update that should sync
        // with https://raw.githubusercontent.com/JetBrains/intellij-community/master/plugins/devkit/devkit-core/src/run/OpenedPackages.txt
        // or do it manually
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
    publishPlugin {
        token.set(System.getenv("JLE_IJ_PLUGINS_PUBLISH_TOKEN"))
    }
}

fun isNonStable(version: String): Boolean {
    if (listOf("RELEASE", "FINAL", "GA").any { version.uppercase().endsWith(it) }) {
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

/** Find latest IntelliJ stable version from JetBrains website. Result is cached locally for 24h. */
fun findLatestStableIdeVersion(): String {
    val definitionsUrl = URL("https://www.jetbrains.com/updates/updates.xml")
    var definitionsStr: String
    try {
        val cacheDurationMs = Integer.parseInt(project.findProperty("pluginIdeaVersionCacheDurationInHours") as String) * 60 * 60_000
        val cachedDefinitionsFile = File(System.getProperty("java.io.tmpdir") + "/jle-ij-updates.cache.xml")
        if (cachedDefinitionsFile.exists() && cachedDefinitionsFile.lastModified() < (System.currentTimeMillis() - cacheDurationMs)) {
            logger.quiet("Delete cached file: $cachedDefinitionsFile")
            cachedDefinitionsFile.delete()
        }
        if (cachedDefinitionsFile.exists()) {
            logger.quiet("Find latest stable IDE version from cached file: $cachedDefinitionsFile")
            definitionsStr = Files.readString(cachedDefinitionsFile.toPath())
        } else {
            logger.quiet("Find latest stable IDE version from: $definitionsUrl")
            definitionsStr = readRemoteContent(definitionsUrl)
            Files.writeString(cachedDefinitionsFile.toPath(), definitionsStr, Charsets.UTF_8)
        }
    } catch (e: Exception) {
        logger.warn("Ignore cache and find latest stable IDE version from: $definitionsUrl", e)
        definitionsStr = readRemoteContent(definitionsUrl)
    }
    val builderFactory = DocumentBuilderFactory.newInstance()
    val builder = builderFactory.newDocumentBuilder()
    val xmlDocument: Document = builder.parse(ByteArrayInputStream(definitionsStr.toByteArray()))
    val xPath = XPathFactory.newInstance().newXPath()
    val expression = "/products/product[@name='IntelliJ IDEA']/channel[@id='IC-IU-RELEASE-licensing-RELEASE']/build[1]/@version"
    return xPath.compile(expression).evaluate(xmlDocument, XPathConstants.STRING) as String
}

/** Read a remote file as String. */
fun readRemoteContent(url: URL): String {
    val content = StringBuilder()
    val conn = url.openConnection() as HttpURLConnection
    conn.requestMethod = "GET"
    BufferedReader(InputStreamReader(conn.inputStream)).use { rd ->
        var line: String? = rd.readLine()
        while (line != null) {
            content.append(line)
            line = rd.readLine()
        }
    }
    return content.toString()
}

/** Get IDE version from gradle.properties or, of wanted, find latest stable IDE version from JetBrains website. */
fun detectBestIdeVersion(): String {
    val pluginIdeaVersionFromProps = project.findProperty("pluginIdeaVersion")
    if (pluginIdeaVersionFromProps.toString() == "IC-LATEST-STABLE") {
        return "IC-${findLatestStableIdeVersion()}"
    }
    if (pluginIdeaVersionFromProps.toString() == "IU-LATEST-STABLE") {
        return "IU-${findLatestStableIdeVersion()}"
    }
    return pluginIdeaVersionFromProps.toString()
}
