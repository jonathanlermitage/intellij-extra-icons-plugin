// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InGraphQLFolderEnabler extends AbstractInFolderEnabler implements IconEnabler {

    // one icon enabler per project
    private static final Map<Project, IconEnabler> enablersCache = new ConcurrentHashMap<>();

    public static IconEnabler getInstance(@NotNull Project project) {
        if (enablersCache.containsKey(project)) {
            return enablersCache.get(project);
        }
        IconEnabler iconEnabler = new InGraphQLFolderEnabler();
        enablersCache.put(project, iconEnabler);
        return iconEnabler;
    }

    @NotNull
    @Override
    protected String[] getFilenamesToSearch() {
        return new String[]{"schema.graphql", "schema.gql", "codegen.yml", ".graphqlconfig", "schema.graphql.json"};
    }

    @Override
    public String getName() {
        return "GraphQL icons";
    }

    @Override
    public boolean getRequiredSearchedFiles() {
        return false;
    }

}
