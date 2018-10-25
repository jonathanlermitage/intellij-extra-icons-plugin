package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import java.util.List;

import static java.util.Arrays.asList;

public class ExtraIconProvider extends IconProvider {
    
    private List<Model> models;
    
    public ExtraIconProvider() {
        super();
        String[] txt = new String[]{".md", ".txt", ".adoc"};
        String[] cfg = new String[]{".xml", ".yml", ".yaml", ".properties", ".json", ".conf", ".ini", ".txt"};
        String[] yml = new String[]{".yaml", ".yml"};
        models = asList(
                m("/icons/apache.png").eq(".htaccess"),
                m("/icons/appveyor.png").eq("appveyor.yml"),
                m("/icons/authors.png").eq("author", "authors").mayEnd(txt),
                m("/icons/bamboo.png").eq("bamboo.yml"),
                m("/icons/bash-gradlew.png").eq("gradlew"),
                m("/icons/bash-mvnw.png").eq("mvnw"),
                m("/icons/berkshelf.png").eq("berksfile"),
                m("/icons/berkshelflock.png").eq("berksfile.lock"),
                m("/icons/bower.png").eq("bower.json", ".bowerrc"),
                m("/icons/build.png").eq("build", "building").mayEnd(txt),
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
                m("/icons/docker.png").eq("dockerfile", "docker-compose", ".dockerignore").mayEnd(yml),
                m("/icons/editorconfig.png").eq(".editorconfig"),
                m("/icons/email.png").eq(".mailmap"),
                m("/icons/git.png").eq(".gitattributes", ".gitignore", ".gitmodules"),
                m("/icons/gitlab.png").eq(".gitlab-ci.yml"),
                m("/icons/gocd.png").eq(".gocd").mayEnd(cfg),
                m("/icons/jenkins.png").start("jenkinsfile", "jenkins").mayEnd(cfg),
                m("/icons/jsbeautify.png").eq(".jsbeautifyrc").mayEnd(cfg),
                m("/icons/jshint.png").eq(".jshintrc").mayEnd(cfg),
                m("/icons/license.png").eq("license", "copying", "license_info", "additional_license_info").mayEnd(txt),
                m("/icons/log4j.png").eq("log4j", "log4j-test").mayEnd(cfg),
                m("/icons/logback.png").eq("logback", "logback-test").mayEnd(cfg),
                m("/icons/my.png").eq("my.ini"),
                m("/icons/nginx.png").eq("nginx").mayEnd(cfg),
                m("/icons/notice.png").eq("notice").mayEnd(txt),
                m("/icons/packagejson.png").eq("package.json"),
                m("/icons/packagejsonlock.png").eq("package-lock.json"),
                m("/icons/privacy.png").eq("privacy").mayEnd(txt),
                m("/icons/puppet.png").eq("puppet").mayEnd(cfg),
                m("/icons/readme.png").eq("readme").mayEnd(txt),
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
                m("/icons/ijbeam.png").end(".iml"),
                m("/icons/ini.png").end(".ini", ".cfg", ".conf"),
                m("/icons/jar.png").end(".jar"),
                m("/icons/keepass.png").end(".kdbx"),
                m("/icons/markdown.png").end(".md"),
                m("/icons/pdf.png").end(".pdf"),
                m("/icons/rpm.png").end(".rpm"),
                m("/icons/terraform.png").end(".hcl", ".tf", ".tf.json"),
                m("/icons/toml.png").end(".toml"),
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
