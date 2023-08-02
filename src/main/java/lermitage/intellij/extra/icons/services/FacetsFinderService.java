// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.services;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service(Service.Level.PROJECT)
public final class FacetsFinderService {

    private static final @NonNls Logger LOGGER = Logger.getInstance(FacetsFinderService.class);

    private final Set<String> facets;

    public FacetsFinderService(@NotNull Project project) {
        long t1 = System.currentTimeMillis();
        Set<String> facetsFound = new HashSet<>();
        try {
            ModuleManager moduleManager = ModuleManager.getInstance(project);
            Stream.of(moduleManager.getModules()).forEach(module -> {
                FacetManager facetManager = FacetManager.getInstance(module);
                @NotNull Facet<?>[] allFacets = facetManager.getAllFacets();
                Set<String> facetNames = Stream.of(allFacets).map(facet -> facet.getName().toLowerCase()).collect(Collectors.toSet());
                facetsFound.addAll(facetNames);
            });
        } catch (Exception e) {
            LOGGER.error(e);
        }
        long execTime = System.currentTimeMillis() - t1;
        if (execTime > 50) { // should be instant
            LOGGER.warn("Found facets " + facetsFound + " for project " + project + " in " + execTime + "ms (should be instant)");
        }
        facets = facetsFound;
    }

    public static FacetsFinderService getInstance(@NotNull Project project) {
        return project.getService(FacetsFinderService.class);
    }

    /**
     * Get project's facets (as in {@code Project Structure > Projects Settings > Facets}),
     * like "Spring", "JPA", etc. <b>Facet names are in lowercase</b>.
     */
    public @NotNull Set<String> getFacets() {
        return facets;
    }
}
