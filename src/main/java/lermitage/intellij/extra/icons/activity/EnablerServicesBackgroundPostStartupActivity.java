// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.activity;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import lermitage.intellij.extra.icons.enablers.EnablerUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Re-init icon enablers on project init, once indexing tasks are done. May fix init issues when querying IDE filename index while indexing.
 */
public class EnablerServicesBackgroundPostStartupActivity implements ProjectActivity {

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        DumbService.getInstance(project).runWhenSmart(() -> {
            ApplicationManager.getApplication().runReadAction(() -> EnablerUtils.forceInitAllEnablers(project));
        });
        return null;
    }
}
