package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Icon provider specific for Javascript-related files.
 * This should be registered only if the bundled Javascript Support plugin is enabled.
 *
 * @author Edoardo Luppi
 */
public class JavascriptIconProvider extends BaseIconProvider implements DumbAware {
    
    private List<Model> models;
    
    @NotNull
    public static List<Model> allModels() {
        return Collections.singletonList(
                new Model("typescript", "/icons/test-ts.svg", "Typescript: *.spec.ts")
                        .end(".spec.ts")
        );
    }
    
    public JavascriptIconProvider() {
        super();
        models = allModels();
        List<String> disabledModelIds = SettingsService.getDisabledModelIds();
        models.forEach(model -> model.setEnabled(!disabledModelIds.contains(model.getId())));
    }
    
    @NotNull
    @Override
    protected List<Model> getModels() {
        return models;
    }
    
    @Override
    protected boolean isSupported(@NotNull final PsiFile psiFile) {
        return true;
    }
}
