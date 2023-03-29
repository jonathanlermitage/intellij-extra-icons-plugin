## Icon Pack tools

[NewUIFilesToOldUIThemeCreator.py](NewUIFilesToOldUIThemeCreator.py):  
- takes one parameter: IntelliJ Community [sources](https://github.com/JetBrains/intellij-community) folder. Per example, `python NewUIToOldUIThemeCreator.py C:\Downloads\intellij-community`.
- produces a JSON Icon Pack that changes new UI file icons to old UI file icons: `NewUIToOldUI-IconPack.json`.
- tested with Python 3.11.2 on Windows 10, and *should* work with any Python 3 version and OS.
