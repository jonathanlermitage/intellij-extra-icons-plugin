// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFolderEnabler extends AbstractInFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(AbstractFolderEnabler.class);

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        if (shouldInit()) {
            try {
                init(project);
            } catch (Exception e) {
                LOGGER.warn(e);
            }
        }
        String normalizedPathToVerify = normalizePath(absolutePathToVerify);
        if (!normalizedPathToVerify.endsWith("/")) {
            normalizedPathToVerify += "/";
        }
        for (String folder : folders) {
            if (normalizedPathToVerify.equals(folder)) {
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
