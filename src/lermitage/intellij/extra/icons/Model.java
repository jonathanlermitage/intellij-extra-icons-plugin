package lermitage.intellij.extra.icons;

@SuppressWarnings("WeakerAccess")
class Model {
    
    private boolean start = false;
    private boolean eq = false;
    private boolean mayEnd = false;
    private boolean end = false;
    
    private String icon;
    private String[] names = new String[0];
    private String[] extensions = new String[0];
    
    public Model(String icon) {
        this.icon = icon;
    }
    
    public String getIcon() {
        return icon;
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
    
    public boolean check(String name) {
        if (eq) {
            if (end) {
                for (String n : names) {
                    for (String e : extensions) {
                        if (name.equals(n + e)) {
                            return true;
                        }
                    }
                }
            } else if (mayEnd) {
                for (String n : names) {
                    if (name.equals(n)) {
                        return true;
                    }
                    for (String e : extensions) {
                        if (name.equals(n + e)) {
                            return true;
                        }
                    }
                }
            } else {
                for (String n : names) {
                    if (name.equals(n)) {
                        return true;
                    }
                }
            }
        }
        
        if (start) {
            if (end) {
                for (String n : names) {
                    for (String e : extensions) {
                        if (name.startsWith(n) && name.endsWith(e)) {
                            return true;
                        }
                    }
                }
            } else if (mayEnd) {
                for (String n : names) {
                    if (name.startsWith(n)) {
                        return true;
                    }
                    for (String e : extensions) {
                        if (name.startsWith(n) && name.endsWith(e)) {
                            return true;
                        }
                    }
                }
            } else {
                for (String n : names) {
                    if (name.startsWith(n)) {
                        return true;
                    }
                }
            }
        }
        
        if (end) {
            for (String e : extensions) {
                if (name.endsWith(e)) {
                    return true;
                }
            }
        }
        return false;
    }
}
