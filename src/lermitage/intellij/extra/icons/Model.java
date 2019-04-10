package lermitage.intellij.extra.icons;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class Model {
    
    private boolean start = false;
    private boolean eq = false;
    private boolean mayEnd = false;
    private boolean end = false;
    private boolean noDot = false;
    private boolean checkParent = false;
    
    private String icon;
    private String[] names = new String[0];
    private Set<String> parentNames = Collections.emptySet();
    private String[] extensions = new String[0];
    
    private String id;
    private String description;
    private boolean enabled = true;
    
    public Model(String id, String icon, String description) {
        this.id = id;
        this.icon = icon;
        this.description = description;
    }
    
    public String getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public Model parents(String... parents) {
        this.checkParent = true;
        this.parentNames = Stream.of(parents).collect(Collectors.toSet());
        return this;
    }
    
    public Model start(String... base) {
        this.start = true;
        this.names = base;
        return this;
    }
    
    public Model eq(String... base) {
        this.eq = true;
        this.names = base;
        return this;
    }
    
    public Model mayEnd(String... extensions) {
        this.mayEnd = true;
        this.extensions = extensions;
        return this;
    }
    
    public Model end(String... extensions) {
        this.end = true;
        this.extensions = extensions;
        return this;
    }
    
    public Model noDot() {
        this.noDot = true;
        return this;
    }
    
    public boolean check(String parentName, String fileName) {
        if (!enabled) {
            return false;
        }
        if (checkParent) {
            if (!parentNames.contains(parentName)) {
                return false;
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
