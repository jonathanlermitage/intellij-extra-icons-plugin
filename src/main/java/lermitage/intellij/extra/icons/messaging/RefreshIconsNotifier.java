// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.messaging;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

/**
 * This notifier is used to refresh icons for projects.
 * See <a href="https://plugins.jetbrains.com/docs/intellij/messaging-infrastructure.html">Messaging Infrastructure documentation</a>.
 */
public interface RefreshIconsNotifier {

    /** The topic used for notifying the icons refresh handled by Extra Icons plugin. */
    @Topic.ProjectLevel
    Topic<RefreshIconsNotifier> EXTRA_ICONS_REFRESH_ICONS_NOTIFIER_TOPIC =
        Topic.create("extra-icons refresh icons", RefreshIconsNotifier.class);

    /**
     * Refresh the icons for the specified project.
     * @param project the project whose icons need to be refreshed.
     */
    void refreshProjectIcons(@NotNull Project project);

    /** Refresh the icons for all projects. */
    void refreshAllProjectsIcons();
}
