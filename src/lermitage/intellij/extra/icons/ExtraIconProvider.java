package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public class ExtraIconProvider extends IconProvider {
    
    private static final String TRAVIS_ICON = "/icons/travis.png";
    private static final String APPVEYOR_ICON = "/icons/appveyor.png";
    private static final String JENKINS_ICON = "/icons/jenkins.png";
    private static final String GITLAB_ICON = "/icons/gitlab.png";
    
    public Icon getIcon(@NotNull PsiElement psiElement, int flags) {
        PsiFile containingFile = psiElement.getContainingFile();
        if (containingFile != null) {
            String name = containingFile.getName().toUpperCase();
            
            if (name.equalsIgnoreCase(".travis.yml")) {
                return IconLoader.getIcon(TRAVIS_ICON);
            }
            
            if (name.equalsIgnoreCase("appveyor.yml")) {
                return IconLoader.getIcon(APPVEYOR_ICON);
            }
            
            if (name.equalsIgnoreCase("jenkinsfile")) {
                return IconLoader.getIcon(JENKINS_ICON);
            }
            
            if (name.equalsIgnoreCase(".gitlab-ci.yml")) {
                return IconLoader.getIcon(GITLAB_ICON);
            }
        }
        return null;
    }
}
