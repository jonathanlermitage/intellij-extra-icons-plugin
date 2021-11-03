<h1 align="center">
    <a href="https://plugins.jetbrains.com/plugin/11058-extra-icons">
      <img src="./src/main/resources/META-INF/pluginIcon.svg" width="84" height="84" alt="logo"/>
    </a><br/>
    Extra Icons
</h1>

<p align="center">
    <a href="https://plugins.jetbrains.com/plugin/11058-extra-icons"><img src="https://img.shields.io/jetbrains/plugin/v/11058-extra-icons.svg"/></a>
    <a href="https://plugins.jetbrains.com/plugin/11058-extra-icons"><img src="https://img.shields.io/jetbrains/plugin/d/11058-extra-icons.svg"/></a>
    <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/LICENSE.txt"><img src="https://img.shields.io/github/license/jonathanlermitage/intellij-extra-icons-plugin.svg"/></a>
    <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/graphs/contributors"><img src="https://img.shields.io/github/contributors/jonathanlermitage/intellij-extra-icons-plugin"/></a><br>
    <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/actions?query=workflow%3A%22Build%22"><img src="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/workflows/Build/badge.svg"/></a>
    <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/actions?query=workflow%3A%22Compatibility%22"><img src="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/workflows/Compatibility/badge.svg"/></a><br>
    <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/actions?query=workflow%3A%22Build+EAP%22"><img src="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/workflows/Build%20EAP/badge.svg"/></a>
    <a href="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/actions?query=workflow%3A%22Compatibility+EAP%22"><img src="https://github.com/jonathanlermitage/intellij-extra-icons-plugin/workflows/Compatibility%20EAP/badge.svg"/></a>
</p>

Intellij IDEA (Community and Ultimate) plugin that adds icons for files like Travis YML, Appveyor YML, Git sub-modules, etc.  
Starting from version 0.4, it works with all JetBrains products like WebStorm, DataGrip, etc.

Download plugin from [JetBrains Plugins Repository](https://plugins.jetbrains.com/plugin/11058-extra-icons) or via your JetBrains IDE (<kbd>File</kbd>, <kbd>Settings</kbd>, <kbd>Plugins</kbd>, <kbd>Browse repositories...</kbd>).

1. [Build](#build)  
2. [Contribution](#contribution)  
3. [Known issues](#known-issues)  
4. [License](#license)  
5. [Credits](#credits)  
6. [Screenshots](#screenshots)  

## Build

Install JDK11+. You should be able to start Gradle Wrapper (`gradlew`). See Gradle commands below. 

### Gradle commands

```bash
$ gradlew buildPlugin        # build plugin to build/distributions/*.zip
$ gradlew runIde             # try plugin in a standalone IDE
$ gradlew dependencyUpdates  # check for dependencies updates
$ gradlew verifyPlugin       # validate plugin.xml descriptors as well as plugin's archive structure
$ gradlew runPluginVerifier  # check for compatibility issues with IDE
$ gradlew test               # run tests
```

Additionally, take a look at the `do` (or `do.cmd` on Windows) script: it contains useful commands to build, run and test the plugin, check for dependencies updates and some maintenance tasks. Show available commands by running `./do help`. 

### Optimizations

Optionally, you may want to install SVGO in order to optimize SVG icons. Install `npm install -g svgo`, then optimize SVG files by running `./do svgo`.

### Branches

* [ide213](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/tree/ide213): (**future branch**)  plugin is compatible with 213+ IDE builds (2021.3 and newer). It enables plugin hot reloading (you don't have to restart IDE on plugin update). An old bug could remove some Extra Icons preferences at project level if plugin is updated and plugin hot reloading is enabled. This issue should be fixed in recent IDEs.
* [**ide203**](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/tree/ide203): (**active branch**)  plugin is compatible with 203+ IDE builds (2020.3 and newer). It contains features that need capabilities introduced in IntelliJ 2020.3.
* [ide201](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/tree/ide201): ~~plugin is compatible with 201+ IDE builds (2020.1 and newer).~~ this branch is terminated.
* [master](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/tree/master): ~~plugin is compatible with 183+ IDE builds (2018.3 and newer).~~ this branch is terminated.
* [ide173](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/tree/ide173): ~~plugin is compatible with 173+ IDE builds (2017.3 and newer), but doesn't support AngularJS icons.~~ this branch is terminated.

## Contribution

Open an issue or a pull-request:

* feature request -> support new icons: please provide SVG (with `height="16" width="16"` header) or 32x32 PNG (with transparency) file(s) and filename patterns.
* contributions: they should be tested.      
  Please reformat new and modified code only: do not reformat the whole project or entire existing file (in other words, try to limit the amount of changes in order to speed up code review).  
  To finish, don't hesitate to add your name or nickname (and LinkedIn profile, etc.) to contributors list ;-)

## Known issues

See [KNOWN_ISSUES.md](KNOWN_ISSUES.md) and [GitHub open issues](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues).

## License

MIT License. In other words, you can do what you want: this project is entirely OpenSource, Free and Gratis.

## Credits

### 🤝 Contributors

* (author) Jonathan Lermitage (<jonathan.lermitage@gmail.com>, [![linkedin](misc/linkedin_profile_badge.png)](https://www.linkedin.com/in/jonathan-lermitage-092711142/), [buymeacoff.ee](https://buymeacoff.ee/jlermitage))
* Edoardo Luppi (<lp.edoardo@gmail.com>)
* Matthias Kunnen ([github.com/MatthiasKunnen](https://github.com/MatthiasKunnen))
* Florian Böhm ([github.com/Sheigutn](https://github.com/Sheigutn))
* Mateusz Bajorek (<mateusz.bajorek@gmail.com>)
* Bouteiller Alan ([github.com/bouteillerAlan](https://github.com/bouteillerAlan))
* Gaëtan Maisse ([github.com/gaetanmaisse](https://github.com/gaetanmaisse))

### 🤝 [JetBrains](https://www.jetbrains.com/idea/)

Thanks for their Open-Source licence to their amazing IDEs.

### 🤝 [Syntevo](https://www.syntevo.com/smartgit/)

Thanks for their licence for **SmartGit** (an excellent graphical Git client, free for non-commercial projects).

## Screenshots

![Dark Screenshot](misc/screenshots/intellijidea-ce_dark.png)

![Screenshot](misc/screenshots/intellijidea-ce.png)

![Config Panel Screenshot](misc/screenshots/config-panel.png)
