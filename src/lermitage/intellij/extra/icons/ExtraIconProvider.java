package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class ExtraIconProvider extends IconProvider implements DumbAware {
    
    private List<Model> models;
    
    public ExtraIconProvider() {
        super();
        load();
    }
    
    private void load() {
        String[] txt = new String[]{".md", ".txt", ".adoc"};
        String[] cfg = new String[]{".xml", ".yml", ".yaml", ".properties", ".json", ".cfg", ".conf", ".ini", ".txt"};
        String[] yml = new String[]{".yaml", ".yml"};
        List<Model> allModels = asList(
                m("htaccess", "/icons/apache.png").eq(".htaccess"),
                m("appveyor", "/icons/appveyor.png").eq("appveyor.yml"),
                m("archunit", "/icons/archunit.png").eq("archunit.properties"),
                m("author", "/icons/authors.png").eq("author", "authors").mayEnd(txt),
                m("bamboo", "/icons/bamboo.png").eq("bamboo.yml"),
                m("gradle", "/icons/bash-gradlew.png").eq("gradlew", "gradle.properties"),
                m("mvnw", "/icons/bash-mvnw.png").eq("mvnw"),
                m("berksfile", "/icons/berkshelf.png").eq("berksfile"),
                m("berksfile", "/icons/berkshelflock.png").eq("berksfile.lock"),
                m("bower", "/icons/bower.png").eq("bower.json", ".bowerrc"),
                m("build", "/icons/build.png").eq("build", "building").mayEnd(txt),
                m("cassandra", "/icons/cassandra.png").eq("cassandra").mayEnd(yml),
                m("cassandra", "/icons/cassandra.png").start("cassandra").end(yml),
                m("changelog", "/icons/changelog.png").eq("changelog", "changes").mayEnd(txt),
                m("circle", "/icons/circleci.png").eq("circle.yml"),
                m("cirrus", "/icons/cirrus.png").eq(".cirrus.yml"),
                m("gradlew", "/icons/cmd-gradlew.png").eq("gradlew.bat", "gradlew.cmd"),
                m("mvnw", "/icons/cmd-mvnw.png").eq("mvnw.bat", "mvnw.cmd"),
                m("codecov", "/icons/codecov.png").eq(".codecov.yml", "codecov.yml"),
                m("codefresh", "/icons/codefresh.png").eq("codefresh.yml"),
                m("codacy", "/icons/codacy.png").eq(".codacy").mayEnd(yml),
                m("codeship", "/icons/codeship.png").eq("codeship-steps").mayEnd(cfg),
                m("contact", "/icons/contact.png").eq("contact", "contacts").mayEnd(txt),
                m("contribute", "/icons/contributing.png").eq("contribute", "contributing", "contribution").mayEnd(txt),
                m("deptective", "/icons/deptective.png").eq("deptective.json"),
                m("docker", "/icons/docker.png").eq("dockerfile", "docker-compose", ".dockerignore").mayEnd(cfg),
                m("docker", "/icons/docker.png").start("dockerfile", "docker-compose", ".dockerignore").end(cfg),
                m("editorconfig", "/icons/editorconfig.png").eq(".editorconfig").mayEnd(cfg),
                m("elastic", "/icons/elasticsearch.png").eq("elastic").mayEnd(yml),
                m("elastic", "/icons/elasticsearch.png").start("elastic").end(yml),
                m("cerebro", "/icons/elastic-cerebro.png").eq("cerebro").mayEnd(".conf"),
                m("cerebro", "/icons/elastic-cerebro.png").start("cerebro").end(".conf"),
                m("mailmap", "/icons/email.png").eq(".mailmap"),
                m("gatling", "/icons/gatling.png").eq("gatling").mayEnd(".conf"),
                m("gatling", "/icons/gatling.png").start("gatling").end(".conf"),
                m("git", "/icons/git.png").eq(".gitattributes", ".gitignore", ".gitmodules"),
                m("gitlab", "/icons/gitlab.png").eq(".gitlab-ci.yml"),
                m("graphql", "/icons/graphql-config.png").eq("graphql.config.json"),
                m("graphql", "/icons/graphql-schema.png").eq("graphql.schema.json"),
                m("gocd", "/icons/gocd.png").eq(".gocd").mayEnd(cfg),
                m("java", "/icons/java.png").eq("jvm.properties"),
                m("jenkins", "/icons/jenkins.png").eq("jenkinsfile", "jenkins").mayEnd(cfg),
                m("jenkins", "/icons/jenkins.png").start("jenkins").end(cfg),
                m("jenkins", "/icons/jenkins.png").start("jenkins").noDot(),
                m("jsbeautify", "/icons/jsbeautify.png").eq(".jsbeautifyrc").mayEnd(cfg),
                m("jshint", "/icons/jshint.png").eq(".jshintrc").mayEnd(cfg),
                m("junit", "/icons/junit5.png").eq("junit-platform.properties"),
                m("kibana", "/icons/kibana.png").eq("kibana").mayEnd(yml),
                m("kibana", "/icons/kibana.png").start("kibana").end(yml),
                m("kubernetes", "/icons/kubernetes.png").eq("kubernetes").mayEnd(yml),
                m("kubernetes", "/icons/kubernetes.png").start("kubernetes").end(yml),
                m("license", "/icons/license.png").eq("license", "copying", "license_info", "additional_license_info").mayEnd(txt),
                m("log4j", "/icons/log4j.png").eq("log4j", "log4j-test").mayEnd(cfg),
                m("logback", "/icons/logback.png").eq("logback", "logback-test").mayEnd(cfg),
                m("logstash", "/icons/logstash.png").eq("logstash").mayEnd(".cfg", ".conf", ".yml", ".yaml"),
                m("logstash", "/icons/logstash.png").start("logstash").end(".cfg", ".conf", ".yml", ".yaml"),
                m("lombok", "/icons/lombok.png").eq("lombok.config"),
                m("mysql", "/icons/my.png").eq("my.ini"),
                m("nginx", "/icons/nginx.png").eq("nginx").mayEnd(".conf"),
                m("nginx", "/icons/nginx.png").start("nginx").end(".conf"),
                m("notice", "/icons/notice.png").eq("notice").mayEnd(txt),
                m("packageinfojava", "/icons/packageinfojava.png").eq("package-info.java"),
                m("packagejson", "/icons/packagejson.png").eq("package.json"),
                m("packagejson", "/icons/packagejsonlock.png").eq("package-lock.json"),
                m("privacy", "/icons/privacy.png").eq("privacy").mayEnd(txt),
                m("puppet", "/icons/puppet.png").eq("puppet").mayEnd(".conf"),
                m("puppet", "/icons/puppet.png").start("puppet").end(".conf"),
                m("readme", "/icons/readme.png").eq("readme", "lisezmoi").mayEnd(txt),
                m("redis", "/icons/redis.png").eq("redis").mayEnd(".conf"),
                m("redis", "/icons/redis.png").start("redis").end(".conf"),
                m("roadmap", "/icons/roadmap.png").eq("roadmap").mayEnd(txt),
                m("testing", "/icons/testing.png").eq("test", "testing").mayEnd(txt),
                m("todo", "/icons/todo.png").eq("todo").mayEnd(txt),
                m("travis", "/icons/travis.png").eq(".travis.yml"),
                m("vagrant", "/icons/vagrant.png").eq("vagrantfile"),
                m("version", "/icons/version.png").eq("version").mayEnd(txt),
                m("zalando", "/icons/zalando.png").eq(".zappr.yaml"),
                
                m("ext_archive", "/icons/archive.png").end(".zip", ".7z", ".tar", ".gz", ".bz2"),
                m("ext_adoc", "/icons/asciidoc.png").end(".adoc", ".asciidoc"),
                m("ext_back", "/icons/backup.png").end(".versionbackup", ".versionsbackup", ".back", ".backup", ".old", ".prev", ".revert"),
                m("ext_sh", "/icons/bash.png").end(".sh"),
                m("ext_cert", "/icons/certificate.png").end(".jks", ".pem", ".crt", ".cert", ".ca-bundle", ".cer", ".p7b", ".p7s", ".pfx"),
                m("ext_cmd", "/icons/cmd.png").end(".cmd", ".bat", ".ps1"),
                m("ext_csv", "/icons/csv.png").end(".csv"),
                m("ext_deb", "/icons/deb.png").end(".deb"),
                m("ext_form", "/icons/form.png").end(".form"),
                m("ext_graphqls", "/icons/graphql.png").end(".graphqls"),
                m("ext_haxe", "/icons/haxe.png").end(".hx"),
                m("ext_haxexml", "/icons/haxehxml.png").end(".hxml"),
                m("ext_http", "/icons/http.png").end(".http"),
                m("ext_iml", "/icons/ijbeam.png").end(".iml"),
                m("ext_cfg", "/icons/ini.png").end(".ini", ".cfg", ".conf"),
                m("ext_jar", "/icons/jar.png").end(".jar"),
                m("ext_kdbx", "/icons/keepass.png").end(".kdbx"),
                m("ext_md", "/icons/markdown.png").end(".md"),
                m("ext_nsi", "/icons/nsis.png").end(".nsi"),
                m("ext_pdf", "/icons/pdf.png").end(".pdf"),
                m("ext_rpm", "/icons/rpm.png").end(".rpm"),
                m("ext_svg", "/icons/svg.png").end(".svg"),
                m("ext_tf", "/icons/terraform.png").end(".hcl", ".tf", ".tf.json"),
                m("ext_toml", "/icons/toml.png").end(".toml"),
                m("ext_video", "/icons/video.png").end(".3g2", ".3gp", ".avi", ".divx", ".f4a", ".f4b", ".f4p", ".f4v", ".flv", ".m4p",
                        ".m4v", ".mkv", ".mp4", ".mpg", ".mpeg", ".mov", ".ogm", ".ogv", ".vob", ".webm", ".wmv", ".xvid"),
                m("ext_war", "/icons/war.png").end(".war"),
                m("ext_libreoffice_calc", "/icons/officedocs/localc.png").end(".ods"),
                m("ext_libreoffice_draw", "/icons/officedocs/lodraw.png").end(".odg"),
                m("ext_libreoffice_impress", "/icons/officedocs/loimpress.png").end(".odp"),
                m("ext_libreoffice_math", "/icons/officedocs/lomath.png").end(".odf"),
                m("ext_libreoffice_writer", "/icons/officedocs/lowriter.png").end(".odt"),
                m("ext_msoffice_excel", "/icons/officedocs/msexcel.png").end(".xls", ".xlsx"),
                m("ext_msoffice_onenote", "/icons/officedocs/msonenote.png").end(".one", ".onetoc2"),
                m("ext_msoffice_powerpoint", "/icons/officedocs/mspowerpoint.png").end(".ppt", ".pptx"),
                m("ext_msoffice_project", "/icons/officedocs/msproject.png").end(".mpd", ".mpp", ".mpt"),
                m("ext_msoffice_visio", "/icons/officedocs/msvisio.png").end(".vsd", ".vsdx", ".vss", ".vssx", ".vst", ".vstx"),
                m("ext_msoffice_word", "/icons/officedocs/msword.png").end(".doc", ".docx")
        );
        
        List<String> disabledModelIds = SettingsService.getDisabledModelIds();
        models = new ArrayList<>();
        for (Model model : allModels) {
            if (!disabledModelIds.contains(model.getId())) {
                models.add(model);
            }
        }
    }
    
    public Icon getIcon(@NotNull PsiElement psiElement, int flags) {
        PsiFile containingFile = psiElement.getContainingFile();
        if (containingFile != null) {
            String name = containingFile.getName().toLowerCase();
            for (Model m : models) {
                if (m.check(name)) {
                    return IconLoader.getIcon(m.getIcon());
                }
            }
        }
        return null;
    }
    
    @NotNull
    @Contract("_, _ -> new")
    private Model m(String id, String icon) {
        return new Model(id, icon);
    }
}
