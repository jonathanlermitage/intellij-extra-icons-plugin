// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
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

    private boolean initialized = false;

    /** Parent folder(s) where files or folders should be located in order to activate Enabler. */
    protected Set<String> folders = Collections.emptySet();

    protected abstract String[] getFilenamesToSearch();

    /** The name of this icon enabler. Used to identify disabled icon enabler if an error occurred. */
    public abstract String getName();

    @Override
    public synchronized void init(@NotNull Project project, boolean silentErrors) throws Exception {
        long t1 = System.currentTimeMillis();
        initWithIdeFileIndex(project, getFilenamesToSearch(), silentErrors);
        initialized = true;
        long execDuration = System.currentTimeMillis() - t1;
        String logMsg = getName() + " Enabler searched for " + Arrays.toString(getFilenamesToSearch()) + " files in project " + project.getName() + " in " + execDuration + " ms." + " Found folders: " + folders;
        if (execDuration > 4000) {
            LOGGER.warn(logMsg + ". Operation should complete faster. " + ProjectUtils.PLEASE_OPEN_ISSUE_MSG);
        } else {
            LOGGER.info(logMsg);
        }
    }

    private void initWithIdeFileIndex(@NotNull Project project, String[] filenamesToSearch, boolean silentErrors) throws Exception {
        if (!project.isInitialized()) {
            String msg = getName() + " Enabler can't query IDE filename index: project " + project.getName() + " is not initialized. " +
                "Some icons override won't work for now. Meanwhile, an Index Listener will try to fix that automatically once indexing " +
                "tasks are done.";
            if (silentErrors) {
                LOGGER.info(msg);
            } else {
                throw new Exception(msg);
            }
        }
        Collection<VirtualFile> virtualFilesByName;
        try {
            // TODO migrate to getVirtualFilesByName(getFilenamesToSearch()[0], true, GlobalSearchScope.projectScope(project))
            //  in 2023 and set minimal IDE version to 2022.1 (221)
            virtualFilesByName = FilenameIndex.getVirtualFilesByName(
                project,
                getFilenamesToSearch()[0],
                true,
                GlobalSearchScope.projectScope(project));
        } catch (Exception e) {
            initialized = true;
            if (silentErrors) {
                LOGGER.info(getName() + " Enabler failed to query IDE filename index. Some icons override won't work " +
                    "for now. Meanwhile, an Index Listener will try to fix that automatically once indexing tasks are done.", e);
            } else {
                throw (e);
            }
            return;
        }

        final String[] additionalFilenamesToSearch = filenamesToSearch.length > 1 ?
            Arrays.copyOfRange(filenamesToSearch, 1, filenamesToSearch.length) :
            new String[0];
        folders = virtualFilesByName.stream()
            .map(virtualFile ->
                normalizePath(virtualFile.getPath())
                    .replace(normalizePath("/" + getFilenamesToSearch()[0]), "/"))
            .filter(folder -> {
                if (additionalFilenamesToSearch.length > 0) {
                    for (String additionalFilenameToSearch : additionalFilenamesToSearch) {
                        try {
                            if (!new File(folder, additionalFilenameToSearch).exists()) {
                                return false;
                            }
                        } catch (Exception e) {
                            LOGGER.warn(className + " failed to check " + folder + "/" + additionalFilenameToSearch + " existence", e);
                        }
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
                init(project, true);
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

    /** Should (Re)Init if initialization never occurred. */
    protected boolean shouldInit() {
        return !initialized;
    }

    protected static String normalizePath(@NotNull String path) {
        return path.toLowerCase()
            .replaceAll("\\\\", "/")
            .replaceAll("//", "/");
    }

    @Override
    public boolean terminatesConditionEvaluation() {
        return false;
    }
}
