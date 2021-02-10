// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IJUtils {

    private static final Logger LOGGER = Logger.getInstance(IJUtils.class);

    /**
     * Refresh project view.
     */
    public static void refresh(Project project) {
        if (IJUtils.isAlive(project)) {
            ProjectView view = ProjectView.getInstance(project);
            if (view != null) {
                view.refresh();
                AbstractProjectViewPane currentProjectViewPane = view.getCurrentProjectViewPane();
                if (currentProjectViewPane != null) {
                    currentProjectViewPane.updateFromRoot(true);
                }
            }
        }
    }

    /**
     * Refresh all opened project views.
     */
    public static void refreshOpenedProjects() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            refresh(project);
        }
    }

    /**
     * Get project's facets (as in {@code Project Structure > Projects Settings > Facets}),
     * like "Spring", "JPA", etc.
     */
    public static @NotNull Set<String> getFacets(Project project) {
        if (project == null) {
            return Collections.emptySet();
        }
        long t1 = System.currentTimeMillis();
        Set<String> facets = new HashSet<>();
        try {
            ModuleManager moduleManager = ModuleManager.getInstance(project);
            Stream.of(moduleManager.getModules()).forEach(module -> {
                FacetManager facetManager = FacetManager.getInstance(module);
                @NotNull Facet<?>[] allFacets = facetManager.getAllFacets();
                Set<String> facetNames = Stream.of(allFacets).map(Facet::getName).collect(Collectors.toSet());
                facets.addAll(facetNames);
            });
        } catch (Exception e) {
            LOGGER.error(e);
        }
        long execTime = System.currentTimeMillis() - t1;
        if (execTime > 50) { // should be instant
            LOGGER.warn("Found facets " + facets + " for project " + project + " in " + execTime + "ms (it should be instant)");
        }
        return facets;
    }

    /**
     * Return true if the project can be manipulated. Project is not null, not disposed, etc.
     * Developed to fix <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/39">issue #39</a>.
     */
    public static boolean isAlive(@Nullable Project project) {
        return project != null && !project.isDisposed();
    }
}
