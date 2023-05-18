// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ui.EDT;
import lermitage.intellij.extra.icons.utils.LogUtils;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("HardCodedStringLiteral")
public abstract class AbstractInFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(AbstractInFolderEnabler.class);

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
    public synchronized void init(@NotNull Project project) {
        try {
            initWithIdeFileIndex(project, getFilenamesToSearch());
            ProjectUtils.refreshProject(project);
        } catch (Throwable e) {
            LOGGER.warn("Canceled init of " + getName() + " Enabler", e);
        }
    }

    private void initWithIdeFileIndex(@NotNull Project project, String[] filenamesToSearch) throws Throwable {
        if (EDT.isCurrentThreadEdt()) { // we can no longer read index in EDT. See com.intellij.util.SlowOperations documentation
            LOGGER.warn(getName() + " Enabler's init has been called while in EDT thread. Some icons override won't work.");
            return;
        }
        if (!project.isInitialized()) {
            LOGGER.warn(getName() + " Enabler can't query IDE filename index: project " + project.getName() + " is not initialized. " +
                "Some icons override won't work.");
            return;
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
        for (String folder : folders) {
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
