package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.List;
import java.util.Optional;

/**
 * @author Edoardo Luppi
 * @author Jonathan Lermitage
 */
public abstract class BaseIconProvider extends IconProvider {
    
    private List<Model> models;
    
    public BaseIconProvider() {
        super();
        this.models = getAllModels();
        List<String> disabledModelIds = SettingsService.getDisabledModelIds();
        models.forEach(model -> model.setEnabled(!disabledModelIds.contains(model.getId())));
    }
    
    /**
     * Get list of all models managed by this icon provider. Their 'enabled' field doesn't matter.
     * This list will be processed by constructor and models 'enabled' field updated according to running IDE configuration.
     */
    protected abstract List<Model> getAllModels();
    
    /**
     * Check whether this icon provider supports the input file.
     * If not overridden, returns {@code true}.
     */
    protected boolean isSupported(@NotNull final PsiFile psiFile) {
        return true;
    }
    
    @Nullable
    @Override
    public final Icon getIcon(@NotNull final PsiElement psiElement, final int flags) {
        final Optional<PsiFile> optFile = Optional.ofNullable(psiElement.getContainingFile());
        final PsiFile file;
        
        if (optFile.isPresent() && isSupported(file = optFile.get())) {
            final String fileName = file.getName().toLowerCase();
            for (final Model model : models) {
                if (model.check(fileName)) {
                    return IconLoader.getIcon(model.getIcon());
                }
            }
        }
        
        return null;
    }
}
