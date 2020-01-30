package lermitage.intellij.extra.icons;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
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
    private boolean regex = false;
    
    private String icon;
    private String[] names = new String[0];
    private Set<String> parentNames = Collections.emptySet();
    private String[] extensions = new String[0];
    private Pattern pattern;
    
    private String id;
    private String description;
    private ModelType modelType;
    
    @NotNull
    @Contract("_, _, _ -> new")
    public static Model ofFile(String id, String icon, String description) {
        return new Model(id, icon, description, ModelType.FILE);
    }
    
    @NotNull
    @Contract("_, _, _ -> new")
    public static Model ofDir(String id, String icon, String description) {
        return new Model(id, icon, description, ModelType.DIR);
    }
    
    private Model(String id, String icon, String description, ModelType modelType) {
        this.id = id;
        this.icon = icon;
        this.description = description;
        this.modelType = modelType;
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
    
    public ModelType getModelType() {
        return modelType;
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
    
    public Model regex(String regex) {
        this.regex = true;
        this.pattern = Pattern.compile(regex);
        return this;
    }
    
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
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
