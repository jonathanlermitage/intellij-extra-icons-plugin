// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitSubmoduleUtils {

    private static final String GIT_MODULES_FILENAME = ".gitmodules";
    private static final Pattern GIT_MODULES_PATH_PATTERN = Pattern.compile("\\s*path\\s*=\\s*([^\\s]+)\\s*");

    /**
     * Find Git submodules.
     * @param rootPath root directory's path.
     * @return submodule paths relative to project's root directory.
     * @throws FileNotFoundException if root directory doesn't exist.
     */
    public static Set<String> findGitSubmodules(String rootPath) throws FileNotFoundException {
        File rootGitModules = new File(rootPath, GIT_MODULES_FILENAME);
        if (!rootGitModules.exists()) {
            return Collections.emptySet();
        }
        Set<String> submodules = new HashSet<>();
        Scanner scanner = new Scanner(rootGitModules);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = GIT_MODULES_PATH_PATTERN.matcher(line);
            if (matcher.find()) {
                String path = matcher.group(1);
                submodules.add(rootPath + "/" + path);
            }
        }
        return submodules;
    }
}
