package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import lermitage.intellij.extra.icons.cfg.settings.SettingsIDEService;
import lermitage.intellij.extra.icons.cfg.settings.SettingsProjectService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
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

    private String parent(PsiFileSystemItem psiDirectory) {
        return psiDirectory.getParent() == null ? null : psiDirectory.getParent().getName().toLowerCase();
    }

    @Nullable
    @Override
    public final Icon getIcon(@NotNull final PsiElement psiElement, final int flags) {
        if (psiElement instanceof PsiDirectory) {
            PsiDirectory psiDirectory = (PsiDirectory) psiElement;
            final String parentName = parent(psiDirectory);
            final String folderName = psiDirectory.getName().toLowerCase();
            if (isPatternIgnored(psiElement.getProject(), parentName, folderName)) {
                return null;
            }
            final Optional<String> fullPath = getFullPath(psiDirectory);
            for (final Model model : models) {
                if (model.getModelType() == ModelType.DIR && isModelEnabled(psiElement.getProject(), model) && model.check(parentName, folderName, fullPath)) {
                    return IconLoader.getIcon(model.getIcon());
                }
            }
        } else {
            final PsiFile file;
            final Optional<PsiFile> optFile = Optional.ofNullable(psiElement.getContainingFile());
            if (optFile.isPresent() && isSupported(file = optFile.get())) {
                final String parentName = parent(file);
                final String fileName = file.getName().toLowerCase();
                if (isPatternIgnored(psiElement.getProject(), parentName, fileName)) {
                    return null;
                }
                final Optional<String> fullPath = getFullPath(file);
                for (final Model model : models) {
                    if (model.getModelType() == ModelType.FILE && isModelEnabled(psiElement.getProject(), model) && model.check(parentName, fileName, fullPath)) {
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

    private boolean isModelEnabled(Project project, Model model) {
        SettingsService service = SettingsService.getInstance(project);
        if (!((SettingsProjectService) service).isOverrideIDESettings()) {
            service = SettingsIDEService.getInstance();
        }
        return !service.getDisabledModelIds().contains(model.getId());
    }

    /**
     * Indicates if given file/folder should be ignored.
     * @param project project.
     * @param parent optional parent.
     * @param file current file or folder.
     */
    private boolean isPatternIgnored(Project project, String parent, String file) {
        SettingsService service = SettingsService.getInstance(project);
        if (!((SettingsProjectService) service).isOverrideIDESettings()) {
            service = SettingsIDEService.getInstance();
        }
        if (service.getIgnoredPatternObj() == null || service.getIgnoredPattern().isEmpty()) {
            return false;
        }
        return service.getIgnoredPatternObj().matcher(parent == null ? "" : parent + "/" + file).matches();
    }
}
