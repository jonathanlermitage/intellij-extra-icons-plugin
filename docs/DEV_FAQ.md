# Plugin Dev FAQ

## Some common problems and their solution or workaround

---

**Problem**: `class com.intellij.ui.Win7TaskBar cannot access class sun.awt.windows.WComponentPeer (in module java.desktop) because module java.desktop does not export sun.awt.windows to unnamed module @7f2ef9a8` or similar error when you run the plugin with the `runIde` task.

**Solution**: add missing exports if [gradle-intellij-plugin](https://github.com/JetBrains/gradle-intellij-plugin) is not synced with the latest IJ [OpenedPackages.txt](https://raw.githubusercontent.com/JetBrains/intellij-community/master/plugins/devkit/devkit-core/src/run/OpenedPackages.txt) file. Example: add this line to the `build.gradle.kts`'s `runIde` task: 
```kotlin
jvmArgs("--add-exports", "java.desktop/sun.awt.windows=ALL-UNNAMED")
```

This is usually quickly fixed by the latest gradle-intellij-plugin update.

---

