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
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractInFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(AbstractInFolderEnabler.class);
    private final String className = this.getClass().getSimpleName();

    private boolean initialized = false;
    protected Set<String> folders;

    protected abstract String[] getFilenamesToSearch();

    protected synchronized void init(@NotNull Project project) {
        long t1 = System.currentTimeMillis();
        String[] filenamesToSearch = getFilenamesToSearch();
        // TODO migrate to getVirtualFilesByName("angular.json", true, GlobalSearchScope.projectScope(project))
        //  in 2023 and set minimal IDE version to 2022.1 (221)
        @SuppressWarnings("deprecation") Collection<VirtualFile> virtualFilesByName = FilenameIndex.getVirtualFilesByName(
            project,
            getFilenamesToSearch()[0],
            true,
            GlobalSearchScope.projectScope(project));
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

        initialized = true;
        long execDuration = System.currentTimeMillis() - t1;
        String logMsg = className + " searched for " + Arrays.toString(getFilenamesToSearch()) + " files in project " + project.getName() + " in " + execDuration + " ms." + " Found folders: " + folders;
        if (execDuration > 4000) {
            LOGGER.warn(logMsg + ". Operation should complete faster. " + ProjectUtils.PLEASE_OPEN_ISSUE_MSG);
        } else {
            LOGGER.info(logMsg);
        }
    }

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        if (shouldInit()) {
            init(project);
        }
        String normalizedPathToVerify = normalizePath(absolutePathToVerify);
        for (String helmFolder : folders) {
            if (normalizedPathToVerify.startsWith(helmFolder)) {
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
