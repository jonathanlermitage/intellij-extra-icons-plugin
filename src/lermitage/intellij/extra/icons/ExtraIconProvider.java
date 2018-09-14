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
        String[] cfg = new String[]{".xml", ".yml", ".yaml", ".properties", ".json"};
        models = asList(
                m("/icons/appveyor.png").eq("appveyor.yml"),
                m("/icons/authors.png").eq("author", "authors").mayEnd(txt),
                m("/icons/bamboo.png").eq("bamboo.yml"),
                m("/icons/bash-gradlew.png").eq("gradlew"),
                m("/icons/bash-mvnw.png").eq("mvnw"),
                m("/icons/berkshelf.png").eq("berksfile"),
                m("/icons/berkshelflock.png").eq("berksfile.lock"),
                m("/icons/changelog.png").eq("changelog", "changes").mayEnd(txt),
                m("/icons/circleci.png").eq("circle.yml"),
                m("/icons/cmd-gradlew.png").eq("gradlew.bat", "gradlew.cmd"),
                m("/icons/cmd-mvnw.png").eq("mvnw.bat", "mvnw.cmd"),
                m("/icons/codefresh.png").eq("codefresh.yml"),
                m("/icons/codeship.png").eq("codeship-steps").mayEnd(".json", ".yml"),
                m("/icons/contact.png").eq("contact", "contacts").mayEnd(txt),
                m("/icons/contributing.png").eq("contributing").mayEnd(txt),
                m("/icons/docker.png").eq("dockerfile", "docker-compose.yml"),
                m("/icons/editorconfig.png").eq(".editorconfig"),
                m("/icons/email.png").eq(".mailmap"),
                m("/icons/git.png").eq(".gitattributes", ".gitignore", ".gitmodules"),
                m("/icons/gitlab.png").eq(".gitlab-ci.yml"),
                m("/icons/gocd.png").eq(".gocd").mayEnd(".yml", ".yaml"),
                m("/icons/jenkins.png").eq("jenkinsfile", "jenkins").mayEnd(".txt", ".yml"),
                m("/icons/license.png").eq("license", "copying").mayEnd(txt),
                m("/icons/log4j.png").eq("log4j", "log4j-test").mayEnd(cfg),
                m("/icons/logback.png").eq("logback", "logback-test").mayEnd(cfg),
                m("/icons/notice.png").eq("notice").mayEnd(txt),
                m("/icons/packagejson.png").eq("package.json"),
                m("/icons/packagejsonlock.png").eq("package-lock.json"),
                m("/icons/readme.png").eq("readme").mayEnd(txt),
                m("/icons/travis.png").eq(".travis.yml"),
                m("/icons/vagrant.png").eq("vagrantfile"),
                m("/icons/version.png").eq("version").mayEnd(txt),
                
                m("/icons/asciidoc.png").end(".adoc"),
                m("/icons/backup.png").end(".versionbackup", ".back", ".backup", ".old", ".prev", ".revert"),
                m("/icons/bash.png").end(".sh"),
                m("/icons/certificate.png").end(".pem", ".crt", ".cert", ".ca-bundle", ".cer", ".p7b", ".p7s", ".pfx"),
                m("/icons/cmd.png").end(".cmd", ".bat", ".ps1"),
                m("/icons/csv.png").end(".csv"),
                m("/icons/intellijidea.png").end(".iml"),
                m("/icons/jar.png").end(".jar"),
                m("/icons/keepass.png").end(".kdbx"),
                m("/icons/markdown.png").end(".md"),
                m("/icons/pdf.png").end(".pdf"),
                
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
