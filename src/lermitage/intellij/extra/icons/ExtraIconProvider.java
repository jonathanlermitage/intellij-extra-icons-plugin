package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import java.util.List;

import static java.util.Arrays.asList;

public class ExtraIconProvider extends IconProvider implements DumbAware {
    
    private List<Model> models;
    
    public ExtraIconProvider() {
        super();
        String[] txt = new String[]{".md", ".txt", ".adoc"};
        String[] cfg = new String[]{".xml", ".yml", ".yaml", ".properties", ".json", ".cfg", ".conf", ".ini", ".txt"};
        String[] yml = new String[]{".yaml", ".yml"};
        models = asList(
                m("/icons/apache.png").eq(".htaccess"),
                m("/icons/appveyor.png").eq("appveyor.yml"),
                m("/icons/archunit.png").eq("archunit.properties"),
                m("/icons/authors.png").eq("author", "authors").mayEnd(txt),
                m("/icons/bamboo.png").eq("bamboo.yml"),
                m("/icons/bash-gradlew.png").eq("gradlew", "gradle.properties"),
                m("/icons/bash-mvnw.png").eq("mvnw"),
                m("/icons/berkshelf.png").eq("berksfile"),
                m("/icons/berkshelflock.png").eq("berksfile.lock"),
                m("/icons/bower.png").eq("bower.json", ".bowerrc"),
                m("/icons/build.png").eq("build", "building").mayEnd(txt),
                m("/icons/cassandra.png").eq("cassandra").mayEnd(yml),
                m("/icons/cassandra.png").start("cassandra").end(yml),
                m("/icons/changelog.png").eq("changelog", "changes").mayEnd(txt),
                m("/icons/circleci.png").eq("circle.yml"),
                m("/icons/cirrus.png").eq(".cirrus.yml"),
                m("/icons/cmd-gradlew.png").eq("gradlew.bat", "gradlew.cmd"),
                m("/icons/cmd-mvnw.png").eq("mvnw.bat", "mvnw.cmd"),
                m("/icons/codecov.png").eq(".codecov.yml", "codecov.yml"),
                m("/icons/codefresh.png").eq("codefresh.yml"),
                m("/icons/codacy.png").eq(".codacy").mayEnd(yml),
                m("/icons/codeship.png").eq("codeship-steps").mayEnd(cfg),
                m("/icons/contact.png").eq("contact", "contacts").mayEnd(txt),
                m("/icons/contributing.png").eq("contribute", "contributing", "contribution").mayEnd(txt),
                m("/icons/deptective.png").eq("deptective.json"),
                m("/icons/docker.png").eq("dockerfile", "docker-compose", ".dockerignore").mayEnd(cfg),
                m("/icons/docker.png").start("dockerfile", "docker-compose", ".dockerignore").end(cfg),
                m("/icons/editorconfig.png").eq(".editorconfig").mayEnd(cfg),
                m("/icons/elasticsearch.png").eq("elastic").mayEnd(yml),
                m("/icons/elasticsearch.png").start("elastic").end(yml),
                m("/icons/elastic-cerebro.png").eq("cerebro").mayEnd(".conf"),
                m("/icons/elastic-cerebro.png").start("cerebro").end(".conf"),
                m("/icons/email.png").eq(".mailmap"),
                m("/icons/gatling.png").eq("gatling").mayEnd(".conf"),
                m("/icons/gatling.png").start("gatling").end(".conf"),
                m("/icons/git.png").eq(".gitattributes", ".gitignore", ".gitmodules"),
                m("/icons/gitlab.png").eq(".gitlab-ci.yml"),
                m("/icons/graphql-config.png").eq("graphql.config.json"),
                m("/icons/graphql-schema.png").eq("graphql.schema.json"),
                m("/icons/gocd.png").eq(".gocd").mayEnd(cfg),
                m("/icons/java.png").eq("jvm.properties"),
                m("/icons/jenkins.png").eq("jenkinsfile", "jenkins").mayEnd(cfg),
                m("/icons/jenkins.png").start("jenkins").end(cfg),
                m("/icons/jenkins.png").start("jenkins").noDot(),
                m("/icons/jsbeautify.png").eq(".jsbeautifyrc").mayEnd(cfg),
                m("/icons/jshint.png").eq(".jshintrc").mayEnd(cfg),
                m("/icons/junit5.png").eq("junit-platform.properties"),
                m("/icons/kibana.png").eq("kibana").mayEnd(yml),
                m("/icons/kibana.png").start("kibana").end(yml),
                m("/icons/kubernetes.png").eq("kubernetes").mayEnd(yml),
                m("/icons/kubernetes.png").start("kubernetes").end(yml),
                m("/icons/license.png").eq("license", "copying", "license_info", "additional_license_info").mayEnd(txt),
                m("/icons/log4j.png").eq("log4j", "log4j-test").mayEnd(cfg),
                m("/icons/logback.png").eq("logback", "logback-test").mayEnd(cfg),
                m("/icons/logstash.png").eq("logstash").mayEnd(".cfg", ".conf", ".yml", ".yaml"),
                m("/icons/logstash.png").start("logstash").end(".cfg", ".conf", ".yml", ".yaml"),
                m("/icons/lombok.png").eq("lombok.config"),
                m("/icons/my.png").eq("my.ini"),
                m("/icons/nginx.png").eq("nginx").mayEnd(".conf"),
                m("/icons/nginx.png").start("nginx").end(".conf"),
                m("/icons/notice.png").eq("notice").mayEnd(txt),
                m("/icons/packageinfojava.png").eq("package-info.java"),
                m("/icons/packagejson.png").eq("package.json"),
                m("/icons/packagejsonlock.png").eq("package-lock.json"),
                m("/icons/privacy.png").eq("privacy").mayEnd(txt),
                m("/icons/puppet.png").eq("puppet").mayEnd(".conf"),
                m("/icons/puppet.png").start("puppet").end(".conf"),
                m("/icons/readme.png").eq("readme", "lisezmoi").mayEnd(txt),
                m("/icons/redis.png").eq("redis").mayEnd(".conf"),
                m("/icons/redis.png").start("redis").end(".conf"),
                m("/icons/roadmap.png").eq("roadmap").mayEnd(txt),
                m("/icons/testing.png").eq("test", "testing").mayEnd(txt),
                m("/icons/todo.png").eq("todo").mayEnd(txt),
                m("/icons/travis.png").eq(".travis.yml"),
                m("/icons/vagrant.png").eq("vagrantfile"),
                m("/icons/version.png").eq("version").mayEnd(txt),
                m("/icons/zalando.png").eq(".zappr.yaml"),
        
                m("/icons/archive.png").end(".zip", ".7z", ".tar", ".gz", ".bz2"),
                m("/icons/asciidoc.png").end(".adoc", ".asciidoc"),
                m("/icons/backup.png").end(".versionbackup", ".versionsbackup", ".back", ".backup", ".old", ".prev", ".revert"),
                m("/icons/bash.png").end(".sh"),
                m("/icons/certificate.png").end(".jks", ".pem", ".crt", ".cert", ".ca-bundle", ".cer", ".p7b", ".p7s", ".pfx"),
                m("/icons/cmd.png").end(".cmd", ".bat", ".ps1"),
                m("/icons/csv.png").end(".csv"),
                m("/icons/deb.png").end(".deb"),
                m("/icons/form.png").end(".form"),
                m("/icons/graphql.png").end(".graphqls"),
                m("/icons/haxe.png").end(".hx"),
                m("/icons/haxehxml.png").end(".hxml"),
                m("/icons/ijbeam.png").end(".iml"),
                m("/icons/ini.png").end(".ini", ".cfg", ".conf"),
                m("/icons/jar.png").end(".jar"),
                m("/icons/keepass.png").end(".kdbx"),
                m("/icons/markdown.png").end(".md"),
                m("/icons/nsis.png").end(".nsi"),
                m("/icons/pdf.png").end(".pdf"),
                m("/icons/rpm.png").end(".rpm"),
                m("/icons/terraform.png").end(".hcl", ".tf", ".tf.json"),
                m("/icons/toml.png").end(".toml"),
                m("/icons/video.png").end(".3g2", ".3gp", ".avi", ".divx", ".f4a", ".f4b", ".f4p", ".f4v", ".flv", ".m4p",
                        ".m4v", ".mkv", ".mp4", ".mpg", ".mpeg", ".mov", ".ogm", ".ogv", ".vob", ".webm", ".wmv", ".xvid"),
                m("/icons/war.png").end(".war"),
                m("/icons/officedocs/localc.png").end(".ods"),
                m("/icons/officedocs/lodraw.png").end(".odg"),
                m("/icons/officedocs/loimpress.png").end(".odp"),
                m("/icons/officedocs/lomath.png").end(".odf"),
                m("/icons/officedocs/lowriter.png").end(".odt"),
                m("/icons/officedocs/msexcel.png").end(".xls", ".xlsx"),
                m("/icons/officedocs/msonenote.png").end(".one", ".onetoc2"),
                m("/icons/officedocs/mspowerpoint.png").end(".ppt", ".pptx"),
                m("/icons/officedocs/msproject.png").end(".mpd", ".mpp", ".mpt"),
                m("/icons/officedocs/msvisio.png").end(".vsd", ".vsdx", ".vss", ".vssx", ".vst", ".vstx"),
                m("/icons/officedocs/msword.png").end(".doc", ".docx")
        );
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
    
    private Model m(String icon) {
        return new Model(icon);
    }
}
