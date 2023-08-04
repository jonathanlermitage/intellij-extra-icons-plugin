// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.Consumer;
import com.intellij.util.ModalityUiUtil;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Component;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Error reporter which prefills an issue on Extra Icons' GitHub repository.
 * Highly inspired from <b>git-machete-intellij-plugin</b>'s code.
 */
public class ExtraIconsErrorReportSubmitter extends ErrorReportSubmitter {

    private static final @NonNls Logger LOGGER = Logger.getInstance(IconUtils.class);
    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    private static final int MAX_GITHUB_URI_LENGTH = 8192;

    @Override
    public @NlsActions.ActionText @NotNull String getReportActionText() {
        return i18n.getString("error.report.btn.title");
    }

    @Override
    public boolean submit(IdeaLoggingEvent @NotNull [] events,
                          @Nullable String additionalInfo,
                          @NotNull Component parentComponent,
                          @NotNull Consumer<? super SubmittedReportInfo> consumer) { // TODO replace com.intellij.util.Consumer by java.util.function.Consumer once IJ API has been updated. Can do nothing for now, and fix will break compatibility with previous IDEs :-(
        try {
            URI uri = constructNewGitHubIssueUri(events, additionalInfo);
            ModalityUiUtil.invokeLaterIfNeeded(ModalityState.any(), () -> BrowserUtil.browse(uri));
        } catch (Exception e) {
            LOGGER.error("Failed to prepare Extra Icons error reporter", e);
            return false;
        }
        return true;
    }

    @SuppressWarnings("HardCodedStringLiteral")
    URI constructNewGitHubIssueUri(IdeaLoggingEvent[] events, @Nullable String additionalInfo) throws URISyntaxException {
        URIBuilder uriBuilder;
        uriBuilder = new URIBuilder("https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/new");
        String title = Stream.of(events)
            .map(event -> {
                Throwable throwable = event.getThrowable();
                String exceptionMessage = event.getThrowableText().lines().findFirst().orElse("");
                return (throwable != null ? exceptionMessage : event.getMessage()).stripTrailing();
            })
            .collect(Collectors.joining("; "));
        uriBuilder.setParameter("title", title);
        uriBuilder.setParameter("labels", "bug");

        URI uri;
        List<String> reportBodyLines = getReportBody(events, additionalInfo).lines().collect(Collectors.toList());
        do {
            // Let's cut the body gradually line-by-line until the resulting URI fits into the GitHub limits.
            // It's hard to predict the perfect exact cut in advance due to URL encoding.
            reportBodyLines = reportBodyLines.subList(0, reportBodyLines.size() - 1);
            uriBuilder.setParameter("body", reportBodyLines.stream().collect(Collectors.joining(System.lineSeparator())));
            uri = uriBuilder.build();
        } while (uri.toString().length() > MAX_GITHUB_URI_LENGTH);
        return uri;
    }

    private String getReportBody(
        IdeaLoggingEvent[] events,
        @Nullable String additionalInfo) {
        String reportBody = getBugTemplate();
        for (java.util.Map.Entry<String, String> entry : getTemplateVariables(events, additionalInfo).entrySet()) {
            reportBody = reportBody.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return reportBody;
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private java.util.Map<String, String> getTemplateVariables(
        IdeaLoggingEvent[] events,
        @Nullable String additionalInfo) {
        Map<String, String> templateVariables = new HashMap<>();

        templateVariables.put("ide", ApplicationInfo.getInstance().getFullApplicationName());

        IdeaPluginDescriptor pluginDescriptor = PluginManagerCore.getPlugin(PluginId.getId("lermitage.intellij.extra.icons"));
        String pluginVersion = pluginDescriptor == null ? "<unknown>" : pluginDescriptor.getVersion();
        templateVariables.put("pluginVersion", pluginVersion);

        templateVariables.put("os", SystemInfo.getOsNameAndVersion()); //ApplicationInfo.getInstance().

        templateVariables.put("additionalInfo", additionalInfo == null ? "N/A" : additionalInfo);

        String nl = System.lineSeparator();
        String stacktraces = Stream.of(events)
            .map(event -> {
                // This message is distinct from the throwable's message:
                // in `LOG.error(message, throwable)`, it's the first parameter.
                String messagePart = event.getMessage() != null ? (event.getMessage() + nl + nl) : "";
                String throwablePart = shortenExceptionsStack(event.getThrowableText().stripTrailing());
                return nl + messagePart + throwablePart + nl;
            })
            .collect(Collectors.joining(nl + nl));
        templateVariables.put("stacktraces", stacktraces);

        return templateVariables;
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private String shortenExceptionsStack(String stackTrace) {
        String nl = System.lineSeparator();
        int rootCauseIndex = Math.max(
            stackTrace.lastIndexOf("Caused by:"),
            stackTrace.lastIndexOf("\tSuppressed:"));

        if (rootCauseIndex != -1) {
            String rootCauseStackTrace = stackTrace.substring(rootCauseIndex);
            String[] lines = stackTrace.substring(0, rootCauseIndex).split(nl);

            StringBuilder resultString = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains("Caused by:") || lines[i].contains("Suppressed:") || i == 0) {
                    resultString.append(lines[i]).append(nl);
                    if (i + 1 < lines.length) {
                        resultString.append(lines[i + 1]).append("...").append(nl);
                    }
                }
            }
            return resultString.append(rootCauseStackTrace).toString();
        }
        return stackTrace;
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private String getBugTemplate() {
        return """
            ## Running environment
            - Extra Icons plugin version - %pluginVersion%
            - IDE - %ide%
            - OS - %os%

            ## Bug description
            Please include steps to reproduce (like `go to...`/`click on...` etc.) + expected and actual behaviour. \s
            Please attach **IDE logs**. Open your IDE and go to <kbd>Help</kbd>, <kbd>Show Log in Explorer</kbd>, then pick `idea.log`.

            ## IDE - additional info
            %additionalInfo%

            ## IDE - stack trace
            ```%stacktraces%
            """;
    }
}
