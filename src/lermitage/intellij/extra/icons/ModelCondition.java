package lermitage.intellij.extra.icons;

import org.intellij.lang.annotations.Language;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ModelCondition {

    private boolean start = false;
    private boolean eq = false;
    private boolean mayEnd = false;
    private boolean end = false;
    private boolean noDot = false;
    private boolean checkParent = false;
    private boolean regex = false;

    private String[] names = new String[0];
    private Set<String> parentNames = Collections.emptySet();
    private String[] extensions = new String[0];
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
        this.regex = true;
        this.pattern = Pattern.compile(regex);
    }

    public boolean check(String parentName, String fileName, Optional<String> fullPath) {
        if (checkParent) {
            if (!parentNames.contains(parentName)) {
                return false;
            }
        }

        if (regex && fullPath.isPresent()) {
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
