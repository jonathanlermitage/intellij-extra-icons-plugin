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

    public static Set<GitSubmodule> findGitSubmodules(String rootPath) throws FileNotFoundException {
        File rootGitmodules = new File(rootPath, GIT_MODULES_FILENAME);
        if (!rootGitmodules.exists()) {
            return Collections.emptySet();
        }
        Set<GitSubmodule> submodules = new HashSet<>();
        Scanner scanner = new Scanner(rootGitmodules);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = GIT_MODULES_PATH_PATTERN.matcher(line);
            if (matcher.find()) {
                String path = matcher.group(1);
                submodules.add(new GitSubmodule(rootPath + "/" + path));
            }
        }
        return submodules;
    }
}
