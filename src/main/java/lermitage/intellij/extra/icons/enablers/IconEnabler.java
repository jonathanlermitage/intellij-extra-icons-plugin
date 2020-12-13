// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.openapi.project.Project;

public interface IconEnabler {

    void init(Project project);

    boolean verify(Project project, String absolutePathToVerify);
}
