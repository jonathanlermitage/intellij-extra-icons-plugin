package lermitage.intellij.extra.icons;

import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.Arrays.asList;

public class ExtraIconProvider extends BaseIconProvider implements DumbAware {
    
    private static final String[] TXT = new String[]{".md", ".txt", ".adoc"};
    private static final String[] CFG = new String[]{".xml", ".yml", ".yaml", ".properties", ".json", ".cfg", ".conf", ".ini", ".txt"};
    private static final String[] YML = new String[]{".yaml", ".yml"};
    
    @NotNull
    public static List<Model> allModels() {
        return asList(
                new Model("htaccess", "/icons/apache.png", "Apache: .htaccess")
                        .eq(".htaccess"),
                new Model("appveyor", "/icons/appveyor.png", "Appveyor: appveyor.yml")
                        .eq("appveyor.yml"),
                new Model("archunit", "/icons/archunit.png", "ArchUnit: archunit.properties")
                        .eq("archunit.properties"),
                new Model("author", "/icons/authors.png", "Author: author(.md,.txt,.adoc), authors")
                        .eq("author", "authors").mayEnd(TXT),
                new Model("bamboo", "/icons/bamboo.png", "Bamboo: bamboo.yml")
                        .eq("bamboo.yml"),
                new Model("gradle", "/icons/bash-gradlew.png", "Gradle: gradlew, gradle.properties")
                        .eq("gradlew", "gradle.properties"),
                new Model("mvnw", "/icons/bash-mvnw.png", "Maven: mvnw")
                        .eq("mvnw"),
                new Model("berksfile", "/icons/berkshelf.png", "Berkshelf: berksfile")
                        .eq("berksfile"),
                new Model("berksfilelock", "/icons/berkshelflock.png", "Berkshelf: berksfile.lock")
                        .eq("berksfile.lock"),
                new Model("bower", "/icons/bower.png", "Bower: bower.json, .bowerrc")
                        .eq("bower.json", ".bowerrc"),
                new Model("build", "/icons/build.png", "Build: build(.md,.txt,.adoc), building")
                        .eq("build", "building").mayEnd(TXT),
                new Model("cassandra", "/icons/cassandra.png", "Cassandra: cassandra(.yml,.yaml)")
                        .eq("cassandra").mayEnd(YML),
                new Model("cassandra1", "/icons/cassandra.png", "Cassandra: start by 'cassandra' and end by '.yml,.yaml'")
                        .start("cassandra").end(YML),
                new Model("changelog", "/icons/changelog.png", "Changelog: changelog(.md,.txt,.adoc), changes")
                        .eq("changelog", "changes").mayEnd(TXT),
                new Model("circle", "/icons/circleci.png", "Circle CI: circle.yml")
                        .eq("circle.yml"),
                new Model("cirrus", "/icons/cirrus.png", "Cirrus CI: .cirrus.yml")
                        .eq(".cirrus.yml"),
                new Model("gradlew", "/icons/cmd-gradlew.png", "Gradle (Windows): gradlew.bat, gradlew.cmd")
                        .eq("gradlew.bat", "gradlew.cmd"),
                new Model("mvnw", "/icons/cmd-mvnw.png", "Maven (Windows): mvnw.bat, mvnw.cmd")
                        .eq("mvnw.bat", "mvnw.cmd"),
                new Model("codecov", "/icons/codecov.png", "CodeCov: .codecov.yml, codecov.yml")
                        .eq(".codecov.yml", "codecov.yml"),
                new Model("codefresh", "/icons/codefresh.png", "Codefresh: codefresh.yml")
                        .eq("codefresh.yml"),
                new Model("codacy", "/icons/codacy.png", "Codacy: .codacy(.yml,.yaml)")
                        .eq(".codacy").mayEnd(YML),
                new Model("codeship", "/icons/codeship.png", "Codeship steps: codeship-steps(.xml,.yml,...)")
                        .eq("codeship-steps").mayEnd(CFG),
                new Model("contact", "/icons/contact.png", "Contact: contact(.md,.txt,.adoc), contacts")
                        .eq("contact", "contacts").mayEnd(TXT),
                new Model("contribute", "/icons/contributing.png", "Contribution: contribute(.md,.txt,.adoc), contributing, contribution")
                        .eq("contribute", "contributing", "contribution").mayEnd(TXT),
                new Model("deptective", "/icons/deptective.png", "Deptective: deptective.json")
                        .eq("deptective.json"),
                new Model("docker", "/icons/docker.png", "Docker: dockerfile(.xml,.yml,...), docker-compose, .dockerignore")
                        .eq("dockerfile", "docker-compose", ".dockerignore").mayEnd(CFG),
                new Model("docker1", "/icons/docker.png", "Docker: start by 'dockerfile', 'docker-compose', '.dockerignore' and end by '.xml,.yml,...'")
                        .start("dockerfile", "docker-compose", ".dockerignore").end(CFG),
                new Model("editorconfig", "/icons/editorconfig.png", "EditorConfig: .editorconfig")
                        .eq(".editorconfig").mayEnd(CFG),
                new Model("elastic", "/icons/elasticsearch.png", "Elasticsearch: elastic(.yml,.yaml)")
                        .eq("elastic").mayEnd(YML),
                new Model("elastic1", "/icons/elasticsearch.png", "Elasticsearch: start by 'elastic' and end by '.yml,.yaml'")
                        .start("elastic").end(YML),
                new Model("cerebro", "/icons/elastic-cerebro.png", "Cerebro: cerebro(.conf)")
                        .eq("cerebro").mayEnd(".conf"),
                new Model("cerebro1", "/icons/elastic-cerebro.png", "Cerebro: start by 'cerebro' and end by '.conf'")
                        .start("cerebro").end(".conf"),
                new Model("mailmap", "/icons/email.png", "Mailmap: .mailmap")
                        .eq(".mailmap"),
                new Model("gatling", "/icons/gatling.png", "Gatling: gatling(.conf)")
                        .eq("gatling").mayEnd(".conf"),
                new Model("gatling1", "/icons/gatling.png", "Gatling: start by 'gatling' and end by '.conf'")
                        .start("gatling").end(".conf"),
                new Model("git", "/icons/git.png", "Git: .gitattributes, .gitignore, .gitmodules")
                        .eq(".gitattributes", ".gitignore", ".gitmodules"),
                new Model("gitlab", "/icons/gitlab.png", "Gitlab: .gitlab-ci.yml")
                        .eq(".gitlab-ci.yml"),
                new Model("graphqlconfig", "/icons/graphql-config.png", "GraphQL: graphql.config.json")
                        .eq("graphql.config.json"),
                new Model("graphqlschema", "/icons/graphql-schema.png", "GraphQL: graphql.schema.json")
                        .eq("graphql.schema.json"),
                new Model("gocd", "/icons/gocd.png", "Gocd: .gocd(.xml,.yml,...)")
                        .eq(".gocd").mayEnd(CFG),
                new Model("java", "/icons/java.png", "JVM properties (e.g. Tomcat configuration): jvm.properties")
                        .eq("jvm.properties"),
                new Model("jenkins", "/icons/jenkins.png", "Jenkins: jenkinsfile(.xml,.yml,...), jenkins")
                        .eq("jenkinsfile", "jenkins").mayEnd(CFG),
                new Model("jenkins1", "/icons/jenkins.png", "Jenkins: start by 'jenkins and end by '.xml,.yml,...'")
                        .start("jenkins").end(CFG),
                new Model("jenkins2", "/icons/jenkins.png", "Jenkins: start by 'jenkins' and contain no dot")
                        .start("jenkins").noDot(),
                new Model("jsbeautify", "/icons/jsbeautify.png", "JSBeautify: .jsbeautifyrc(.xml,.yml,...)")
                        .eq(".jsbeautifyrc").mayEnd(CFG),
                new Model("jshint", "/icons/jshint.png", "JSHint: .jshintrc(.xml,.yml,...)")
                        .eq(".jshintrc").mayEnd(CFG),
                new Model("junit", "/icons/junit5.png", "JUnit: junit-platform.properties")
                        .eq("junit-platform.properties"),
                new Model("kibana", "/icons/kibana.png", "Kibana: kibana(.xml,.yml,...)")
                        .eq("kibana").mayEnd(YML),
                new Model("kibana1", "/icons/kibana.png", "Kibana: start by 'kibana' and end by '.xml,.yml,...'")
                        .start("kibana").end(YML),
                new Model("kubernetes", "/icons/kubernetes.png", "Kubernetes: kubernetes(.xml,.yml,...)")
                        .eq("kubernetes").mayEnd(YML),
                new Model("kubernetes1", "/icons/kubernetes.png", "Kubernetes: start by 'kubernetes' and end by '.xml,.yml,...'")
                        .start("kubernetes").end(YML),
                new Model("license", "/icons/license.png", "License: license(.md,.txt,.adoc), copying, license_info, additional_license_info")
                        .eq("license", "copying", "license_info", "additional_license_info").mayEnd(TXT),
                new Model("log4j", "/icons/log4j.png", "Log4j: log4j(.xml,.yml,...), log4j-test").eq("log4j", "log4j-test")
                        .mayEnd(CFG),
                new Model("logback", "/icons/logback.png", "Logback: logback(.xml,.yml,...), logback-test")
                        .eq("logback", "logback-test").mayEnd(CFG),
                new Model("logstash", "/icons/logstash.png", "Logstash: logstash(.cfg,.conf,.yml,.yaml)")
                        .eq("logstash").mayEnd(".cfg", ".conf", ".yml", ".yaml"),
                new Model("logstash1", "/icons/logstash.png", "Logstash: start by 'logstash' and end by '.cfg,.conf,.yml,.yaml'")
                        .start("logstash").end(".cfg", ".conf", ".yml", ".yaml"),
                new Model("lombok", "/icons/lombok.png", "Lombok: lombok.config")
                        .eq("lombok.config"),
                new Model("mysql", "/icons/my.png", "MySQL: my.ini")
                        .eq("my.ini"),
                new Model("nginx", "/icons/nginx.png", "Nginx: nginx(.conf)")
                        .eq("nginx").mayEnd(".conf"),
                new Model("nginx1", "/icons/nginx.png", "Nginx: start by 'nginx' and end by '.conf'")
                        .start("nginx").end(".conf"),
                new Model("notice", "/icons/notice.png", "Notice: notice(.md,.txt,.adoc)")
                        .eq("notice").mayEnd(TXT),
                new Model("packageinfojava", "/icons/packageinfojava.png", "Java package info: package-info.java")
                        .eq("package-info.java"),
                new Model("packagejson", "/icons/packagejson.png", "NPM: package.json")
                        .eq("package.json"),
                new Model("packagejsonlock", "/icons/packagejsonlock.png", "NPM: package-lock.json")
                        .eq("package-lock.json"),
                new Model("privacy", "/icons/privacy.png", "Privacy: privacy(.md,.txt,.adoc)")
                        .eq("privacy").mayEnd(TXT),
                new Model("puppet", "/icons/puppet.png", "Puppet: puppet(.conf)")
                        .eq("puppet").mayEnd(".conf"),
                new Model("puppet1", "/icons/puppet.png", "Puppet: start by 'puppet' and end by '.conf'")
                        .start("puppet").end(".conf"),
                new Model("readme", "/icons/readme.png", "Readme: readme(.md,.txt,.adoc), lisezmoi")
                        .eq("readme", "lisezmoi").mayEnd(TXT),
                new Model("redis", "/icons/redis.png", "Redis: redis(.conf)")
                        .eq("redis").mayEnd(".conf"),
                new Model("redis1", "/icons/redis.png", "Redis: start by 'redis' and end by '.conf'")
                        .start("redis").end(".conf"),
                new Model("roadmap", "/icons/roadmap.png", "Roadmap: roadmap(.md,.txt,.adoc)")
                        .eq("roadmap").mayEnd(TXT),
                new Model("testing", "/icons/testing.png", "Test: test(.md,.txt,.adoc), testing")
                        .eq("test", "testing").mayEnd(TXT),
                new Model("todo", "/icons/todo.png", "To-Do: todo(.md,.txt,.adoc)")
                        .eq("todo").mayEnd(TXT),
                new Model("travis", "/icons/travis.png", "Travis CI: .travis.yml")
                        .eq(".travis.yml"),
                new Model("vagrant", "/icons/vagrant.png", "Vagrant: vagrantfile")
                        .eq("vagrantfile"),
                new Model("version", "/icons/version.png", "Version: version(.md,.txt,.adoc)")
                        .eq("version").mayEnd(TXT),
                new Model("zalando", "/icons/zalando.png", "Zalando Zappr: .zappr.yaml")
                        .eq(".zappr.yaml"),
                
                new Model("ext_archive", "/icons/archive.png", "Archive: *.zip, *.7z, *.tar, *.gz, *.bz2")
                        .end(".zip", ".7z", ".tar", ".gz", ".bz2"),
                new Model("ext_adoc", "/icons/asciidoc.png", "Asciidoc: *.adoc, *.asciidoc")
                        .end(".adoc", ".asciidoc"),
                new Model("ext_back", "/icons/backup.png", "Backup: *.versionbackup, *.versionsbackup, *.back, *.backup, *.old, *.prev, *.revert")
                        .end(".versionbackup", ".versionsbackup", ".back", ".backup", ".old", ".prev", ".revert"),
                new Model("ext_sh", "/icons/bash.png", "Bash: *.sh")
                        .end(".sh"),
                new Model("ext_cert", "/icons/certificate.png", "Certificate: *.jks, *.pem, *.crt, *.cert, *.ca-bundle, *.cer, *.p7b, *.p7s, *.pfx")
                        .end(".jks", ".pem", ".crt", ".cert", ".ca-bundle", ".cer", ".p7b", ".p7s", ".pfx"),
                new Model("ext_cmd", "/icons/cmd.png", "Windows script: *.cmd, *.bat, *.ps1")
                        .end(".cmd", ".bat", ".ps1"),
                new Model("ext_csv", "/icons/csv.png", "CSV: *.csv")
                        .end(".csv"),
                new Model("ext_deb", "/icons/deb.png", "Debian package: *.deb")
                        .end(".deb"),
                new Model("ext_form", "/icons/form.png", "Form (e.g. IntelliJ Swing xml form): *.form")
                        .end(".form"),
                new Model("ext_graphqls", "/icons/graphql.png", "GraphQL: *.graphqls")
                        .end(".graphqls"),
                new Model("ext_haxe", "/icons/haxe.png", "Haxe: *.hx")
                        .end(".hx"),
                new Model("ext_haxexml", "/icons/haxehxml.png", "Haxe: *.hxml")
                        .end(".hxml"),
                new Model("ext_http", "/icons/http.png", "HTTP (e.g. IntelliJ HTTP Client queries file): *.http")
                        .end(".http"),
                new Model("ext_iml", "/icons/ijbeam.png", "IntelliJ project: *.iml")
                        .end(".iml"),
                new Model("ext_cfg", "/icons/ini.png", "Configuration: *.ini, *.cfg, *.conf")
                        .end(".ini", ".cfg", ".conf"),
                new Model("ext_jar", "/icons/jar.png", "Java archive: *.jar")
                        .end(".jar"),
                new Model("ext_kdbx", "/icons/keepass.png", "KeePass: *.kdbx")
                        .end(".kdbx"),
                new Model("ext_md", "/icons/markdown.png", "Markdown: *.md")
                        .end(".md"),
                new Model("ext_nsi", "/icons/nsis.png", "NSIS Nullsoft Scriptable Install System: *.nsi")
                        .end(".nsi"),
                new Model("ext_pdf", "/icons/pdf.png", "PDF: *.pdf")
                        .end(".pdf"),
                new Model("ext_rpm", "/icons/rpm.png", "Red Hat package: *.rpm")
                        .end(".rpm"),
                new Model("ext_svg", "/icons/svg.png", "SVG: *.svg")
                        .end(".svg"),
                new Model("ext_tf", "/icons/terraform.png", "Terraform: *.hcl, *.tf, *.tf.json")
                        .end(".hcl", ".tf", ".tf.json"),
                new Model("ext_toml", "/icons/toml.png", "TOML: *.toml")
                        .end(".toml"),
                new Model("ext_video", "/icons/video.png", "Video: *.3gp, *.avi, *.flv, *.mkv, *.mp4, *.mpeg, *.ogm, *.webm, ...")
                        .end(".3g2", ".3gp", ".avi", ".divx", ".f4a", ".f4b", ".f4p", ".f4v", ".flv", ".m4p",
                                ".m4v", ".mkv", ".mp4", ".mpg", ".mpeg", ".mov", ".ogm", ".ogv", ".vob", ".webm", ".wmv", ".xvid"),
                new Model("ext_war", "/icons/war.png", "Java WAR: *.war")
                        .end(".war"),
                new Model("ext_libreoffice_calc", "/icons/officedocs/localc.png", "LibreOffice Calc: *.ods")
                        .end(".ods"),
                new Model("ext_libreoffice_draw", "/icons/officedocs/lodraw.png", "LibreOffice Draw: *.odg")
                        .end(".odg"),
                new Model("ext_libreoffice_impress", "/icons/officedocs/loimpress.png", "LibreOffice Impress: *.odp")
                        .end(".odp"),
                new Model("ext_libreoffice_math", "/icons/officedocs/lomath.png", "LibreOffice Math: *.odf")
                        .end(".odf"),
                new Model("ext_libreoffice_writer", "/icons/officedocs/lowriter.png", "LibreOffice Writer: *.odt")
                        .end(".odt"),
                new Model("ext_msoffice_excel", "/icons/officedocs/msexcel.png", "MSOffice Excel: *.xls, *.xlsx")
                        .end(".xls", ".xlsx"),
                new Model("ext_msoffice_onenote", "/icons/officedocs/msonenote.png", "MSOffice OneNote: *.one, *.onetoc2")
                        .end(".one", ".onetoc2"),
                new Model("ext_msoffice_powerpoint", "/icons/officedocs/mspowerpoint.png", "MSOffice Powerpoint: *.ppt, *.pptx")
                        .end(".ppt", ".pptx"),
                new Model("ext_msoffice_project", "/icons/officedocs/msproject.png", "MSOffice Project: *.mpd, *.mpp, *.mpt")
                        .end(".mpd", ".mpp", ".mpt"),
                new Model("ext_msoffice_visio", "/icons/officedocs/msvisio.png", "MSOffice Visio: *.vsd, *.vsdx, *.vss, *.vssx, *.vst, *.vstx")
                        .end(".vsd", ".vsdx", ".vss", ".vssx", ".vst", ".vstx"),
                new Model("ext_msoffice_word", "/icons/officedocs/msword.png", "MSOffice Word: *.doc, *.docx")
                        .end(".doc", ".docx")
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
