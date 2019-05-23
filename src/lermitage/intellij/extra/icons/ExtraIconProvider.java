package lermitage.intellij.extra.icons;

import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.Arrays.asList;
import static lermitage.intellij.extra.icons.Model.ofDir;
import static lermitage.intellij.extra.icons.Model.ofFile;

public class ExtraIconProvider extends BaseIconProvider implements DumbAware {
    
    private static final String[] TXT = new String[]{".md", ".txt", ".adoc"};
    private static final String[] CFG = new String[]{".xml", ".yml", ".yaml", ".properties", ".json", ".cfg", ".conf", ".ini", ".txt"};
    private static final String[] YML = new String[]{".yaml", ".yml"};
    
    @NotNull
    public static List<Model> allModels() {
        return asList(
            
            //
            // custom
            //
            ofFile("custom_jle1", "/icons/bash.png", "Custom: do").eq("do"),
            
            //
            // regex (directory)
            //
            ofDir("dir_circleci", "/icons/folder_circleci.png", "Circle CI (regex): '^/\\.circleci$' folder")
                .regex("^/\\.circleci$"),
            ofDir("dir_dependabot", "/icons/folder_dependabot.png", "Dependabot (regex): '^/\\.dependabot$' folder")
                .regex("^/\\.dependabot$"),
            ofDir("dir_docker", "/icons/folder_docker.svg", "Docker (regex): '^/docker$' folder")
                .regex("^/docker$"),
            ofDir("dir_github", "/icons/folder_github.svg", "Github (regex): '^/\\.github$' folder")
                .regex("^/\\.github$"),
            ofDir("dir_gitlab", "/icons/folder_gitlab.svg", "Gitlab (regex): '^/\\.gitlab$' folder")
                .regex("^/\\.gitlab$"),
            ofDir("dir_gradle", "/icons/folder_gradle.svg", "Gradle (regex): '^/gradle$' folder")
                .regex("^/gradle$"),
            ofDir("dir_idea", "/icons/folder_idea.png", "IntelliJ Idea (regex): '^/\\.idea$' folder")
                .regex("^/\\.idea$"),
            ofDir("dir_mvn", "/icons/folder_mvnw.png", "Maven (regex): '^/\\.mvn$' folder")
                .regex("^/\\.mvn$"),
            ofDir("dir_vscode_settings", "/icons/folder_vscode.png", "Visual Studio Code (regex): '^/\\.vscode$' folder")
                .regex("^/\\.vscode$"),
            
            //
            // regex (file)
            //
            ofFile("flyway", "/icons/flyway.png", "Flyway (regex): '.*/db/migration/.*\\.sql'")
                .regex(".*/db/migration/.*\\.sql"),
            
            //
            // file plus its containing folder
            //
            ofFile("dependabot_config", "/icons/dependabot.png", "Dependabot: .dependabot/config.yml")
                .eq("config.yml").parents(".dependabot"),
            ofFile("vscode_settings", "/icons/vscode.png", "Visual Studio Code: .vscode/settings.json")
                .eq("settings.json").parents(".vscode"),
            
            //
            // file plus extension
            //
            ofFile("htaccess", "/icons/apache.png", "Apache: .htaccess")
                .eq(".htaccess"),
            ofFile("appveyor", "/icons/appveyor.png", "Appveyor: appveyor.yml")
                .eq("appveyor.yml"),
            ofFile("archunit", "/icons/archunit.png", "ArchUnit: archunit.properties")
                .eq("archunit.properties"),
            ofFile("author", "/icons/authors.png", "Author: author(.md,.txt,.adoc), authors")
                .eq("author", "authors").mayEnd(TXT),
            ofFile("bamboo", "/icons/bamboo.png", "Bamboo: bamboo.yml")
                .eq("bamboo.yml"),
            ofFile("gradle_main", "/icons/gradle.png", "Gradle: build.gradle, gradle.properties, settings.gradle")
                .eq("build.gradle", "gradle.properties", "settings.gradle"),
            ofFile("gradle_kotlin", "/icons/gradle.png", "Gradle (Kotlin DSL): build.gradle.kts, settings.gradle.kts")
                .eq("build.gradle.kts", "settings.gradle.kts"),
            ofFile("gradle", "/icons/gradlew.svg", "Gradle: gradlew")
                .eq("gradlew"),
            ofFile("gradlew", "/icons/gradlew.svg", "Gradle (Windows): gradlew.bat, gradlew.cmd")
                .eq("gradlew.bat", "gradlew.cmd"),
            ofFile("mvnw_linux", "/icons/bash-mvnw.png", "Maven: mvnw")
                .eq("mvnw"),
            ofFile("berksfile", "/icons/berkshelf.png", "Berkshelf: berksfile")
                .eq("berksfile"),
            ofFile("berksfilelock", "/icons/berkshelflock.png", "Berkshelf: berksfile.lock")
                .eq("berksfile.lock"),
            ofFile("bettercodehub", "/icons/bettercodehub.svg", "Better Code Hub: .bettercodehub.yml")
                .eq(".bettercodehub.yml"),
            ofFile("bower", "/icons/bower.png", "Bower: bower.json, .bowerrc")
                .eq("bower.json", ".bowerrc"),
            ofFile("build", "/icons/build.png", "Build: build(.md,.txt,.adoc), building")
                .eq("build", "building").mayEnd(TXT),
            ofFile("cassandra", "/icons/cassandra.png", "Cassandra: cassandra(.yml,.yaml)")
                .eq("cassandra").mayEnd(YML),
            ofFile("cassandra1", "/icons/cassandra.png", "Cassandra: start by 'cassandra' and end by '.yml,.yaml'")
                .start("cassandra").end(YML),
            ofFile("changelog", "/icons/changelog.png", "Changelog: changelog(.md,.txt,.adoc), changes")
                .eq("changelog", "changes").mayEnd(TXT),
            ofFile("circle", "/icons/circleci.png", "Circle CI: circle.yml")
                .eq("circle.yml"),
            ofFile("circle1", "/icons/circleci.png", "Circle CI: .circleci/config.yml")
                .eq("config.yml").parents(".circleci"),
            ofFile("cirrus", "/icons/cirrus.png", "Cirrus CI: .cirrus.yml")
                .eq(".cirrus.yml"),
            ofFile("mvnw_windows", "/icons/cmd-mvnw.png", "Maven (Windows): mvnw.bat, mvnw.cmd")
                .eq("mvnw.bat", "mvnw.cmd"),
            ofFile("codecov", "/icons/codecov.png", "CodeCov: .codecov.yml, codecov.yml")
                .eq(".codecov.yml", "codecov.yml"),
            ofFile("codefresh", "/icons/codefresh.png", "Codefresh: codefresh.yml")
                .eq("codefresh.yml"),
            ofFile("codacy", "/icons/codacy.png", "Codacy: .codacy(.yml,.yaml)")
                .eq(".codacy").mayEnd(YML),
            ofFile("codeship", "/icons/codeship.png", "Codeship steps: codeship-steps(.xml,.yml,...)")
                .eq("codeship-steps").mayEnd(CFG),
            ofFile("contact", "/icons/contact.png", "Contact: contact(.md,.txt,.adoc), contacts")
                .eq("contact", "contacts").mayEnd(TXT),
            ofFile("contribute", "/icons/contributing.png", "Contribution: contribute(.md,.txt,.adoc), contributing, contribution")
                .eq("contribute", "contributing", "contribution").mayEnd(TXT),
            ofFile("deptective", "/icons/deptective.png", "Deptective: deptective.json")
                .eq("deptective.json"),
            ofFile("docker", "/icons/docker.png", "Docker: dockerfile(.xml,.yml,...), docker-compose")
                .eq("dockerfile", "docker-compose").mayEnd(CFG),
            ofFile("docker1", "/icons/docker.png", "Docker: start by 'dockerfile', 'docker-compose' and end by '.xml,.yml,...'")
                .start("dockerfile", "docker-compose").end(CFG),
            ofFile("dockerignore", "/icons/dockerignore.png", "Docker: .dockerignore")
                .eq(".dockerignore"),
            ofFile("editorconfig", "/icons/editorconfig.png", "EditorConfig: .editorconfig")
                .eq(".editorconfig").mayEnd(CFG),
            ofFile("elastic", "/icons/elasticsearch.png", "Elasticsearch: elastic(.yml,.yaml)")
                .eq("elastic").mayEnd(YML),
            ofFile("elastic1", "/icons/elasticsearch.png", "Elasticsearch: start by 'elastic' and end by '.yml,.yaml'")
                .start("elastic").end(YML),
            ofFile("cerebro", "/icons/elastic-cerebro.png", "Cerebro: cerebro(.conf)")
                .eq("cerebro").mayEnd(".conf"),
            ofFile("cerebro1", "/icons/elastic-cerebro.png", "Cerebro: start by 'cerebro' and end by '.conf'")
                .start("cerebro").end(".conf"),
            ofFile("intellijcodestyle", "/icons/ijbeam.png", "IntelliJ: intellijcodestyle.xml")
                .eq("intellijcodestyle.xml"),
            ofFile("mailmap", "/icons/email.png", "Mailmap: .mailmap")
                .eq(".mailmap"),
            ofFile("msazure", "/icons/msazure.svg", "Microsoft Azure: azure-pipelines.yml")
                .eq("azure-pipelines.yml"),
            ofFile("gatling", "/icons/gatling.png", "Gatling: gatling(.conf)")
                .eq("gatling").mayEnd(".conf"),
            ofFile("gatling1", "/icons/gatling.png", "Gatling: start by 'gatling' and end by '.conf'")
                .start("gatling").end(".conf"),
            ofFile("grunt", "/icons/grunt.png", "Grunt: Gruntfile.js")
                .eq("gruntfile.js"),
            ofFile("git", "/icons/git.png", "Git: .gitattributes, .gitignore, .gitmodules")
                .eq(".gitattributes", ".gitignore", ".gitmodules"),
            ofFile("gitlab", "/icons/gitlab.png", "Gitlab: .gitlab-ci.yml")
                .eq(".gitlab-ci.yml"),
            ofFile("graphqlconfig", "/icons/graphql-config.png", "GraphQL: graphql.config.json")
                .eq("graphql.config.json"),
            ofFile("graphqlschema", "/icons/graphql-schema.png", "GraphQL: graphql.schema.json")
                .eq("graphql.schema.json"),
            ofFile("gocd", "/icons/gocd.png", "Gocd: .gocd(.xml,.yml,...)")
                .eq(".gocd").mayEnd(CFG),
            ofFile("java", "/icons/java.png", "JVM properties (e.g. Tomcat configuration): jvm.properties")
                .eq("jvm.properties"),
            ofFile("jenkins", "/icons/jenkins.png", "Jenkins: jenkinsfile(.xml,.yml,...), jenkins")
                .eq("jenkinsfile", "jenkins").mayEnd(CFG),
            ofFile("jenkins1", "/icons/jenkins.png", "Jenkins: start by 'jenkins and end by '.xml,.yml,...'")
                .start("jenkins").end(CFG),
            ofFile("jenkins2", "/icons/jenkins.png", "Jenkins: start by 'jenkins' and contain no dot")
                .start("jenkins").noDot(),
            ofFile("jsbeautify", "/icons/jsbeautify.png", "JSBeautify: .jsbeautifyrc(.xml,.yml,...)")
                .eq(".jsbeautifyrc").mayEnd(CFG),
            ofFile("jshint", "/icons/jshint.png", "JSHint: .jshintrc(.xml,.yml,...)")
                .eq(".jshintrc").mayEnd(CFG),
            ofFile("junit", "/icons/junit5.png", "JUnit: junit-platform.properties")
                .eq("junit-platform.properties"),
            ofFile("kibana", "/icons/kibana.png", "Kibana: kibana(.xml,.yml,...)")
                .eq("kibana").mayEnd(YML),
            ofFile("kibana1", "/icons/kibana.png", "Kibana: start by 'kibana' and end by '.xml,.yml,...'")
                .start("kibana").end(YML),
            ofFile("kubernetes", "/icons/kubernetes.png", "Kubernetes: kubernetes(.xml,.yml,...)")
                .eq("kubernetes").mayEnd(YML),
            ofFile("kubernetes1", "/icons/kubernetes.png", "Kubernetes: start by 'kubernetes' and end by '.xml,.yml,...'")
                .start("kubernetes").end(YML),
            ofFile("license", "/icons/license.png", "License: license(.md,.txt,.adoc), copying, license_info, additional_license_info")
                .eq("license", "copying", "license_info", "additional_license_info").mayEnd(TXT),
            ofFile("log4j", "/icons/log4j.png", "Log4j: log4j(.xml,.yml,...), log4j-test").eq("log4j", "log4j-test")
                .mayEnd(CFG),
            ofFile("logback", "/icons/logback.png", "Logback: logback(.xml,.yml,...), logback-test")
                .eq("logback", "logback-test").mayEnd(CFG),
            ofFile("logstash", "/icons/logstash.png", "Logstash: logstash(.cfg,.conf,.yml,.yaml)")
                .eq("logstash").mayEnd(".cfg", ".conf", ".yml", ".yaml"),
            ofFile("logstash1", "/icons/logstash.png", "Logstash: start by 'logstash' and end by '.cfg,.conf,.yml,.yaml'")
                .start("logstash").end(".cfg", ".conf", ".yml", ".yaml"),
            ofFile("lombok", "/icons/lombok.png", "Lombok: lombok.config")
                .eq("lombok.config"),
            ofFile("mysql", "/icons/my.png", "MySQL: my.ini")
                .eq("my.ini"),
            ofFile("nginx", "/icons/nginx.png", "Nginx: nginx(.conf)")
                .eq("nginx").mayEnd(".conf"),
            ofFile("nginx1", "/icons/nginx.png", "Nginx: start by 'nginx' and end by '.conf'")
                .start("nginx").end(".conf"),
            ofFile("notice", "/icons/notice.png", "Notice: notice(.md,.txt,.adoc)")
                .eq("notice").mayEnd(TXT),
            ofFile("packageinfojava", "/icons/packageinfojava.png", "Java package info: package-info.java")
                .eq("package-info.java"),
            ofFile("packagejson", "/icons/packagejson.png", "NPM: package.json")
                .eq("package.json"),
            ofFile("packagejsonlock", "/icons/packagejsonlock.png", "NPM: package-lock.json")
                .eq("package-lock.json"),
            ofFile("privacy", "/icons/privacy.png", "Privacy: privacy(.md,.txt,.adoc)")
                .eq("privacy").mayEnd(TXT),
            ofFile("puppet", "/icons/puppet.png", "Puppet: puppet(.conf)")
                .eq("puppet").mayEnd(".conf"),
            ofFile("puppet1", "/icons/puppet.png", "Puppet: start by 'puppet' and end by '.conf'")
                .start("puppet").end(".conf"),
            ofFile("readme", "/icons/readme.png", "Readme: readme(.md,.txt,.adoc), lisezmoi")
                .eq("readme", "lisezmoi").mayEnd(TXT),
            ofFile("redis", "/icons/redis.png", "Redis: redis(.conf)")
                .eq("redis").mayEnd(".conf"),
            ofFile("redis1", "/icons/redis.png", "Redis: start by 'redis' and end by '.conf'")
                .start("redis").end(".conf"),
            ofFile("roadmap", "/icons/roadmap.png", "Roadmap: roadmap(.md,.txt,.adoc)")
                .eq("roadmap").mayEnd(TXT),
            ofFile("testing", "/icons/testing.png", "Test: test(.md,.txt,.adoc), testing")
                .eq("test", "testing").mayEnd(TXT),
            ofFile("todo", "/icons/todo.png", "To-Do: todo(.md,.txt,.adoc)")
                .eq("todo").mayEnd(TXT),
            ofFile("travis", "/icons/travis.png", "Travis CI: .travis.yml")
                .eq(".travis.yml"),
            ofFile("vagrant", "/icons/vagrant.png", "Vagrant: vagrantfile")
                .eq("vagrantfile"),
            ofFile("version", "/icons/version.png", "Version: version(.md,.txt,.adoc)")
                .eq("version").mayEnd(TXT),
            ofFile("zalando", "/icons/zalando.png", "Zalando Zappr: .zappr.yaml")
                .eq(".zappr.yaml"),
            
            //
            // extension only
            //
            ofFile("ext_archive", "/icons/archive.png", "Archive: *.zip, *.7z, *.tar, *.gz, *.bz2")
                .end(".zip", ".7z", ".tar", ".gz", ".bz2"),
            ofFile("ext_adoc", "/icons/asciidoc.png", "Asciidoc: *.adoc, *.asciidoc")
                .end(".adoc", ".asciidoc"),
            ofFile("ext_apk", "/icons/apk.svg", "Android application package (APK): *.apk, *.xapk")
                .end(".apk", ".xapk"),
            ofFile("ext_back", "/icons/backup.png", "Backup: *.versionbackup, *.versionsbackup, *.back, *.backup, *.old, *.prev, *.revert")
                .end(".versionbackup", ".versionsbackup", ".back", ".backup", ".old", ".prev", ".revert"),
            ofFile("ext_sh", "/icons/bash.png", "Bash: *.sh")
                .end(".sh"),
            ofFile("ext_cert", "/icons/certificate.png", "Certificate: *.jks, *.pem, *.crt, *.cert, *.ca-bundle, *.cer, *.p7b, *.p7s, *.pfx")
                .end(".jks", ".pem", ".crt", ".cert", ".ca-bundle", ".cer", ".p7b", ".p7s", ".pfx"),
            ofFile("ext_cmd", "/icons/cmd.png", "Windows script: *.cmd, *.bat, *.ps1")
                .end(".cmd", ".bat", ".ps1"),
            ofFile("ext_csv", "/icons/csv.png", "CSV: *.csv")
                .end(".csv"),
            ofFile("ext_deb", "/icons/deb.png", "Debian package: *.deb")
                .end(".deb"),
            ofFile("ext_form", "/icons/form.png", "Form (e.g. IntelliJ Swing xml form): *.form")
                .end(".form"),
            ofFile("ext_graphqls", "/icons/graphql.png", "GraphQL: *.graphqls")
                .end(".graphqls"),
            ofFile("ext_haxe", "/icons/haxe.png", "Haxe: *.hx")
                .end(".hx"),
            ofFile("ext_haxexml", "/icons/haxehxml.png", "Haxe: *.hxml")
                .end(".hxml"),
            ofFile("ext_http", "/icons/http.png", "HTTP (e.g. IntelliJ HTTP Client queries file): *.http")
                .end(".http"),
            ofFile("ext_iml", "/icons/ijbeam.png", "IntelliJ project: *.iml")
                .end(".iml"),
            ofFile("ext_cfg", "/icons/ini.png", "Configuration: *.ini, *.cfg, *.conf")
                .end(".ini", ".cfg", ".conf"),
            ofFile("ext_jar", "/icons/jar.png", "Java archive: *.jar")
                .end(".jar"),
            ofFile("ext_kdbx", "/icons/keepass.png", "KeePass: *.kdbx")
                .end(".kdbx"),
            ofFile("ext_md", "/icons/markdown.png", "Markdown: *.md")
                .end(".md"),
            ofFile("ext_nsi", "/icons/nsis.png", "NSIS Nullsoft Scriptable Install System: *.nsi")
                .end(".nsi"),
            ofFile("ext_pdf", "/icons/pdf.png", "PDF: *.pdf")
                .end(".pdf"),
            ofFile("ext_rpm", "/icons/rpm.png", "Red Hat package: *.rpm")
                .end(".rpm"),
            ofFile("sass", "/icons/sass.png", "SASS: *.sass, *.scss")
                .end(".sass", ".scss"),
            ofFile("less", "/icons/less.png", "LESS CSS: *.less")
                .end(".less"),
            ofFile("ext_svg", "/icons/svg.png", "SVG: *.svg")
                .end(".svg"),
            ofFile("ext_tf", "/icons/terraform.png", "Terraform: *.hcl, *.tf, *.tf.json")
                .end(".hcl", ".tf", ".tf.json"),
            ofFile("ext_toml", "/icons/toml.png", "TOML: *.toml")
                .end(".toml"),
            ofFile("typescript", "/icons/test-ts.svg", "Typescript: *.spec.ts")
                .end(".spec.ts"),
            ofFile("ext_video", "/icons/video.png", "Video: *.3gp, *.avi, *.flv, *.mkv, *.mp4, *.mpeg, *.ogm, *.webm, ...")
                .end(".3g2", ".3gp", ".avi", ".divx", ".f4a", ".f4b", ".f4p", ".f4v", ".flv", ".m4p",
                    ".m4v", ".mkv", ".mp4", ".mpg", ".mpeg", ".mov", ".ogm", ".ogv", ".vob", ".webm", ".wmv", ".xvid"),
            ofFile("ext_war", "/icons/war.png", "Java WAR: *.war")
                .end(".war"),
            ofFile("ext_libreoffice_calc", "/icons/officedocs/localc.png", "LibreOffice Calc: *.ods")
                .end(".ods"),
            ofFile("ext_libreoffice_draw", "/icons/officedocs/lodraw.png", "LibreOffice Draw: *.odg")
                .end(".odg"),
            ofFile("ext_libreoffice_impress", "/icons/officedocs/loimpress.png", "LibreOffice Impress: *.odp")
                .end(".odp"),
            ofFile("ext_libreoffice_math", "/icons/officedocs/lomath.png", "LibreOffice Math: *.odf")
                .end(".odf"),
            ofFile("ext_libreoffice_writer", "/icons/officedocs/lowriter.png", "LibreOffice Writer: *.odt")
                .end(".odt"),
            ofFile("ext_msoffice_excel", "/icons/officedocs/msexcel.png", "MSOffice Excel: *.xls, *.xlsx")
                .end(".xls", ".xlsx"),
            ofFile("ext_msoffice_onenote", "/icons/officedocs/msonenote.png", "MSOffice OneNote: *.one, *.onetoc2")
                .end(".one", ".onetoc2"),
            ofFile("ext_msoffice_powerpoint", "/icons/officedocs/mspowerpoint.png", "MSOffice Powerpoint: *.ppt, *.pptx")
                .end(".ppt", ".pptx"),
            ofFile("ext_msoffice_project", "/icons/officedocs/msproject.png", "MSOffice Project: *.mpd, *.mpp, *.mpt")
                .end(".mpd", ".mpp", ".mpt"),
            ofFile("ext_msoffice_visio", "/icons/officedocs/msvisio.png", "MSOffice Visio: *.vsd, *.vsdx, *.vss, *.vssx, *.vst, *.vstx")
                .end(".vsd", ".vsdx", ".vss", ".vssx", ".vst", ".vstx"),
            ofFile("ext_msoffice_word", "/icons/officedocs/msword.png", "MSOffice Word: *.doc, *.docx")
                .end(".doc", ".docx"),
            
            //
            // generic
            //
            ofFile("docker_generic", "/icons/docker.png", "Docker (generic): start by 'dockerfile'")
                .start("dockerfile")
        );
    }
    
    public ExtraIconProvider() {
        super();
    }
    
    @Override
    protected List<Model> getAllModels() {
        return allModels();
    }
}
