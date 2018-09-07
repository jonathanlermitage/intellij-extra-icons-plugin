package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import java.util.Arrays;
import java.util.List;

public class ExtraIconProvider extends IconProvider {
    
    private List<Model> models;
    
    public ExtraIconProvider() {
        super();
        models = Arrays.asList(
                new Model("/icons/appveyor.png").willEqual("appveyor.yml"),
                new Model("/icons/authors.png").willEqualAndMayEndWith("author", ".md", ".txt", ".adoc"),
                new Model("/icons/authors.png").willEqualAndMayEndWith("authors", ".md", ".txt", ".adoc"),
                new Model("/icons/bamboo.png").willEqual("bamboo.yml"),
                new Model("/icons/bash-gradlew.png").willEqual("gradlew"),
                new Model("/icons/bash-mvnw.png").willEqual("mvnw"),
                new Model("/icons/berkshelf.png").willEqual("berksfile"),
                new Model("/icons/berkshelf.png").willEqual("berksfile.lock"),
                new Model("/icons/changelog.png").willEqualAndMayEndWith("changelog", ".md", ".txt", ".adoc"),
                new Model("/icons/changelog.png").willEqualAndMayEndWith("changes", ".md", ".txt", ".adoc"),
                new Model("/icons/circleci.png").willEqual("circle.yml"),
                new Model("/icons/cmd-gradlew.png").willEqual("gradlew.bat"),
                new Model("/icons/cmd-gradlew.png").willEqual("gradlew.cmd"),
                new Model("/icons/cmd-mvnw.png").willEqual("mvnw.bat"),
                new Model("/icons/cmd-mvnw.png").willEqual("mvnw.cmd"),
                new Model("/icons/codefresh.png").willEqual("codefresh.yml"),
                new Model("/icons/codeship.png").willEqual("codeship-steps.yml"),
                new Model("/icons/codeship.png").willEqualAndMayEndWith("codeship-steps", "json", "yml"),
                new Model("/icons/contact.png").willEqualAndMayEndWith("contact", ".md", ".txt", ".adoc"),
                new Model("/icons/contact.png").willEqualAndMayEndWith("contacts", ".md", ".txt", ".adoc"),
                new Model("/icons/contributing.png").willEqualAndMayEndWith("contributing", ".md", ".txt", ".adoc"),
                new Model("/icons/docker.png").willEqual("dockerfile"),
                new Model("/icons/docker.png").willEqual("docker-compose.yml"),
                new Model("/icons/editorconfig.png").willEqual(".editorconfig"),
                new Model("/icons/email.png").willEqual(".mailmap"),
                new Model("/icons/git.png").willEqual(".gitattributes"),
                new Model("/icons/git.png").willEqual(".gitignore"),
                new Model("/icons/gitlab.png").willEqual(".gitlab-ci.yml"),
                new Model("/icons/gocd.png").willEqualAndMayEndWith(".gocd", "yml", "yaml"),
                new Model("/icons/intellijidea.png").willEndWith("iml"),
                new Model("/icons/jenkins.png").willEqual("jenkinsfile"),
                new Model("/icons/jenkins.png").willEqualAndMayEndWith("jenkins", "txt", "yml"),
                new Model("/icons/license.png").willEqualAndMayEndWith("license", ".md", ".txt", ".adoc"),
                new Model("/icons/license.png").willEqualAndMayEndWith("copying", ".md", ".txt", ".adoc"),
                new Model("/icons/notice.png").willEqualAndMayEndWith("notice", ".md", ".txt", ".adoc"),
                new Model("/icons/packagejson.png").willEqual("package.json"),
                new Model("/icons/packagejsonlock.png").willEqual("package-lock.json"),
                new Model("/icons/readme.png").willEqualAndMayEndWith("readme", ".md", ".txt", ".adoc"),
                new Model("/icons/travis.png").willEqual(".travis.yml"),
                new Model("/icons/vagrant.png").willEqual("vagrantfile"),
                new Model("/icons/version.png").willEqualAndMayEndWith("version", ".md", ".txt", ".adoc"),
        
                new Model("/icons/bash.png").willEndWith(".sh"),
                new Model("/icons/certificate.png").willEndWith(".pem", ".crt", ".ca-bundle", ".cer", ".p7b", ".p7s", ".pfx"),
                new Model("/icons/cmd.png").willEndWith(".cmd", ".bat", ".ps1"),
                new Model("/icons/jar.png").willEndWith(".jar"),
                new Model("/icons/keepass.png").willEndWith(".kdbx")
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
}
