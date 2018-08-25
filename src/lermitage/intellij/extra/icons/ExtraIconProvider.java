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
    private static final String BASH_ICON = "/icons/bash.png";
    private static final String BASH_GRADLEW_ICON = "/icons/bash-gradlew.png";
    private static final String BASH_MVNW_ICON = "/icons/bash-mvnw.png";
    private static final String CMD_ICON = "/icons/cmd.png";
    private static final String CMD_GRADLEW_ICON = "/icons/cmd-gradlew.png";
    private static final String CMD_MVNW_ICON = "/icons/cmd-mvnw.png";
    
    public Icon getIcon(@NotNull PsiElement psiElement, int flags) {
        PsiFile containingFile = psiElement.getContainingFile();
        if (containingFile != null) {
            String name = containingFile.getName().toLowerCase();
            
            if (name.equals(".travis.yml")) {
                return IconLoader.getIcon(TRAVIS_ICON);
            } else if (name.equals("appveyor.yml")) {
                return IconLoader.getIcon(APPVEYOR_ICON);
            } else if (name.equals("jenkinsfile")) {
                return IconLoader.getIcon(JENKINS_ICON);
            } else if (name.equals(".gitlab-ci.yml")) {
                return IconLoader.getIcon(GITLAB_ICON);
            } else if (name.equals("gradlew")) {
                return IconLoader.getIcon(BASH_GRADLEW_ICON);
            } else if (name.equals("mvnw")) {
                return IconLoader.getIcon(BASH_MVNW_ICON);
            } else if (name.endsWith(".sh")) {
                return IconLoader.getIcon(BASH_ICON);
            } else if (name.equals("gradlew.cmd") || name.equals("gradlew.bat")) {
                return IconLoader.getIcon(CMD_GRADLEW_ICON);
            } else if (name.equals("mvnw.cmd") || name.equals("mvnw.bat")) {
                return IconLoader.getIcon(CMD_MVNW_ICON);
            } else if (name.endsWith(".cmd") || name.endsWith(".bat") || name.endsWith(".ps1")) {
                return IconLoader.getIcon(CMD_ICON);
            }
        }
        return null;
    }
}
