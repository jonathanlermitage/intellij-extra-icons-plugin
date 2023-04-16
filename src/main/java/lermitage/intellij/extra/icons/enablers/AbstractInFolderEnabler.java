// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import lermitage.intellij.extra.icons.utils.LogUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractInFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(AbstractInFolderEnabler.class);

    private final String className = this.getClass().getSimpleName();

    /** Indicates if the Enabler has been initialized, even with error. */
    private boolean initialized = false;

    /** Parent folder(s) where files or folders should be located in order to activate Enabler. */
    protected Set<String> folders = Collections.emptySet();

    protected abstract String[] getFilenamesToSearch();

    /** The name of this icon enabler. Used to identify disabled icon enabler if an error occurred. */
    public abstract String getName();

    /** A boolean flag used to obtain a match if any of the specified files exists in the project. */
    public boolean getRequiredSearchedFiles() {
        return true;
    }

    @Override
    public synchronized void init(@NotNull Project project) { // TODO this is a 'slow operation' in EDT, run in background or schedule a startup task
        initialized = true;
        DumbService.getInstance(project).runWhenSmart(() -> { // run when smart = run once indexing tasks completed
            try {
                long t1 = System.currentTimeMillis();
                initWithIdeFileIndex(project, getFilenamesToSearch());
                long execDuration = System.currentTimeMillis() - t1;
                String logMsg = getName() + " Enabler searched for " + Arrays.toString(getFilenamesToSearch()) + " files in project " + project.getName() + " in " + execDuration + " ms." + " Found folders: " + folders;
                if (execDuration > 4000) {
                    LOGGER.warn(logMsg + ". Operation should complete faster. " + ProjectUtils.PLEASE_OPEN_ISSUE_MSG);
                } else {
                    LOGGER.info(logMsg);
                }
                LOGGER.info(getName() + " Enabler init done. Refreshing project " + project.getName());
                ProjectUtils.refreshProject(project);
            } catch (Throwable e) {
                LogUtils.showErrorIfAllowedByUser(LOGGER, "Canceled init of" + getName() + " Enabler", e);
            }
        });
    }

    private void initWithIdeFileIndex(@NotNull Project project, String[] filenamesToSearch) throws Throwable {
        if (!project.isInitialized()) {
            String msg = getName() + " Enabler can't query IDE filename index: project " + project.getName() + " is not initialized. " +
                "Some icons override won't work.";
            throw new Exception(msg);
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
                initialized = true;
                LogUtils.throwErrorIfAllowedByUser(LOGGER,
                    getName() + " Enabler failed to query IDE filename index. Some icons override won't work.",
                    e);
                if (allRequired) {
                    return;
                }
            }
        }

        final String[] additionalFilenamesToSearch = filenamesToSearch.length > 1 && allRequired ?
            Arrays.copyOfRange(filenamesToSearch, 1, filenamesToSearch.length) :
            new String[0];

        final String matchedFilename = matchedFile;

        folders = virtualFilesByName.stream()
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
                        LOGGER.warn(className + " failed to check " + folder + "/" + additionalFilenameToSearch + " existence", e);
                    }
                }
                return true;
            })
            .collect(Collectors.toSet());
    }

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        if (shouldInit()) {
            try {
                init(project);
            } catch (Exception e) {
                LOGGER.warn(e);
            }
        }

        String normalizedPathToVerify = normalizePath(absolutePathToVerify);

        for (String folder : folders) {
            if (normalizedPathToVerify.startsWith(folder)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Should (Re)Init if initialization never occurred.
     */
    protected boolean shouldInit() {
        return !initialized;
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
