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
            ofDir("dir_circleci", "/icons/folder_circleci.svg", "Circle CI (regex): '^/\\.circleci$' folder")
                .regex("^/\\.circleci$")
                .eq(".circleci"), // FIXME #13 temp fix
            ofDir("dir_dependabot", "/icons/folder_dependabot.png", "Dependabot (regex): '^/\\.dependabot$' folder")
                .regex("^/\\.dependabot$")
                .eq(".dependabot"), // FIXME #13 temp fix
            ofDir("dir_docker", "/icons/folder_docker.svg", "Docker (regex): '^/docker$' folder")
                .regex("^/docker$")
                .eq("docker"), // FIXME #13 temp fix
            ofDir("dir_github", "/icons/folder_github.svg", "Github (regex): '^/\\.github$' folder")
                .regex("^/\\.github$")
                .eq(".github"), // FIXME #13 temp fix
            ofDir("dir_gitlab", "/icons/folder_gitlab.svg", "Gitlab (regex): '^/\\.gitlab$' folder")
                .regex("^/\\.gitlab$")
                .eq(".gitlab"), // FIXME #13 temp fix
            ofDir("dir_gradle", "/icons/folder_gradle.svg", "Gradle (regex): '^/gradle$' folder")
                .regex("^/gradle$")
                .eq("gradle"), // FIXME #13 temp fix
            ofDir("dir_idea", "/icons/folder_idea.svg", "IntelliJ Idea (regex): '^/\\.idea$' folder")
                .regex("^/\\.idea$")
                .eq(".idea"), // FIXME #13 temp fix
            ofDir("dir_mvn", "/icons/folder_mvnw.svg", "Maven (regex): '^/\\.mvn$' folder")
                .regex("^/\\.mvn$")
                .eq(".mvn"), // FIXME #13 temp fix
            ofDir("dir_vscode_settings", "/icons/folder_vscode.svg", "Visual Studio Code (regex): '^/\\.vscode$' folder")
                .regex("^/\\.vscode$")
                .eq(".vscode"), // FIXME #13 temp fix

            //
            // regex (file)
            //
            ofFile("angular_module_generic", "/icons/angular-module.svg", "AngularJS: *.module.(js|ts)")
                .regex(".*[^a-zA-Z0-9]module\\.(js|ts)"),
            ofFile("angular_component_generic", "/icons/angular-component.svg", "AngularJS: *.(component|controller).(js|ts)")
                .regex(".*[^a-zA-Z0-9](component|controller)\\.(js|ts)"),
            ofFile("angular_service_generic", "/icons/angular-service.svg", "AngularJS: *.service.(js|ts)")
                .regex(".*[^a-zA-Z0-9]service\\.(js|ts)"),
            ofFile("angular_pipe_generic", "/icons/angular-pipe.svg", "AngularJS: *.pipe.(js|ts)")
                .regex(".*[^a-zA-Z0-9]pipe\\.(js|ts)"),
            ofFile("angular_directive_generic", "/icons/angular-directive.svg", "AngularJS: *.directive.(js|ts)")
                .regex(".*[^a-zA-Z0-9]directive(s)?\\.(js|ts)"),
            ofFile("angular_guard_generic", "/icons/angular-guard.svg", "AngularJS: *.guard.(js|ts)")
                .regex(".*[^a-zA-Z0-9]guard\\.(js|ts)"),
            ofFile("angular_resolver_generic", "/icons/angular-resolver.svg", "AngularJS: *.resolver.(js|ts)")
                .regex(".*[^a-zA-Z0-9]resolver\\.(js|ts)"),
            ofFile("angular_spec_generic", "/icons/test-ts.svg", "AngularJS: *.spec.(js|ts)")
                .regex(".*[^a-zA-Z0-9]spec\\.(js|ts)"),
            ofFile("flyway", "/icons/flyway.png", "Flyway (regex): '.*/db/migration/.*\\.sql'")
                .regex(".*/db/migration/.*\\.sql"),
            ofFile("liquibase", "/icons/liquibase.png", "Liquibase (regex): '.*/db/changelog/.*\\.(sql|xml)'")
                .regex(".*/db/changelog/.*\\.(sql|xml)"),

            //
            // file plus its containing folder
            //
            ofFile("dependabot_config", "/icons/dependabot.png", "Dependabot: .dependabot/config.yml")
                .eq("config.yml").parents(".dependabot"),
            ofFile("golwroot", "/icons/glowroot.png", "Glowroot: glowroot/admin.json and glowroot/config.json")
                .eq("admin.json", "config.json").parents("glowroot"),
            ofFile("vscode_settings", "/icons/vscode.svg", "Visual Studio Code: .vscode/settings.json")
                .eq("settings.json").parents(".vscode"),

            //
            // file plus extension
            //
            ofFile("vcskeep", "/icons/keep.svg", "Various VCS: .keep, .gitkeep, .hgkeep, .svnkeep")
                .eq(".keep", ".gitkeep", ".hgkeep", ".svnkeep"),
            ofFile("htaccess", "/icons/apache.png", "Apache: .htaccess")
                .eq(".htaccess"),
            ofFile("appveyor", "/icons/appveyor.png", "Appveyor: appveyor.yml, .appveyor.yml")
                .eq("appveyor.yml", ".appveyor.yml"),
            ofFile("archunit", "/icons/archunit.png", "ArchUnit: archunit.properties")
                .eq("archunit.properties"),
            ofFile("author", "/icons/authors.png", "Author: author(.md,.txt,.adoc), authors")
                .eq("author", "authors").mayEnd(TXT),
            ofFile("bamboo", "/icons/bamboo.png", "Bamboo: bamboo.yml")
                .eq("bamboo.yml"),
            ofFile("cargo", "/icons/cargo.png", "Cargo: cargo.toml")
                .eq("cargo.toml"),
            ofFile("gradle_main", "/icons/gradle.png", "Gradle: build.gradle, gradle.properties, settings.gradle")
                .eq("build.gradle", "gradle.properties", "settings.gradle"),
            ofFile("gradle_kotlin", "/icons/gradle.png", "Gradle (Kotlin DSL): build.gradle.kts, settings.gradle.kts")
                .eq("build.gradle.kts", "settings.gradle.kts"),
            ofFile("gradle", "/icons/gradlew.svg", "Gradle: gradlew")
                .eq("gradlew"),
            ofFile("gradlew", "/icons/gradlew.svg", "Gradle (Windows): gradlew.bat, gradlew.cmd")
                .eq("gradlew.bat", "gradlew.cmd"),
            ofFile("gradle_wrapper", "/icons/gradle.png", "Gradle Wrapper: gradle-wrapper.jar, gradle-wrapper.properties")
                .eq("gradle-wrapper.jar", "gradle-wrapper.properties"),
            ofFile("mvnw_linux", "/icons/bash-mvnw.png", "Maven: mvnw")
                .eq("mvnw"),
            ofFile("berksfile", "/icons/berkshelf.png", "Berkshelf: berksfile")
                .eq("berksfile"),
            ofFile("berksfilelock", "/icons/berkshelflock.png", "Berkshelf: berksfile.lock")
                .eq("berksfile.lock"),
            ofFile("bettercodehub", "/icons/bettercodehub.svg", "Better Code Hub: .bettercodehub.yml")
                .eq(".bettercodehub.yml"),
            ofFile("bitbucket", "/icons/bitbucket.svg", "Bitbucket: bitbucket-pipelines.yml")
                .eq("bitbucket-pipelines").end(YML),
            ofFile("bower", "/icons/bower.svg", "Bower: bower.json, .bowerrc")
                .eq("bower.json", ".bowerrc"),
            ofFile("browserslist", "/icons/browserslist.svg", "Browserslist: browserslist, .browserslistrc")
                .eq("browserslist", ".browserslistrc"),
            ofFile("build", "/icons/build.png", "Build: build(.md,.txt,.adoc), building")
                .eq("build", "building").mayEnd(TXT),
            ofFile("cassandra", "/icons/cassandra.png", "Cassandra: cassandra(.yml,.yaml)")
                .eq("cassandra").mayEnd(YML),
            ofFile("cassandra1", "/icons/cassandra.png", "Cassandra: start by 'cassandra' and end by '.yml,.yaml'")
                .start("cassandra").end(YML),
            ofFile("changelog", "/icons/changelog.png", "Changelog: changelog(.md,.txt,.adoc), changes")
                .eq("changelog", "changes").mayEnd(TXT),
            ofFile("circle", "/icons/circleci.svg", "Circle CI: circle.yml")
                .eq("circle.yml"),
            ofFile("circle1", "/icons/circleci.svg", "Circle CI: .circleci/config.yml")
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
            ofFile("docker", "/icons/docker.png", "Docker: dockerfile(.xml,.yml,...)")
                .eq("dockerfile").mayEnd(CFG),
            ofFile("dockercompose", "/icons/dockercompose.png", "Docker: docker-compose")
                .eq("docker-compose").mayEnd(CFG),
            ofFile("docker1", "/icons/docker.png", "Docker: start by 'dockerfile' and end by '.xml,.yml,...'")
                .start("dockerfile").end(CFG),
            ofFile("dockercompose1", "/icons/dockercompose.png", "Docker: start by 'docker-compose' and end by '.xml,.yml,...'")
                .start("docker-compose").end(CFG),
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
            ofFile("intellijcodestyle", "/icons/jetbrains.svg", "IntelliJ: intellijcodestyle.xml")
                .eq("intellijcodestyle.xml"),
            ofFile("mailmap", "/icons/email.png", "Mailmap: .mailmap")
                .eq(".mailmap"),
            ofFile("msazure", "/icons/msazure.svg", "Microsoft Azure: azure-pipelines.yml")
                .eq("azure-pipelines.yml"),
            ofFile("gatling", "/icons/gatling.png", "Gatling: gatling(.conf)")
                .eq("gatling").mayEnd(".conf"),
            ofFile("gatling1", "/icons/gatling.png", "Gatling: start by 'gatling' and end by '.conf'")
                .start("gatling").end(".conf"),
            ofFile("gitpod", "/icons/gitpod.svg", "Gitpod: .gitpod.yml")
                .start(".gitpod").end(YML),
            ofFile("grunt", "/icons/grunt.svg", "Grunt: Gruntfile.js")
                .eq("gruntfile.js"),
            ofFile("git", "/icons/git.png", "Git: .gitattributes, .gitignore, .gitmodules")
                .eq(".gitattributes", ".gitignore", ".gitmodules"),
            ofFile("gitlab", "/icons/gitlab.png", "Gitlab: .gitlab-ci.yml")
                .eq(".gitlab-ci.yml"),
            ofFile("graphqlconfig", "/icons/graphql-config.png", "GraphQL: graphql.config.json")
                .eq("graphql.config.json"),
            ofFile("grafana", "/icons/grafana.svg", "Grafana: grafana.ini")
                .eq("grafana.ini"),
            ofFile("graphqlschema", "/icons/graphql-schema.png", "GraphQL: graphql.schema.json")
                .eq("graphql.schema.json"),
            ofFile("gocd", "/icons/gocd.png", "Gocd: .gocd(.xml,.yml,...)")
                .eq(".gocd").mayEnd(CFG),
            ofFile("java", "/icons/java.png", "JVM properties (e.g. Tomcat or M ven JVM configuration): jvm.properties, jvm.config")
                .eq("jvm.properties", "jvm.config"),
            ofFile("imgbot", "/icons/imgbot.svg", "ImgBot: .imgbotconfig")
                .eq(".imgbotconfig"),
            ofFile("jenkins", "/icons/jenkins.png", "Jenkins: jenkinsfile(.xml,.yml,...), jenkins")
                .eq("jenkinsfile", "jenkins").mayEnd(CFG),
            ofFile("jenkins1", "/icons/jenkins.png", "Jenkins: start by 'jenkins and end by '.xml,.yml,...'")
                .start("jenkins").end(CFG),
            ofFile("jenkins2", "/icons/jenkins.png", "Jenkins: start by 'jenkins' and contain no dot")
                .start("jenkins").noDot(),
            ofFile("jest_js", "/icons/jest.svg", "Jest: jest.config.js")
                .eq("jest.config.js"),
            ofFile("jest_ts", "/icons/jest.svg", "Jest: jest.config.ts")
                .eq("jest.config.ts"),
            ofFile("jsbeautify", "/icons/jsbeautify.png", "JSBeautify: .jsbeautifyrc(.xml,.yml,...)")
                .eq(".jsbeautifyrc").mayEnd(CFG),
            ofFile("jshint", "/icons/jshint.png", "JSHint: .jshintrc(.xml,.yml,...)")
                .eq(".jshintrc").mayEnd(CFG),
            ofFile("junit", "/icons/junit5.png", "JUnit: junit-platform.properties")
                .eq("junit-platform.properties"),
            ofFile("karma_js", "/icons/karma.svg", "Karma: karma.conf.js")
                .eq("karma.conf.js"),
            ofFile("karma_ts", "/icons/karma.svg", "Karma: karma.conf.ts")
                .eq("karma.conf.ts"),
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
            ofFile("log4j", "/icons/log4j.png", "Log4j: log4j(.xml,.yml,...), log4j-test")
                .eq("log4j", "log4j-test").mayEnd(CFG),
            ofFile("logback", "/icons/logback.png", "Logback: logback(.xml,.yml,...)")
                .eq("logback").mayEnd(CFG),
            ofFile("logback1", "/icons/logback.png", "Logback: start by 'logback-' and end by '.xml,.yml,...'")
                .start("logback-").mayEnd(CFG),
            ofFile("logstash", "/icons/logstash.png", "Logstash: logstash(.cfg,.conf,.yml,.yaml)")
                .eq("logstash").mayEnd(".cfg", ".conf", ".yml", ".yaml"),
            ofFile("logstash1", "/icons/logstash.png", "Logstash: start by 'logstash' and end by '.cfg,.conf,.yml,.yaml'")
                .start("logstash").end(".cfg", ".conf", ".yml", ".yaml"),
            ofFile("logstash2", "/icons/logstash.png", "Logstash: start by 'logstash-' and end by '.cfg,.conf,.yml,.yaml,.txt'")
                .start("logstash-").end(".cfg", ".conf", ".yml", ".yaml", ".txt"),
            ofFile("lombok", "/icons/lombok.png", "Lombok: lombok.config")
                .eq("lombok.config"),
            ofFile("mysql", "/icons/my.png", "MySQL: my.ini")
                .eq("my.ini"),
            ofFile("nginx", "/icons/nginx.png", "Nginx: nginx(.conf)")
                .eq("nginx").mayEnd(".conf"),
            ofFile("nginx1", "/icons/nginx.png", "Nginx: start by 'nginx' and end by '.conf'")
                .start("nginx").end(".conf"),
            ofFile("npmrc", "/icons/npm.svg", "NPM: .npmrc")
                .eq(".npmrc"),
            ofFile("npmignore", "/icons/npmignore.svg", "NPM: .npmignore")
                .eq(".npmignore"),
            ofFile("notice", "/icons/notice.png", "Notice: notice(.md,.txt,.adoc)")
                .eq("notice").mayEnd(TXT),
            ofFile("packageinfojava", "/icons/packageinfojava.png", "Java package info: package-info.java")
                .eq("package-info.java"),
            ofFile("packagejson", "/icons/npm.svg", "NPM: package.json")
                .eq("package.json"),
            ofFile("packagejsonlock", "/icons/packagejsonlock.png", "NPM: package-lock.json")
                .eq("package-lock.json"),
            ofFile("prettier", "/icons/prettier.svg", "Prettier: .prettierrc")
                .eq(".prettierrc"),
            ofFile("privacy", "/icons/privacy.png", "Privacy: privacy(.md,.txt,.adoc)")
                .eq("privacy").mayEnd(TXT),
            ofFile("prometheus", "/icons/prometheus.svg", "Prometheus: prometheus(.yml,.yaml)")
                .eq("prometheus").end(YML),
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
            ofFile("rollup", "/icons/rollup.png", "Rollup: rollup.config.js")
                .eq("rollup.config.js"),
            ofFile("swaggerignore", "/icons/swaggerignore.svg", "Swagger: .swagger-codegen-ignore")
                .eq(".swagger-codegen-ignore"),
            ofFile("testing", "/icons/testing.png", "Test: test(.md,.txt,.adoc), testing")
                .eq("test", "testing").mayEnd(TXT),
            ofFile("todo", "/icons/todo.png", "To-Do: todo(.md,.txt,.adoc)")
                .eq("todo").mayEnd(TXT),
            ofFile("travis", "/icons/travis.svg", "Travis CI: .travis.yml")
                .eq(".travis.yml"),
            ofFile("vagrant", "/icons/vagrant.png", "Vagrant: vagrantfile")
                .eq("vagrantfile"),
            ofFile("version", "/icons/version.png", "Version: version(.md,.txt,.adoc)")
                .eq("version").mayEnd(TXT),
            ofFile("webpack", "/icons/webpack.svg", "Webpack: webpack.conf.js")
                .eq("webpack.config.js"),
            ofFile("zalando", "/icons/zalando.png", "Zalando Zappr: .zappr.yaml")
                .eq(".zappr.yaml"),

            //
            // extension only
            //
            ofFile("ext_archive", "/icons/archive.png", "Archive: *.zip, *.7z, *.tar, *.gz, *.bz2, *.xz")
                .end(".zip", ".7z", ".tar", ".gz", ".bz2", ".xz"),
            ofFile("ext_adoc", "/icons/asciidoc.png", "Asciidoc: *.adoc, *.asciidoc")
                .end(".adoc", ".asciidoc"),
            ofFile("ext_apk", "/icons/apk.svg", "Android application package (APK): *.apk, *.xapk")
                .end(".apk", ".xapk"),
            ofFile("ext_avro_avsc", "/icons/avro.svg", "Avro: *.avsc")
                .end(".avsc"),
            ofFile("ext_back", "/icons/backup.png", "Backup: *.versionbackup, *.versionsbackup, *.bak, *.back, *.backup, *.old, *.prev, *.revert")
                .end(".versionbackup", ".versionsbackup", ".bak", ".back", ".backup", ".old", ".prev", ".revert"),
            ofFile("ext_sh", "/icons/bash.png", "Bash: *.sh")
                .end(".sh"),
            ofFile("ext_cert", "/icons/certificate.png", "Certificate: *.jks, *.pem, *.crt, *.cert, *.ca-bundle, *.cer, ...")
                .end(".jks", ".pem", ".crt", ".cert", ".ca-bundle", ".cer", ".jceks", ".p12", ".p7b", ".p7s", ".pfx", ".pubkey"),
            ofFile("ext_cmd", "/icons/cmd.png", "Windows script: *.cmd, *.bat, *.ps1")
                .end(".cmd", ".bat", ".ps1"),
            ofFile("ext_csv", "/icons/csv.png", "CSV: *.csv")
                .end(".csv"),
            ofFile("ext_dar", "/icons/jar.png", "Diffusion Archive / XL Deploy Archive: *.dar")
                .end(".dar"),
            ofFile("ext_deb", "/icons/deb.png", "Debian package: *.deb")
                .end(".deb"),
            ofFile("ext_epub", "/icons/epub.svg", "Epub: *.epub, *.mobi, *.azw, *.azw3")
                .end(".epub", ".mobi", ".azw", ".azw3"),
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
            ofFile("ext_iml", "/icons/jetbrains.svg", "IntelliJ project: *.iml")
                .end(".iml"),
            ofFile("ext_cfg", "/icons/ini.png", "Configuration: *.ini, *.cfg, *.conf")
                .end(".ini", ".cfg", ".conf"),
            ofFile("ext_jar", "/icons/jar.png", "Java archive: *.jar")
                .end(".jar"),
            ofFile("ext_jaroriginal", "/icons/jar.png", "Java archive (copy): *.jar.original")
                .end(".jar.original"),
            ofFile("ext_kdbx", "/icons/keepass.png", "KeePass: *.kdbx")
                .end(".kdbx"),
            ofFile("ext_md", "/icons/markdown.png", "Markdown: *.md")
                .end(".md"),
            ofFile("ext_mwb", "/icons/mysqlworkbench.png", "MySQL Workbench: *.mwb")
                .end(".mwb"),
            ofFile("ext_nsi", "/icons/nsis.png", "NSIS Nullsoft Scriptable Install System: *.nsi")
                .end(".nsi"),
            ofFile("ext_pdf", "/icons/pdf.png", "PDF: *.pdf")
                .end(".pdf"),
            ofFile("ext_pid", "/icons/pid.svg", "PID: *.pid")
                .end(".pid"),
            ofFile("ext_postmanconfig", "/icons/postman.svg", "Postman config: *.postman.json, *postman_collection.json, *postman_environment.json")
                .end(".postman.json", "postman_collection.json", "postman_environment.json"),
            ofFile("ext_rpm", "/icons/rpm.svg", "Red Hat package: *.rpm")
                .end(".rpm"),
            ofFile("sass", "/icons/sass.svg", "SASS: *.sass, *.scss")
                .end(".sass", ".scss"),
            ofFile("less", "/icons/less.png", "LESS CSS: *.less")
                .end(".less"),
            ofFile("ext_svg", "/icons/svg.svg", "SVG: *.svg")
                .end(".svg"),
            ofFile("ext_tf", "/icons/terraform.png", "Terraform: *.hcl, *.tf, *.tf.json")
                .end(".hcl", ".tf", ".tf.json"),
            ofFile("ext_toml", "/icons/toml.png", "TOML: *.toml")
                .end(".toml"),
            ofFile("typescript", "/icons/test-ts.svg", "Typescript: *.spec.ts")
                .end(".spec.ts"),
            ofFile("typescript-ts", "/icons/typescript.svg", "Typescript: *.ts")
                .end(".ts"),
            ofFile("ext_video", "/icons/video.svg", "Video: *.3gp, *.avi, *.flv, *.mkv, *.mp4, *.mpeg, *.ogm, *.webm, ...")
                .end(".3g2", ".3gp", ".avi", ".divx", ".f4a", ".f4b", ".f4p", ".f4v", ".flv", ".m4p",
                    ".m4v", ".mkv", ".mp4", ".mpg", ".mpeg", ".mov", ".ogm", ".ogv", ".vob", ".webm", ".wmv", ".xvid"),
            ofFile("ext_war", "/icons/war.svg", "Java WAR: *.war")
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
