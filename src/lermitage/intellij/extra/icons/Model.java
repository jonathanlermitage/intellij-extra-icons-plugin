package lermitage.intellij.extra.icons;

@SuppressWarnings("WeakerAccess")
class Model {
    
    private boolean doEquals = false;
    private boolean doEqualsAndMayEndWith = false;
    private boolean doEndsWith = false;
    
    private String icon;
    private String base = "";
    private String[] extensions = new String[0];
    
    public Model(String icon) {
        this.icon = icon;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public Model willEqual(String base) {
        this.doEquals = true;
        this.base = base;
        return this;
    }
    
    public Model willEqualAndMayEndWith(String base, String... extensions) {
        this.doEqualsAndMayEndWith = true;
        this.base = base;
        this.extensions = extensions;
        return this;
    }
    
    public Model willEndWith(String... extensions) {
        this.doEndsWith = true;
        this.extensions = extensions;
        return this;
    }
    
    public boolean check(String name) {
        if (doEquals) {
            return name.equals(base);
        }
        if (doEndsWith) {
            for (String ext : extensions) {
                if (name.endsWith(ext)) {
                    return true;
                }
            }
        }
        if (doEqualsAndMayEndWith) {
            if (name.equals(base)) {
                return true;
            }
            for (String ext : extensions) {
                if (name.startsWith(base) && name.endsWith(ext)) {
                    return true;
                }
            }
        }
        return false;
    }
}
