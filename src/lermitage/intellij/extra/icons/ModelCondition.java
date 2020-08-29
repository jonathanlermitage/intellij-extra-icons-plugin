// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import com.intellij.util.xmlb.annotations.Tag;
import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
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
    private String[] names = new String[0];
    @OptionTag
    private Set<String> parentNames = Collections.emptySet();
    @OptionTag
    private String[] extensions = new String[0];
    @OptionTag
    private String regex;
    @OptionTag
    private String[] facets = new String[0];
    private Pattern pattern;

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

    public void setRegex(@Language("RegExp") String regex) {
        this.hasRegex = true;
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    public void setFacets(String[] facets) {
        this.checkFacets = true;
        this.facets = facets;
    }

    public boolean check(String parentName, String fileName, Optional<String> fullPath, Set<String> prjFacets) {
        if (!enabled) {
            return false;
        }

        // facet is a pre-condition, should always be associated with other conditions
        if (checkFacets) {
            boolean facetMatched = false;
            for (String modelFacet : facets) {
                for (String prjFacet : prjFacets) {
                    if (modelFacet.equalsIgnoreCase(prjFacet)) {
                        facetMatched = true;
                        break;
                    }
                }
                if (facetMatched) {
                    break;
                }
            }
            if (!facetMatched) {
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

        if (hasRegex && fullPath.isPresent()) {
            if (pattern == null) {
                pattern = Pattern.compile(regex);
            }
            if (pattern.matcher(fullPath.get()).matches()) {
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
            parameters.add("regex: " + this.regex);
        }

        if (checkParent) {
            parameters.add("parent(s): " + String.join(delimiter, this.parentNames));
        }

        if (start || eq) {
            String names = String.join(delimiter, this.names);
            if (start) {
                names = "name starts with: " + names;
                if (noDot) {
                    names += " and does not contain a dot";
                }
            } else {
                names = "name equals: " + names;
            }
            parameters.add(names);
        }

        if (mayEnd || end) {
            String extensions = String.join(delimiter, this.extensions);
            if (mayEnd) {
                extensions = "name may end with: " + extensions;
            } else {
                extensions = "name ends with: " + extensions;
            }
            parameters.add(extensions);
        }

        if (checkFacets) {
            parameters.add("facets: " + Arrays.toString(this.facets));
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
