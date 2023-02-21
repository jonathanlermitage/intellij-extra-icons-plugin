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
import com.intellij.testFramework.LightVirtualFile;
import lermitage.intellij.extra.icons.cfg.services.SettingsIDEService;
import lermitage.intellij.extra.icons.cfg.services.SettingsProjectService;
import lermitage.intellij.extra.icons.cfg.services.SettingsService;
import lermitage.intellij.extra.icons.services.FacetsFinderService;
import lermitage.intellij.extra.icons.utils.I18nUtils;
import lermitage.intellij.extra.icons.utils.IconUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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

    private static final @NonNls Logger LOGGER = Logger.getInstance(BaseIconProvider.class);
    private final String className = this.getClass().getSimpleName();

    private final List<Model> models;

    private long checks_done = 0;
    private long checks_saved = 0;

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    public BaseIconProvider() {
        super();
        this.models = getAllModels();
    }

    /**
     * Get list of all models managed by this icon provider. Their 'enabled' field doesn't matter.
     * This list will be processed by constructor and models 'enabled' field updated according to running IDE configuration.
     */
    protected abstract List<Model> getAllModels();

    private static Model extractAltModel(Model model, int altIconIdx) {
        String altDescription;
        String altId;
        if (altIconIdx < 1) {
            if (model.getAltIcons().length == 1) {
                altDescription = MessageFormat.format(i18n.getString("model.desc.alternative"), model.getDescription());
            } else {
                altDescription = MessageFormat.format(i18n.getString("model.desc.alternative.first"), model.getDescription());
            }
            altId = model.getId() + "_alt"; //NON-NLS
        } else {
            altDescription = MessageFormat.format(i18n.getString("model.desc.alternative.other"), model.getDescription(),altIconIdx + 1);
            altId = model.getId() + "_alt" + (altIconIdx + 1); //NON-NLS
        }
        return Model.createAltModel(model, altId, model.getIdeIcon(), model.getAltIcons()[altIconIdx], altDescription);
    }

    public static Stream<Model> modelList(Model model) {
        if (model.getAltIcons() == null || model.getAltIcons().length == 0) {
            return Stream.of(model);
        }
        List<Model> models = new ArrayList<>();
        models.add(model);
        for (int i = 0; i < model.getAltIcons().length; i++) {
            models.add(extractAltModel(model, i));
        }
        return models.stream();
    }

    /**
     * Check whether this icon provider supports the input file.
     * If not overridden, returns {@code true}.
     */
    protected boolean isSupported(@SuppressWarnings("unused") @NotNull final PsiFile psiFile) {
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
            String errMsg = e.getMessage()
                .replaceAll("[\\s_]", "") //NON-NLS
                .toUpperCase();
            if (errMsg.contains("DISPOSEINPROGRESS") || errMsg.contains("PROJECTISALREADYDISPOSED")) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(e);
                }
            } else {
                LOGGER.warn(e);
            }
        }
    }

    @Nullable
    @Override
    public Icon getIcon(@NotNull FilePath filePath, @Nullable Project project) {
        try {
            if (ProjectUtils.isProjectAlive(project)) {
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
                    return getIcon(psiFileSystemItem, 0 /* flags are ignored (could be com.intellij.ui.icons.ImageDescriptor.HAS_2x or HAS_DARK_2x) */);
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
            if (ProjectUtils.isProjectAlive(project)) {
                PsiFileSystemItem psiFileSystemItem;
                if (file instanceof LightVirtualFile) {
                    // TODO need to reproduce and understand what happens in
                    //  https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/86
                    //  (error: Light files should have PSI only in one project)
                    return null;
                } else if (file.isDirectory()) {
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
            if (!ProjectUtils.isProjectAlive(project)) {
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
            String fullPath = getFullPath(currentPsiFileItem);
            Set<String> facets = FacetsFinderService.getInstance(project).getFacets();
            Double additionalUIScale = SettingsService.getIDEInstance().getAdditionalUIScale();
            SettingsService service = getSettingsService(project);
            Object parentModelIdWhoseCheckFailed = null;
            for (final Model model : getModelsIncludingUserModels(project)) {
                if (model.getModelType() == currentModelType && model.isEnabled() && !service.getDisabledModelIds().contains(model.getId())) {
                    if (model.getParentId() != null && parentModelIdWhoseCheckFailed == model.getParentId()) {
                        // check already returned false for this model (parent or alt), don't need to check again
                        checks_saved++;
                        continue;
                    }
                    checks_done++;
                    if (model.check(parentName, currentFileName, fullPath, facets, project)) {
                        return IconUtils.getIcon(model, additionalUIScale);
                    } else {
                        parentModelIdWhoseCheckFailed = model.getParentId() == null ? model.getId() : model.getParentId();
                    }
                }
            }
        } catch (Throwable e) {
            logError(e);
        } finally {
            if (LOGGER.isDebugEnabled()) {
                // activate with Help > Diagnostic Tools > Debug Log Settings > #lermitage.intellij.extra.icons.BaseIconProvider
                LOGGER.debug(className + " did " + checks_done + " model checks and saved " + checks_saved + " model checks");
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
            } else {
                customModelsStream = projectService.getCustomModels().stream();
            }
        } else {
            customModelsStream = SettingsIDEService.getInstance().getCustomModels().stream();
        }

        return Stream.concat(customModelsStream, models.stream()).collect(Collectors.toList());//
    }

    @Nullable
    private String getFullPath(@NotNull PsiFileSystemItem file) {
        if (file.getVirtualFile() != null) {
            return file.getVirtualFile().getPath().toLowerCase();
        }
        return null;
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
        VirtualFile contentRoot = ProjectFileIndex.getInstance(project).getContentRootForFile(file);
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
