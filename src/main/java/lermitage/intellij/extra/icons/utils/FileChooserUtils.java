// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.util.Arrays;
import java.util.Optional;

public class FileChooserUtils {

    private static final boolean IS_IDE_SLOW_OPERATIONS_ASSERTION_ENABLED = Registry.is("ide.slow.operations.assertion", false);

    public static Optional<String> chooseFile(String diagTitle,
                                              Component parent,
                                              String filterTitle,
                                              String... filterExtensions) {
        return chooseFileOrFolder(FileChooserType.FILE, diagTitle, parent, filterTitle, filterExtensions);
    }

    public static Optional<String> chooseFolder(String diagTitle,
                                                Component parent) {
        return chooseFileOrFolder(FileChooserType.FOLDER, diagTitle, parent, null);
    }

    private static Optional<String> chooseFileOrFolder(FileChooserType fileChooserType,
                                                       String diagTitle,
                                                       Component parent,
                                                       String filterTitle,
                                                       String... filterExtensions) {
        String filePath = null;
        if (IS_IDE_SLOW_OPERATIONS_ASSERTION_ENABLED) {
            // FIXME temporary workaround for "Slow operations are prohibited on EDT" issue
            //  https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/126
            //  We should be able to use FileChooser.chooseFile instead of JFileChooser.showOpenDialog
            JFileChooser dialogue = new JFileChooser();
            dialogue.setDialogTitle(diagTitle);
            dialogue.setMultiSelectionEnabled(false);
            if (fileChooserType == FileChooserType.FILE) {
                dialogue.setFileSelectionMode(JFileChooser.FILES_ONLY);
                dialogue.setFileFilter(new FileNameExtensionFilter(filterTitle, filterExtensions));
            } else {
                dialogue.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            }
            dialogue.setAcceptAllFileFilterUsed(false);
            if (dialogue.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                filePath = dialogue.getSelectedFile().getAbsolutePath();
            }
        } else {
            FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(
                fileChooserType == FileChooserType.FILE, fileChooserType == FileChooserType.FOLDER, false,
                false, false, false);
            fileChooserDescriptor.setTitle(diagTitle);
            fileChooserDescriptor.setHideIgnored(false);
            fileChooserDescriptor.setShowFileSystemRoots(true);
            if (fileChooserType == FileChooserType.FILE) {
                fileChooserDescriptor.withFileFilter(virtualFile -> {
                    return Arrays.stream(filterExtensions).anyMatch(s -> s.equalsIgnoreCase(virtualFile.getExtension()));
                });
            }
            VirtualFile virtualFile = FileChooser.chooseFile(fileChooserDescriptor, null, null);
            filePath = virtualFile != null ? virtualFile.getPath() : null;
        }
        return Optional.ofNullable(filePath);
    }

    private enum FileChooserType {
        FILE,
        FOLDER
    }
}
