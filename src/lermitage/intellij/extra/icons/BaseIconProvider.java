// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import lermitage.intellij.extra.icons.cfg.services.impl.SettingsIDEService;
import lermitage.intellij.extra.icons.cfg.services.impl.SettingsProjectService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private String parent(@NotNull PsiFileSystemItem fileSystemItem) {
        return fileSystemItem.getParent() == null ? null : fileSystemItem.getParent().getName().toLowerCase();
    }

    @Nullable
    @Override
    public final Icon getIcon(@NotNull final PsiElement psiElement, final int flags) {
        Project project = psiElement.getProject();
        if (psiElement instanceof PsiDirectory) {
            PsiDirectory psiDirectory = (PsiDirectory) psiElement;
            final String parentName = parent(psiDirectory);
            final String folderName = psiDirectory.getName().toLowerCase();
            if (isPatternIgnored(project, psiDirectory.getVirtualFile())) {
                return null;
            }
            final Optional<String> fullPath = getFullPath(psiDirectory);
            for (final Model model : getModelsIncludingUserModels(project)) {
                if (model.getModelType() == ModelType.DIR && isModelEnabled(project, model) && model.check(parentName, folderName, fullPath)) {
                    return CustomIconLoader.getIcon(model);
                }
            }
        } else {
            final PsiFile file;
            final Optional<PsiFile> optFile = Optional.ofNullable(psiElement.getContainingFile());
            if (optFile.isPresent() && isSupported(file = optFile.get())) {
                final String parentName = parent(file);
                final String fileName = file.getName().toLowerCase();
                if (isPatternIgnored(project, file.getVirtualFile())) {
                    return null;
                }
                final Optional<String> fullPath = getFullPath(file);
                for (final Model model : getModelsIncludingUserModels(project)) {
                    if (model.getModelType() == ModelType.FILE && isModelEnabled(project, model) && model.check(parentName, fileName, fullPath)) {
                        return CustomIconLoader.getIcon(model);
                    }
                }
            }
        }

        return null;
    }

    /**
     * Depending on whether the checkbox in the settings is checked, this method appends
     * the user added models to the model list.
     */
    private List<Model> getModelsIncludingUserModels(@Nullable Project project) {
        SettingsService service = SettingsService.getInstance(project);
        SettingsProjectService projectService = (SettingsProjectService) service;
        Stream<Model> customModelsStream;

        if (projectService.isOverrideIDESettings()) {
            if (projectService.isAddToIDEUserIcons()) {
                customModelsStream = Stream.concat(SettingsIDEService.getInstance().getCustomModels().stream(),
                    projectService.getCustomModels().stream());
            }
            else {
                customModelsStream = projectService.getCustomModels().stream();
            }
        }
        else {
            customModelsStream = SettingsIDEService.getInstance().getCustomModels().stream();
        }

        return Stream.concat(customModelsStream, models.stream()).collect(Collectors.toList());
    }

    private Optional<String> getFullPath(@NotNull PsiFileSystemItem file) {
        if (file.getVirtualFile() != null) {
            return Optional.of(file.getVirtualFile().getPath().toLowerCase());
        }
        return Optional.empty();
    }

    private boolean isModelEnabled(@Nullable Project project, @NotNull Model model) {
        SettingsService service = getSettingsService(project);
        return !service.getDisabledModelIds().contains(model.getId()) && model.isEnabled();
    }

    /**
     * Returns the project service if the checkbox in the project settings was checked,
     * otherwise returns the IDE settings service.
     */
    private SettingsService getSettingsService(Project project) {
        SettingsService service = SettingsService.getInstance(project);
        if (!((SettingsProjectService) service).isOverrideIDESettings()) {
            service = SettingsIDEService.getInstance();
        }
        return service;
    }

    /**
     * Indicates if given file/folder should be ignored.
     * @param project project.
     * @param file current file or folder.
     */
    private boolean isPatternIgnored(Project project, VirtualFile file) {
        SettingsService service = getSettingsService(project);
        if (service.getIgnoredPatternObj() == null || service.getIgnoredPattern() == null || service.getIgnoredPattern().isEmpty()) {
            return false;
        }
        VirtualFile contentRoot = ProjectFileIndex.SERVICE.getInstance(project).getContentRootForFile(file);
        if (contentRoot == null) {
            return false;
        }
        String relativePath = VfsUtilCore.getRelativePath(file, contentRoot);
        if (relativePath == null) {
            return false;
        }
        return service.getIgnoredPatternObj().matcher(relativePath).matches();
    }
}
