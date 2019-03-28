package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Icon provider specific for Javascript-related files.
 * This should be registered only if the bundled Javascript Support plugin is enabled.
 *
 * @author Edoardo Luppi
 */
class JavascriptIconProvider extends BaseIconProvider implements DumbAware {
    @NotNull
    @Override
    protected List<Model> getModels() {
        return Collections.singletonList(
                new Model("typescript", "/icons/test-ts.svg").end(".spec.ts")
        );
    }
    
    @Override
    protected boolean isSupported(@NotNull final PsiFile psiFile) {
        return true;
    }
}
