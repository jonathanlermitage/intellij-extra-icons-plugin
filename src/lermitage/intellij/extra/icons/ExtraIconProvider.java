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
    
    public Icon getIcon(@NotNull PsiElement psiElement, int flags) {
        PsiFile containingFile = psiElement.getContainingFile();
        if (containingFile != null) {
            String name = containingFile.getName().toUpperCase();
    
            if (name.endsWith("TRAVIS.YML") || name.endsWith("TRAVIS.YAML")) {
                return IconLoader.getIcon(TRAVIS_ICON);
            }
    
            if (name.endsWith("APPVEYOR.YML") || name.endsWith("APPVEYOR.YAML")) {
                return IconLoader.getIcon(APPVEYOR_ICON);
            }
    
    
        }
        return null;
    }
}
