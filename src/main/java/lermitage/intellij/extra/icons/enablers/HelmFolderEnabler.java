// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HelmFolderEnabler extends AbstractFolderEnabler implements IconEnabler {

    // one icon enabler per project
    private static final Map<Project, IconEnabler> enablersCache = new ConcurrentHashMap<>();

    public static IconEnabler getInstance(@NotNull Project project) {
        if (enablersCache.containsKey(project)) {
            return enablersCache.get(project);
        }
        IconEnabler iconEnabler = new HelmFolderEnabler();
        enablersCache.put(project, iconEnabler);
        return iconEnabler;
    }

    @NotNull
    @Override
    protected String[] getFilenamesToSearch() {
        return new String[]{"Chart.yaml", "values.yaml"};
    }
}
