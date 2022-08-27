// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.enablers;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import lermitage.intellij.extra.icons.Globals;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import lermitage.intellij.extra.icons.utils.ProjectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractInFolderEnabler implements IconEnabler {

    private static final Logger LOGGER = Logger.getInstance(AbstractInFolderEnabler.class);
    private final String className = this.getClass().getSimpleName();

    private boolean initialized = false;
    private boolean indexErrorReported = false;
    protected Set<String> folders = Collections.emptySet();

    protected abstract String[] getFilenamesToSearch();

    /** The name of this icon enabler. Used to identify disabled icon enabler if an error occurred. */
    public abstract String getName();

    protected synchronized void init(@NotNull Project project) {
        long t1 = System.currentTimeMillis();
        String[] filenamesToSearch = getFilenamesToSearch();
        Collection<VirtualFile> virtualFilesByName = null;
        try {
            final int MAX_ATTEMPTS = 3;
            //noinspection ConstantConditions
            for (int i = 1; i <= MAX_ATTEMPTS; i++) {
                try {
                    // TODO migrate to getVirtualFilesByName(getFilenamesToSearch()[0], true, GlobalSearchScope.projectScope(project))
                    //  in 2023 and set minimal IDE version to 2022.1 (221)
                    virtualFilesByName = FilenameIndex.getVirtualFilesByName(
                        project,
                        getFilenamesToSearch()[0],
                        true,
                        GlobalSearchScope.projectScope(project));
                    break;
                } catch (Exception e) {
                    if (i == MAX_ATTEMPTS) {
                        throw (e);
                    }
                    LOGGER.warn(getName() + " IconEnabler failed to query IDE filename index (attempt " + i + "/" + MAX_ATTEMPTS + "). " +
                        "Will try again in 20 ms. " + e.getMessage());
                    Thread.sleep(20);
                }
            }
        } catch (Exception e) {
            initialized = true;
            if (!indexErrorReported) {
                indexErrorReported = true;
                String msg = "Failed to query IDE filename index. <b>This feature won't work: " + getName() + "</b>. " +
                    "If this is the first time you see this message, and if you really need this feature, " +
                    "please restart your IDE. If it doesn't help, try to clear the file system cache and " +
                    "Local History (go to File, Invalidate Caches...).<br><b>It seems to be a JetBrains issue</b> (feel free to <b>upvote</b> " +
                    "https://youtrack.jetbrains.com/issue/IDEA-289822). Please do not open a new ticket for that.<br><hr>" +
                    "To disable this notification, please go to <b>File</b>, <b>Settings...</b>, <b>Extra Icons</b>, and check <b>Ignore plugin's warnings</b>.";
                LOGGER.warn(msg, e);
                if (!SettingsService.getIDEInstance().getIgnoreWarnings()) {
                    NotificationGroupManager.getInstance().getNotificationGroup(Globals.PLUGIN_GROUP_DISPLAY_ID)
                        .createNotification(msg, NotificationType.WARNING)
                        .setTitle(Globals.PLUGIN_NAME)
                        .setImportant(true)
                        .notify(null);
                }
            }
            return;
        }

        final String[] additionalFilenamesToSearch = filenamesToSearch.length > 1 ?
            Arrays.copyOfRange(filenamesToSearch, 1, filenamesToSearch.length) :
            new String[0];
        folders = virtualFilesByName.stream()
            .map(virtualFile ->
                normalizePath(virtualFile.getPath())
                    .replace(normalizePath("/" + getFilenamesToSearch()[0]), "/"))
            .filter(folder -> {
                if (additionalFilenamesToSearch.length > 0) {
                    for (String additionalFilenameToSearch : additionalFilenamesToSearch) {
                        try {
                            if (!new File(folder, additionalFilenameToSearch).exists()) {
                                return false;
                            }
                        } catch (Exception e) {
                            LOGGER.warn(className + " failed to check " + folder + "/" + additionalFilenameToSearch + " existence", e);
                        }
                    }
                }
                return true;
            })
            .collect(Collectors.toSet());

        initialized = true;
        long execDuration = System.currentTimeMillis() - t1;
        String logMsg = className + " searched for " + Arrays.toString(getFilenamesToSearch()) + " files in project " + project.getName() + " in " + execDuration + " ms." + " Found folders: " + folders;
        if (execDuration > 4000) {
            LOGGER.warn(logMsg + ". Operation should complete faster. " + ProjectUtils.PLEASE_OPEN_ISSUE_MSG);
        } else {
            LOGGER.info(logMsg);
        }
    }

    @Override
    public boolean verify(@NotNull Project project, @NotNull String absolutePathToVerify) {
        if (shouldInit()) {
            init(project);
        }
        String normalizedPathToVerify = normalizePath(absolutePathToVerify);
        for (String folder : folders) {
            if (normalizedPathToVerify.startsWith(folder)) {
                return true;
            }
        }
        return false;
    }

    /** Should (Re)Init if initialization never occurred. */
    protected boolean shouldInit() {
        return !initialized;
    }

    protected static String normalizePath(@NotNull String path) {
        return path.toLowerCase()
            .replaceAll("\\\\", "/")
            .replaceAll("//", "/");
    }

    @Override
    public boolean terminatesConditionEvaluation() {
        return false;
    }
}
