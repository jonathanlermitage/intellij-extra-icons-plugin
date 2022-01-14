// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.ide.FileIconProvider;
import com.intellij.ide.IconProvider;
import com.intellij.openapi.diagnostic.ControlFlowException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.changes.FilePathIconProvider;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiManager;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import lermitage.intellij.extra.icons.cfg.services.impl.SettingsIDEService;
import lermitage.intellij.extra.icons.cfg.services.impl.SettingsProjectService;
import lermitage.intellij.extra.icons.utils.IconUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Edoardo Luppi
 * @author Jonathan Lermitage
 */
public abstract class BaseIconProvider
    extends IconProvider /* to override icons in Project view */
    implements FilePathIconProvider, /* to override icons in VCS (Git, etc.) views */
    FileIconProvider /* to override icons in Project view */ {

    private static final Logger LOGGER = Logger.getInstance(BaseIconProvider.class);

    private final List<Model> models;

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

    private void logError(@NotNull Throwable e) {
        if (e instanceof ControlFlowException) {
            // Control-flow exceptions should never be logged.
            return;
        }
        // Workaround for https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/39
        // Plugin may want to reload icon on closed or disposed project. Just ignore it
        if (e.getMessage() != null) {
            String errMsg = e.getMessage().toUpperCase();
            if (errMsg.contains("DISPOSE_IN_PROGRESS") || errMsg.contains("PROJECT IS ALREADY DISPOSED")) {
                LOGGER.debug(e);
            } else {
                LOGGER.warn(e);
            }
        }
    }

    @Nullable
    @Override
    public Icon getIcon(@NotNull FilePath filePath, @Nullable Project project) {
        try {
            if (ProjectUtils.isAlive(project)) {
                VirtualFile file = filePath.getVirtualFile();
                if (file == null) {
                    return null;
                }
                PsiFileSystemItem psiFileSystemItem;
                if (file.isDirectory()) {
                    psiFileSystemItem = PsiManager.getInstance(project).findDirectory(file);
                } else {
                    psiFileSystemItem = PsiManager.getInstance(project).findFile(file);
                }
                if (psiFileSystemItem != null) {
                    return getIcon(psiFileSystemItem, 0 /* flags are ignored */);
                }
            }
        } catch (Throwable e) {
            logError(e);
        }
        return null;
    }

    @Nullable
    @Override
    public Icon getIcon(@NotNull VirtualFile file, int flags, @Nullable Project project) {
        try {
            if (ProjectUtils.isAlive(project)) {
                PsiFileSystemItem psiFileSystemItem;
                if (file.isDirectory()) {
                    psiFileSystemItem = PsiManager.getInstance(project).findDirectory(file);
                } else {
                    psiFileSystemItem = PsiManager.getInstance(project).findFile(file);
                }
                if (psiFileSystemItem != null) {
                    return getIcon(psiFileSystemItem, flags);
                }
            }
        } catch (Throwable e) {
            logError(e);
        }
        return null;
    }

    @Nullable
    @Override
    public final Icon getIcon(@NotNull final PsiElement psiElement, final int flags) {
        try {
            Project project = psiElement.getProject();
            if (!ProjectUtils.isAlive(project)) {
                return null;
            }
            ModelType currentModelType;
            PsiFileSystemItem currentPsiFileItem;
            if (psiElement instanceof PsiDirectory) {
                currentPsiFileItem = (PsiFileSystemItem) psiElement;
                if (isPatternIgnored(project, currentPsiFileItem.getVirtualFile())) {
                    return null;
                }
                currentModelType = ModelType.DIR;
            } else {
                final PsiFile file;
                final Optional<PsiFile> optFile = Optional.ofNullable(psiElement.getContainingFile());
                if (optFile.isPresent() && isSupported(file = optFile.get())) {
                    if (isPatternIgnored(project, file.getVirtualFile())) {
                        return null;
                    }
                    currentPsiFileItem = file;
                    currentModelType = ModelType.FILE;
                } else {
                    return null;
                }
            }
            String parentName = parent(currentPsiFileItem);
            String currentFileName = currentPsiFileItem.getName().toLowerCase();
            Optional<String> fullPath = getFullPath(currentPsiFileItem);
            Set<String> facets = ProjectUtils.getFacets(project);
            Double additionalUIScale = SettingsService.getIDEInstance().getAdditionalUIScale();
            for (final Model model : getModelsIncludingUserModels(project)) {
                if (model.getModelType() == currentModelType && isModelEnabled(project, model) && model.check(parentName, currentFileName, fullPath, facets, project)) {
                    return IconUtils.getIcon(model, additionalUIScale);
                }
            }
        } catch (Throwable e) {
            logError(e);
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
            } else {
                customModelsStream = projectService.getCustomModels().stream();
            }
        } else {
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
