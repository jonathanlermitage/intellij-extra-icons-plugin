// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public interface IconEnabler {

    boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify);
}
