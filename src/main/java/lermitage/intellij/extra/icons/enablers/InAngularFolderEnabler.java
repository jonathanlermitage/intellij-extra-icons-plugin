// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InAngularFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(InAngularFolderEnabler.class);

    /**
     * Initialization ({@link #init(Project)}) is done once at project opening to avoid
     * searching angular.json files again for every folder in the project.
     */
    private boolean initialized = false;
    private Set<String> angularFolders;

    // one icon enabler per project
    private static final Map<Project, IconEnabler> enablersCache = new ConcurrentHashMap<>();

    public static IconEnabler getInstance(@NotNull Project project) {
        if (enablersCache.containsKey(project)) {
            return enablersCache.get(project);
        }
        IconEnabler iconEnabler = new InAngularFolderEnabler();
        enablersCache.put(project, iconEnabler);
        return iconEnabler;
    }

    private synchronized void init(@NotNull Project project) {
        long t1 = System.currentTimeMillis();
        Collection<VirtualFile> virtualFilesByName = FilenameIndex
            .getVirtualFilesByName("angular.json", true, GlobalSearchScope.projectScope(project));
        angularFolders = virtualFilesByName.stream()
            .map(virtualFile -> normalizePath(virtualFile.getPath()).replace("/angular.json", "/"))
            .collect(Collectors.toSet());

        initialized = true;
        long execDuration = System.currentTimeMillis() - t1;
        LOGGER.info("Searched for angular.json files in project " + project.getName() + " in " + execDuration + " ms." +
            " Found angular folders: " + angularFolders);
    }

    /** Should (Re)Init if initialization never occurred. */
    private boolean shouldInit() {
        return !initialized;
    }

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        if (shouldInit()) {
            init(project);
        }
        String normalizedPathToVerify = normalizePath(absolutePathToVerify);
        for (String angularFolder : angularFolders) {
            if (normalizedPathToVerify.startsWith(angularFolder)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean terminatesConditionEvaluation() {
        return false;
    }

    private static String normalizePath(@NotNull String path) {
        return path.toLowerCase().replaceAll("\\\\", "/");
    }
}
