// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.activity;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import lermitage.intellij.extra.icons.messaging.RefreshIconsNotifierService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Re-init icon enablers on project init, once indexing tasks are done. May fix init issues when querying IDE filename index while indexing.
 * At least, it fixes icons reloading (Enablers) after long indexing tasks (example: after you asked to invalidate caches).
 */
public class EnablerServicesProjectActivity implements ProjectActivity {

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        RefreshIconsNotifierService.getInstance().triggerProjectIconEnablersReinit(project);
        return null;
    }
}
