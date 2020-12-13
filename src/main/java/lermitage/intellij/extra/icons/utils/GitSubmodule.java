// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import java.util.Objects;

public class GitSubmodule {

    /** Path relative to project's root directory. */
    private final String path;

    public GitSubmodule(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitSubmodule that = (GitSubmodule) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "GitSubmodule{" +
            "path='" + path + '\'' +
            '}';
    }
}
