<h1 align="center">
    Extra Icons
</h1>

<p align="center">
    <a href="https://plugins.jetbrains.com/plugin/11058-extra-icons"><img src="https://img.shields.io/jetbrains/plugin/v/11058-extra-icons.svg"/></a>
    <a href="https://plugins.jetbrains.com/plugin/11058-extra-icons"><img src="https://img.shields.io/jetbrains/plugin/d/11058-extra-icons.svg"/></a>
    <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/LICENSE.txt"><img src="https://img.shields.io/github/license/jonathanlermitage/intellij-extra-icons-plugin.svg"/></a>
</p>

Intellij IDEA (Community and Ultimate) plugin that adds icons for files like Travis YML, Appveyor YML, etc.  
Starting from version 0.4, it works with all JetBrains products like WebStorm, DataGrip, etc.

Download plugin on [GitHub](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/releases), [JetBrains Plugins Repository](https://plugins.jetbrains.com/plugin/11058-extra-icons) or via IntelliJ IDEA (<kbd>File</kbd>, <kbd>Settings</kbd>, <kbd>Plugins</kbd>, <kbd>Browse repositories...</kbd>).

## Version number

* starting from 0.19, there are two builds:
  * odd minor revision number (0.19, 0.21, 1.1, 1.3...): compatible with 173.0 IDE builds (aka 2017.3). This build doesn't bundles features that need 2018.3 IDE builds: AngularJS, SASS, Javascript. They're excluded because Extra Icons plugin reads project's type in order to activate some file recognition (AngularJS, SASS, Javascript): it is based on 2018.3 IDE features. Other files detection is simply based on files pattern, that's why it works with older IDE builds, and I will maintain a branch (`ide173`) to keep this support.
  * even minor revision number (0.20, 0.22, 1.0, 1.2...): compatible with latest IDE builds (183.0, aka 2018.3).
  
This way, you simply have to download the latest version offered by the plugins manager: on older IDE, you'll get the latest odd minor revision number. On recent IDE, you'll get the latest even minor revision number that sheeps same features as odd version, plus features that comes with recent IDE builds.

## Author

Jonathan Lermitage (<jonathan.lermitage@gmail.com>)  
Linkedin profile: [jonathan-lermitage-092711142](https://www.linkedin.com/in/jonathan-lermitage-092711142/)

## Contributors
Edoardo Luppi (<lp.edoardo@gmail.com>)

## Build

Starting from version 0.17, you need JetBrains `AngularJS`, `JavaScript` and `Sass support` plugins to build Extra Icons. Go to `Project Structure`, `SDKs`, select an IntelliJ platform (e.g. IntelliJ IDEA Ultimate) that bundles these plugins, then add `<thePlatform>/plugins/AngularJS/lib/AngularJS.jar!/`, `<thePlatform>/plugins/JavaScriptLanguage/lib/JavaScriptLanguage.jar!/`, `<thePlatform>/plugins/sass/lib/sass.jar!/` to classpath.

This support is optional, so you still can install Extra Icons on IntelliJ IDEA Community edition.

## License

MIT License. In other words, you can do what you want: this project is entirely OpenSource, Free and Gratis.

## Screenshot

![Dark Screenshot](https://raw.githubusercontent.com/jonathanlermitage/intellij-extra-icons-plugin/master/misc/screenshots/intellijidea-ce_dark.png)

![Screenshot](https://raw.githubusercontent.com/jonathanlermitage/intellij-extra-icons-plugin/master/misc/screenshots/intellijidea-ce.png)
