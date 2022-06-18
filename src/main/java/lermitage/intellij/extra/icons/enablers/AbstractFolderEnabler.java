// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFolderEnabler extends AbstractInFolderEnabler implements IconEnabler {

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        if (shouldInit()) {
            init(project);
        }
        String normalizedPathToVerify = normalizePath(absolutePathToVerify);
        if (!normalizedPathToVerify.endsWith("/")) {
            normalizedPathToVerify += "/";
        }
        for (String helmFolder : folders) {
            if (normalizedPathToVerify.equals(helmFolder)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean terminatesConditionEvaluation() {
        return true;
    }
}
