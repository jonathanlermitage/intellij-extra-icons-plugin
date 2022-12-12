// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers.services;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import lermitage.intellij.extra.icons.enablers.AbstractInFolderEnabler;
import lermitage.intellij.extra.icons.enablers.IconEnabler;
import org.jetbrains.annotations.NotNull;

@Service
public final class InGraphQLFolderEnablerService extends AbstractInFolderEnabler implements IconEnabler {

    public InGraphQLFolderEnablerService(@NotNull Project project) {
        init(project);
    }

    public static InGraphQLFolderEnablerService getInstance(@NotNull Project project) {
        return project.getService(InGraphQLFolderEnablerService.class);
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
