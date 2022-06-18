// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractInFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(AbstractInFolderEnabler.class);

    private boolean initialized = false;
    protected Set<String> folders;

    protected abstract String getFilenameToSearch();

    protected synchronized void init(@NotNull Project project) {
        long t1 = System.currentTimeMillis();
        // TODO migrate to getVirtualFilesByName("angular.json", true, GlobalSearchScope.projectScope(project))
        //  in 2023 and set minimal IDE version to 2022.1 (221)
        @SuppressWarnings("deprecation") Collection<VirtualFile> virtualFilesByName = FilenameIndex.getVirtualFilesByName(project, getFilenameToSearch(), true, GlobalSearchScope.projectScope(project));
        folders = virtualFilesByName.stream().map(virtualFile -> normalizePath(virtualFile.getPath()).replace(normalizePath("/" + getFilenameToSearch()), "/")).collect(Collectors.toSet());

        initialized = true;
        long execDuration = System.currentTimeMillis() - t1;
        String logMsg = "Searched for angular.json files in project " + project.getName() + " in " + execDuration + " ms." + " Found angular folders: " + folders;
        if (execDuration > 200) {
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
        return path.toLowerCase().replaceAll("\\\\", "/");
    }

    @Override
    public boolean terminatesConditionEvaluation() {
        return false;
    }
}
