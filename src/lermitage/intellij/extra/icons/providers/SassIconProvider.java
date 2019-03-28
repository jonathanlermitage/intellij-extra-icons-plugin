package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Icon provider specific for SASS files.
 * This should be registered only if the bundled SASS plugin is enabled.
 *
 * @author Edoardo Luppi
 */
class SassIconProvider extends BaseIconProvider implements DumbAware {
    @NotNull
    @Override
    protected List<Model> getModels() {
        return Collections.singletonList(
                new Model("sass", "/icons/sass.svg").end(".sass", ".scss")
        );
    }
    
    @Override
    protected boolean isSupported(@NotNull final PsiFile psiFile) {
        return true;
    }
}
