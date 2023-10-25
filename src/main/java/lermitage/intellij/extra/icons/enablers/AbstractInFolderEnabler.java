// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ui.EDT;
import lermitage.intellij.extra.icons.cfg.services.SettingsIDEService;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("HardCodedStringLiteral")
public abstract class AbstractInFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(AbstractInFolderEnabler.class);

    /** Parent folder(s) where files or folders should be located in order to activate Enabler. */
    protected Set<String> enabledFolders = Collections.emptySet();

    protected abstract String[] getFilenamesToSearch();

    /** The name of this icon enabler. Used to identify disabled icon enabler if an error occurred. */
    public abstract String getName();

    /** A boolean flag used to obtain a match if any of the specified files exists in the project. */
    public boolean getRequiredSearchedFiles() {
        return true;
    }

    @Override
    public synchronized void init(@NotNull Project project) {
        try {
            if (SettingsIDEService.getInstance().getUseIDEFilenameIndex2()) {
                enabledFolders = initWithIDEFileIndex(project, getFilenamesToSearch());
            } else {
                enabledFolders = initWithRegularFS(project, getFilenamesToSearch());
            }
        } catch (Throwable e) {
            LOGGER.warn("Canceled init of " + getName() + " Enabler", e);
        }
    }

    /**
     * Look for given files in modules base path and level-1 sub-folders (by querying
     * regular FS, not IDE filename index), then return folders containing at least one
     * of these files.
     */
    private Set<String> initWithRegularFS(@NotNull Project project, String[] filenamesToSearch) {
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        Set<String> modulePaths = new HashSet<>();
        Stream.of(moduleManager.getModules()).forEach(module -> {
            VirtualFile moduleVirtualFile = ProjectUtil.guessModuleDir(module);
            if (moduleVirtualFile != null) {
                modulePaths.add(moduleVirtualFile.getPath());
            }
        });
        Set<String> foldersToEnable = new HashSet<>();
        modulePaths.forEach(modulePath -> {

            // look in modules root
            for (String filenameToSearch : filenamesToSearch) {
                try {
                    if (new File(modulePath, filenameToSearch).exists()) {
                        foldersToEnable.add(normalizePath(modulePath + "/"));
                    }
                } catch (Exception e) {
                    LOGGER.warn("Failed to check '" + modulePath + "/" + filenameToSearch + "' existence", e);
                }
            }

            // look in modules level-1 sub-folders
            File[] moduleSubFiles = new File(modulePath).listFiles();
            if (moduleSubFiles != null) {
                Stream.of(moduleSubFiles).filter(File::isDirectory).forEach(dir -> {
                    for (String filenameToSearch : filenamesToSearch) {
                        try {
                            if (new File(dir, filenameToSearch).exists()) {
                                foldersToEnable.add(normalizePath(dir.getAbsolutePath() + "/"));
                            }
                        } catch (Exception e) {
                            LOGGER.warn("Failed to check '" + modulePath + "/" + filenameToSearch + "' existence", e);
                        }
                    }
                });
            }
        });

        return foldersToEnable;
    }

    /**
     * Look for given files in project (by querying IDE filename index), then return
     * folders containing at least one of these files.
     */
    private Set<String> initWithIDEFileIndex(@NotNull Project project, String[] filenamesToSearch) {
        if (EDT.isCurrentThreadEdt()) { // we can no longer read index in EDT. See com.intellij.util.SlowOperations documentation
            LOGGER.warn(getName() + " Enabler's init has been called while in EDT thread. " +
                "Will try again later. Some icons override may not work.");
            return Collections.emptySet();
        }
        if (!project.isInitialized()) {
            LOGGER.warn(getName() + " Enabler can't query IDE filename index: project " + project.getName() + " is not initialized. " +
                "Will try again later. Some icons override may not work.");
            return Collections.emptySet();
        }

        final boolean allRequired = getRequiredSearchedFiles();
        Collection<VirtualFile> virtualFilesByName = null;
        String matchedFile = null;

        for (String filename : filenamesToSearch) {
            try {
                virtualFilesByName = FilenameIndex.getVirtualFilesByName(filename, true, GlobalSearchScope.projectScope(project));
                if (!virtualFilesByName.isEmpty()) {
                    matchedFile = filename;
                    break;
                }
            } catch (Exception e) {
                LOGGER.warn(getName() + " Enabler failed to query IDE filename index. " +
                    "Will try again later. Some icons override may not work.", e);
                if (allRequired) {
                    return Collections.emptySet();
                }
            }
        }

        final String[] additionalFilenamesToSearch = filenamesToSearch.length > 1 && allRequired ?
            Arrays.copyOfRange(filenamesToSearch, 1, filenamesToSearch.length) :
            new String[0];

        final String matchedFilename = matchedFile;

        return virtualFilesByName.stream()
            .map(virtualFile ->
                normalizePath(virtualFile.getPath())
                    .replace(normalizePath("/" + matchedFilename), "/"))
            .filter(folder -> {
                for (String additionalFilenameToSearch : additionalFilenamesToSearch) {
                    try {
                        if (!new File(folder, additionalFilenameToSearch).exists()) {
                            return false;
                        }
                    } catch (Exception e) {
                        LOGGER.warn(getName() + " Enabler failed to check " + folder + "/" + additionalFilenameToSearch + " existence", e);
                    }
                }
                return true;
            })
            .collect(Collectors.toSet());
    }

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        String normalizedPathToVerify = normalizePath(absolutePathToVerify);
        for (String folder : enabledFolders) {
            if (normalizedPathToVerify.startsWith(folder)) {
                return true;
            }
        }
        return false;
    }

    protected String normalizePath(@NotNull String path) {
        return path.toLowerCase()
            .replaceAll("\\\\", "/")
            .replaceAll("//", "/");
    }

    @Override
    public boolean terminatesConditionEvaluation() {
        return false;
    }
}
