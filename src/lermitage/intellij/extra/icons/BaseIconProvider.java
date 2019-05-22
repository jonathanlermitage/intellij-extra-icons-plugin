package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
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
        if (psiElement instanceof PsiDirectory) {
            PsiDirectory psiDirectory = (PsiDirectory) psiElement;
            final String containingFolderName = psiDirectory.getParent() == null ? null : psiDirectory.getParent().getName().toLowerCase();
            final String folderName = psiDirectory.getName().toLowerCase();
            final Optional<String> filePathRelativeToProject = getPathRelativeToProject(psiDirectory, true, psiDirectory.getContext());
            for (final Model model : models) {
                if (model.getModelType() == ModelType.DIR && model.check(containingFolderName, folderName, filePathRelativeToProject)) {
                    return IconLoader.getIcon(model.getIcon());
                }
            }
        } else {
            final PsiFile file;
            final Optional<PsiFile> optFile = Optional.ofNullable(psiElement.getContainingFile());
            if (optFile.isPresent() && isSupported(file = optFile.get())) {
                final String containingFolderName = file.getParent() == null ? null : file.getParent().getName().toLowerCase();
                final String fileName = file.getName().toLowerCase();
                final Optional<String> filePathRelativeToProject = getPathRelativeToProject(file, false, file.getParent().getContext());
                for (final Model model : models) {
                    if (model.getModelType() == ModelType.FILE && model.check(containingFolderName, fileName, filePathRelativeToProject)) {
                        return IconLoader.getIcon(model.getIcon());
                    }
                }
            }
        }
        
        return null;
    }
    
    private Optional<String> getPathRelativeToProject(@NotNull PsiFileSystemItem file, boolean isFolder, PsiElement context) {
        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile != null) {
            String projectAbsolutePath = context.getProject().getBasePath();
            if (projectAbsolutePath != null) {
                String absolutePath = isFolder ? virtualFile.getPath() : virtualFile.getPath() + "/" + file.getName();
                String relativePath = absolutePath.substring(projectAbsolutePath.length()).toLowerCase();
                return Optional.of(relativePath.replaceAll("\\\\", "/"));
            }
        }
        return Optional.empty();
    }
}
