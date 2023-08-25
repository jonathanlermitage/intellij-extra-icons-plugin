// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import com.intellij.util.xmlb.annotations.Tag;
import lermitage.intellij.extra.icons.enablers.IconEnabler;
import lermitage.intellij.extra.icons.enablers.IconEnablerProvider;
import lermitage.intellij.extra.icons.enablers.IconEnablerType;
import lermitage.intellij.extra.icons.utils.I18nUtils;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Tag
public class ModelCondition {

    @OptionTag
    private boolean start = false;
    @OptionTag
    private boolean eq = false;
    @OptionTag
    private boolean mayEnd = false;
    @OptionTag
    private boolean end = false;
    @OptionTag
    private boolean noDot = false;
    @OptionTag
    private boolean checkParent = false;
    @OptionTag
    private boolean hasRegex = false;
    @OptionTag
    private boolean enabled = true;
    @OptionTag
    private boolean checkFacets = false;
    @OptionTag
    private boolean hasIconEnabler = false;

    @OptionTag
    private String[] names = new String[0];
    @OptionTag
    private Set<String> parentNames = Collections.emptySet();
    @OptionTag
    private String[] extensions = new String[0];
    @OptionTag
    private String regex;
    @OptionTag
    private String[] facets = new String[0];

    // transient fields are excluded from IconPack items

    private transient Pattern pattern; // transient because computed dynamically
    private transient IconEnablerType iconEnablerType; // transient because not exposed to user models

    private static final ResourceBundle i18n = I18nUtils.getResourceBundle();

    public void setParents(String... parents) {
        this.checkParent = true;
        this.parentNames = Stream.of(parents).collect(Collectors.toSet());
    }

    public void setStart(String... base) {
        this.start = true;
        this.names = base;
    }

    public void setEq(String... base) {
        this.eq = true;
        this.names = base;
    }

    public void setMayEnd(String... extensions) {
        this.mayEnd = true;
        this.extensions = extensions;
    }

    public void setEnd(String... extensions) {
        this.end = true;
        this.extensions = extensions;
    }

    public void setNoDot() {
        this.noDot = true;
    }

    public void setRegex(String regex) {
        this.hasRegex = true;
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    public void setFacets(String[] facets) {
        this.checkFacets = true;
        this.facets = facets;
    }

    public void setIconEnablerType(IconEnablerType iconEnablerType) {
        this.hasIconEnabler = true;
        this.iconEnablerType = iconEnablerType;
    }

    public boolean check(String parentName, String fileName, @Nullable String fullPath, Set<String> prjFacets, Project project) {
        if (!enabled) {
            return false;
        }

        if (hasIconEnabler && fullPath != null) {
            Optional<IconEnabler> iconEnabler = IconEnablerProvider.getIconEnabler(project, iconEnablerType);
            if (iconEnabler.isPresent()) {
                boolean iconEnabledVerified = iconEnabler.get().verify(project, fullPath);
                if (!iconEnabledVerified) {
                    return false;
                } else if (iconEnabler.get().terminatesConditionEvaluation()) {
                    return true;
                }
            }
        }

        // facet is a pre-condition, should always be associated with other conditions
        if (checkFacets && facets != null) {
            boolean facetChecked = false;
            for (String modelFacet : facets) {
                if (prjFacets.contains(modelFacet)) {
                    facetChecked = true;
                    break;
                }
            }
            if (!facetChecked) {
                return false;
            }
        }

        if (checkParent) {
            if (!(start || eq || end || mayEnd)) {
                if (parentNames.contains(parentName)) {
                    return true; // To style all files in a subdirectory
                }
            } else {
                if (!parentNames.contains(parentName)) {
                    return false;
                }
            }
        }

        if (hasRegex && fullPath != null) {
            if (pattern == null) {
                pattern = Pattern.compile(regex);
            }
            if (pattern.matcher(fullPath).matches()) {
                return true;
            }
        }

        if (eq) {
            if (end) {
                for (String n : names) {
                    for (String e : extensions) {
                        if (fileName.equals(n + e)) {
                            return true;
                        }
                    }
                }
            } else if (mayEnd) {
                for (String n : names) {
                    if (fileName.equals(n)) {
                        return true;
                    }
                    for (String e : extensions) {
                        if (fileName.equals(n + e)) {
                            return true;
                        }
                    }
                }
            } else {
                for (String n : names) {
                    if (fileName.equals(n)) {
                        return true;
                    }
                }
            }
        }

        if (start) {
            if (end) {
                for (String n : names) {
                    for (String e : extensions) {
                        if (fileName.startsWith(n) && fileName.endsWith(e)) {
                            return true;
                        }
                    }
                }
            } else if (mayEnd) {
                for (String n : names) {
                    if (fileName.startsWith(n)) {
                        return true;
                    }
                    for (String e : extensions) {
                        if (fileName.startsWith(n) && fileName.endsWith(e)) {
                            return true;
                        }
                    }
                }
            } else if (noDot) {
                for (String n : names) {
                    if (fileName.startsWith(n) && !fileName.contains(".")) {
                        return true;
                    }
                }
            } else {
                for (String n : names) {
                    if (fileName.startsWith(n)) {
                        return true;
                    }
                }
            }
        }

        if (end & !eq & !start) {
            for (String e : extensions) {
                if (fileName.endsWith(e)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasStart() {
        return start;
    }

    public boolean hasEq() {
        return eq;
    }

    public boolean hasMayEnd() {
        return mayEnd;
    }

    public boolean hasEnd() {
        return end;
    }

    public boolean hasNoDot() {
        return noDot;
    }

    public boolean hasCheckParent() {
        return checkParent;
    }

    public boolean hasRegex() {
        return hasRegex;
    }

    public boolean hasFacets() {
        return checkFacets;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isValid() {
        return hasRegex || checkParent || start || eq || end || mayEnd;
    }

    public String[] getNames() {
        return names;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public Set<String> getParents() {
        return parentNames;
    }

    public String getRegex() {
        return regex;
    }

    public String[] getFacets() {
        return facets;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String asReadableString(String delimiter) {
        ArrayList<String> parameters = new ArrayList<>();
        if (hasRegex) {
            parameters.add(MessageFormat.format(i18n.getString("model.condition.regex"), this.regex));
        }

        if (checkParent) {
            parameters.add(MessageFormat.format(i18n.getString("model.condition.check.parents"), String.join(delimiter, this.parentNames)));
        }

        if (start || eq) {
            String names = String.join(delimiter, this.names);
            if (start) {
                names = MessageFormat.format(i18n.getString("model.condition.name.starts.with"), names);
                if (noDot) {
                    names += i18n.getString("model.condition.name.starts.with.and.no.dot");
                }
            } else {
                names = MessageFormat.format(i18n.getString("model.condition.name.equals"), names);
            }
            parameters.add(names);
        }

        if (mayEnd || end) {
            String extensions = String.join(delimiter, this.extensions);
            if (mayEnd) {
                extensions = MessageFormat.format(i18n.getString("model.condition.name.may.end.with"), extensions);
            } else {
                extensions = MessageFormat.format(i18n.getString("model.condition.name.ends.with"), extensions);
            }
            parameters.add(extensions);
        }

        if (checkFacets) {
            parameters.add(MessageFormat.format(i18n.getString("model.condition.facets"), Arrays.toString(this.facets)));
        }

        return StringUtil.capitalize(String.join(", ", parameters));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelCondition that = (ModelCondition) o;
        return start == that.start &&
            eq == that.eq &&
            mayEnd == that.mayEnd &&
            end == that.end &&
            noDot == that.noDot &&
            checkParent == that.checkParent &&
            hasRegex == that.hasRegex &&
            enabled == that.enabled &&
            Arrays.equals(names, that.names) &&
            parentNames.equals(that.parentNames) &&
            Arrays.equals(extensions, that.extensions) &&
            Objects.equals(regex, that.regex) &&
            Arrays.equals(facets, that.facets);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(start, eq, mayEnd, end, noDot, checkParent, hasRegex, enabled, parentNames, regex);
        result = 31 * result + Arrays.hashCode(names);
        result = 31 * result + Arrays.hashCode(extensions);
        result = 31 * result + Arrays.hashCode(facets);
        return result;
    }
}
