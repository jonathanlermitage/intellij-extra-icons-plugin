package lermitage.intellij.extra.icons;

@SuppressWarnings("WeakerAccess")
class Model {
    
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
            for (String n : names) {
                if (name.equals(n)) {
                    return true;
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
        if (mayEnd) {
            for (String e : extensions) {
                for (String n : names) {
                    if (name.startsWith(n) && name.endsWith(e)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
