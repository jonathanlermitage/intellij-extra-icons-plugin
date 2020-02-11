package lermitage.intellij.extra.icons;

import com.intellij.util.xmlb.annotations.OptionTag;
import com.intellij.util.xmlb.annotations.Tag;
import org.intellij.lang.annotations.Language;

import java.util.Collections;
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
    private String[] names = new String[0];
    @OptionTag
    private Set<String> parentNames = Collections.emptySet();
    @OptionTag
    private String[] extensions = new String[0];
    @OptionTag
    private String regex;
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

    public void setExtensions(String... extensions) {
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

    public boolean check(String parentName, String fileName, Optional<String> fullPath) {
        if (checkParent) {
            if (!parentNames.contains(parentName)) {
                return false;
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
}
