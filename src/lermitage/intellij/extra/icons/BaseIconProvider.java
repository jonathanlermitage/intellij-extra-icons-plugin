package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
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
        if (psiElement instanceof PsiDirectory) {
            PsiDirectory psiDirectory = (PsiDirectory) psiElement;
            final String parentName = psiDirectory.getParent() == null ? null : psiDirectory.getParent().getName().toLowerCase();
            final String folderName = psiDirectory.getName().toLowerCase();
            final Optional<String> fullPath = getFullPath(psiDirectory);
            for (final Model model : models) {
                if (model.getModelType() == ModelType.DIR && isModelEnabled(model) && model.check(parentName, folderName, fullPath)) {
                    return IconLoader.getIcon(model.getIcon());
                }
            }
        } else {
            final PsiFile file;
            final Optional<PsiFile> optFile = Optional.ofNullable(psiElement.getContainingFile());
            if (optFile.isPresent() && isSupported(file = optFile.get())) {
                final String parentName = file.getParent() == null ? null : file.getParent().getName().toLowerCase();
                final String fileName = file.getName().toLowerCase();
                final Optional<String> fullPath = getFullPath(file);
                for (final Model model : models) {
                    if (model.getModelType() == ModelType.FILE && isModelEnabled(model) && model.check(parentName, fileName, fullPath)) {
                        return IconLoader.getIcon(model.getIcon());
                    }
                }
            }
        }
        
        return null;
    }
    
    private Optional<String> getFullPath(@NotNull PsiFileSystemItem file) {
        if (file.getVirtualFile() != null) {
            return Optional.of(file.getVirtualFile().getPath().toLowerCase());
        }
        return Optional.empty();
    }

    private boolean isModelEnabled(Model model) {
        return !SettingsService.getDisabledModelIds().contains(model.getId());
    }
}
