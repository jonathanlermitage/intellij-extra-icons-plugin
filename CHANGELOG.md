# Extra Icons Change Log

## 2025.1.19 (WIP)
* UI reworks in the plugin's settings panel.

## 2025.1.18 (2025/11/22)
* fix [#228](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/228): previous plugin release broke C# support. This is now fixed.
* fix [#229](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/229): NPE when creating a User icon.
* fix [#230](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/230): changes to the "Lowercase file path before check" property were not persisted on User icon modification (User icon creation was OK).
* fix [#231](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/231): when creating a User icon with a Regex condition, setting "Lowercase file path before check" did not lowercase file paths.

## 2025.1.17 (2025/11/21)
* this release adds compatibility with Huawei DevEco Studio and has been tested with DevEco Studio 5.1.1 (I don't have access to DevEco Studio 6 yet, but it should work). [See how to install plugin and activate your license in DevEco Studio](https://jonathanlermitage.github.io/ij-extra-tools-pack-docs/deveco-studio.html).
* support more older IDEs. The minimal IDE version is now 2023.3 (previously 2024.3).
* implement [#223](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/223): support SQL Script files. 2025.3 EAP IDEs introduce a new icon for (some?) SQL files, but it looks like the icon for generic Script files. A new icon association has therefore been added in Extra Icons to restore the previous SQL icon (see "SQL Script" in the  Plugin icons list, New UI only).
* implement [#224](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/224): support Rebar3 `rebar.config` and `rebar.lock` files.
* fix [#226](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/226): NPE when registering a user icon.
* support Extended Typescript `*.ets` files (DevEco Studio only).
* support `oh-package.json5` and `oh-package-lock.json5` files (DevEco Studio only).

## 2025.1.16 (2025/10/16)
* fix usage of some JetBrains deprecated APIs, improving the compatibility with future IDEs (2025.3+).

## 2025.1.15 (2025/10/08)
* user icons: you can now choose to check file paths against the original paths or lowercase paths. [Documentation](https://jonathanlermitage.github.io/ij-extra-tools-pack-docs/extra-icons-user-icons.html#conditions).
* fix usage of some JetBrains deprecated APIs, improving the compatibility with future IDEs (2025.3+).
* update the JUnit icon: it now uses the JUnit 6 logo. You can still use the JUnit 5 icon if you want (see JUnit alternative icons).
* add a new Deno icon. The old Deno icon is still available as an alternative icon.

## 2025.1.14 (2025/09/15)
* support more files when choosing the `Always prefer old UI icons` option in Extra Icons' advanced settings. This is an effort to make this mode more consistent for people who prefer the classic icons (even when using the New UI), or people who are not comfortable with the wireframe icons that come with the New UI.
* fix [#214](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/214): support Razor `*.razor` and `.cshtml` files.
* rework the dark icon variant of Contact files.

## 2025.1.13 (2025/08/27)
* support Firefox extension files `*.xpi`.
* support [Forgejo](https://forgejo.org) `.forgejo` folders.

## 2025.1.12 (2025/08/05)
* new series of workarounds for the [IDEA-376370](https://youtrack.jetbrains.com/issue/IDEA-376370/) issue. Up-voting this issue would greatly help.

## 2025.1.11 (2025/07/30)
* workaround for the [IDEA-376370](https://youtrack.jetbrains.com/issue/IDEA-376370/) issue.

## 2025.1.10 (2025/07/24)
* better fix for [#219](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/219): the previous fix broke some icon customizations, like Docker, Helm, or Git submodules support.

## 2025.1.9 (2025/07/17)
* fix [#219](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/219): `java.lang.IllegalStateException: WriteIntentReadAction can not be called from ReadAction` errors could occur on project loading or when refreshing icons when using 2025.1.3 IDEs or the latest IntelliJ 2025.2 EAP build (IU-252.23591.19).

## 2025.1.8 (2025/07/11)
* reintroduce the alt icon for PHP Composer files. It had been removed by mistake.

## 2025.1.7 (2025/06/27)
* fix [#215](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/215): `com.intellij.diagnostic.PluginException: / by zero` errors.

## 2025.1.6 (2025/06/13)
* the minimal IDE version is now 2024.3 instead of 2023.3.1. This was needed to use newer JetBrains APIs and stay compatible with future IDEs (2025.2+).
* fix usage of some JetBrains deprecated APIs, improving the compatibility with future IDEs (2025.2+).
* implement [#207](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/207): regular expressions shouldn't need to fully match. An option has been added to model conditions, which lets you choose whether a regular expression should match the entire file path or only part of a file path. The default setting is set to match the entire file path.
* [#206](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/206): support Visual Studio `*.csproj`, `*.sln` and `*.slnx` files.
* [#212](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/212): support Fish Shell `*.fish` files.
* fix [#209](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/209): rework how user icon conditions are evaluated. This now respects the [documentation](https://jonathanlermitage.github.io/ij-extra-tools-pack-docs/extra-icons-user-icons.html#conditions).
* minor visual improvements in settings dialogs.
* general performance improvement. Performance was already good, and a new caching mechanism makes the file-icon association ~2x faster, with a minimal memory footprint.
* rework several bundled file-icon associations.

## 2025.1.5 (2025/05/06)
* additional fix for [#200](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/200): error `Class initialization must not depend on services. Consider using instance of the service on-demand instead` may occur on IDE start with IntelliJ 2025.1.

## 2025.1.4 (2025/04/14)
* fix [#193](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/193): support NestJS `*.resolver.(js|ts)` files.
* minor code reworks.

## 2025.1.3 (2025/03/17)
* settings can now be loaded and saved by the **Backup and Sync** plugin.
* fix [#200](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/200): error `Class initialization must not depend on services. Consider using instance of the service on-demand instead` may occur on IDE start with IntelliJ 2025.1 EAP (251.22821.72).
* implement [#202](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/202): support `xunit.runner.json` files.
* implement [#203](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/203): support `log4net.config` files.
* implement [#204](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/204): support Stryker Mutator `stryker-config.json` and `stryker-config.yaml` files.
* support `.sdkmanrc` files.
* settings UI: improve the User Icons table. If no user icon has yet been registered, display a message in the center of the table and a convenient way to add a first icon. Similar thing for the New Icon dialog, when no Condition has yet been registered.
* various code reworks.

## 2025.1.2 (2025/02/25)
* fix [#195](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/195): some custom icons are not working in Explorer in Rider.
* fix [#196](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/196): using Presentation Mode does not resize user icons properly.
* the zoom level set in the IDE's accessibility options is now applied to user icons. This will fix potential bad rendering (wrong scale) of user icons when using any zoom level.
* improve the plugin's compatibility range. The minimal IDE version is now 2023.3.1 instead of 2024.1.

## 2025.1.1 (2025/01/21)
* you can now add icons to Actions in menus. For example, add an icon to the `right-click > Git > Rebase...` action. You can also overwrite an action's icon if it already has one. For icons associated with intermediate menus, JetBrains does not allow that. If you're interested in this missing feature, please upvote [IDEA-364676](https://youtrack.jetbrains.com/issue/IDEA-364676).
* rework how the Chinese language is detected. This should avoid rare scenarios where the Chinese localization of the plugin was loaded on non-Chinese computers.
* [documentation](https://jonathanlermitage.github.io/ij-extra-tools-pack-docs/extra-icons.html).

## 2024.10.1 (2024/12/13)
* fix [#192](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/192): custom icons not working in Explorer tool window in Rider for C# project.
* implement [#191](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/191): support `eslint.config.js` files.
* implement [#191](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/191): support various [Stylelint](https://stylelint.io/user-guide/configure) files.
* the [online documentation](https://jonathanlermitage.github.io/ij-extra-tools-pack-docs/) has been completely rewritten.
* minor UI reworks in the settings panel.

## 2024.9.1 (2024/11/16)
* fix [#190](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/190): Extra Icons not configurable at project level. This was a regression from the previous plugin version. Sorry for the inconvenience.
* support Embedded Open Type, TrueType Font, and Web Open Font Format font files.
* add more alternative icons for `*.cmd`, `*.bat`, Powershell `*.ps1`, Bash `*.sh` files.
* support Bash (without `.sh` extension) files.
* rework the `*.env` file icon.

## 2024.8.2 (2024/10/29)
* fix potential component ID collisions when installing this plugin alongside other obfuscated plugins.
* code rework: replace usage of obsolete JetBrains APIs, improving compatibility with future IDEs.

## 2024.8.1 (2024/10/21)
* support `Makefile` files.
* UI reworks in the settings panel.
* minor code rework.

## 2024.7.3 (2024/09/23)
* fix usage of some JetBrains deprecated APIs, improving the compatibility with future IDEs (2024.3+).

## 2024.7.2 (2024/09/17)
* improve compatibility with future IDEs (2024.3+).
* avoid some rare NPE errors when importing an old Extra Icons config at project level.
* a recent bug in Extra Icons (2024.6.4, 2024/07/29) prevented icon override for files with multiple dots in their name. This is fixed now.
* fix usage of some JDK deprecated APIs.

## 2024.7.1 (2024/08/27)
* improve compatibility when the Python plugin (Ultimate or Community) is installed.
* rework Python icons (virtualenv, requirements.txt, wheel) and provide icons for the new UI.

## 2024.6.4 (2024/07/29)
* implement [#184](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/discussions/184):  support `.gitea` folders and `*.yml` files in `workflows` subdirectories.
* fix [#183](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/183): custom icons should apply to files and folders only, not to class members like properties, methods and nested classes.
* fix [#185](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/185): `requirements.txt` should have a custom icon in PyCharm.
* add more alternative icons for Java Records.

## 2024.6.3 (2024/07/21)
* UI improvement (for non-EAP IDEs only): use IDE's file chooser dialog instead of the regular Swing file chooser dialog.
* minor performance improvements.

## 2024.6.2 (2024/07/08)
* minor performance improvements and enable future performance improvements for 2024+ IDEs (based on JBR21).
* general stability improvements. Some internal concurrent components were not ideally synchronized, which could lead to minor performance degradations. This is now fixed.
* support [Vale](https://vale.sh) `.vale.ini` files.

## 2024.6.1 (2024/06/26)
* partially implement [IDEA-352785](https://youtrack.jetbrains.com/issue/IDEA-352785): provide icons for Kotlin classes, interfaces, enums, etc., without the small "K" badge.

## 2024.5.2 (2024/06/21)
* 2024.2 IDEs now bundle the Chinese Language Pack plugin. For these IDEs, plugin's Chinese UI is now applied only if you select the Chinese locale (File > Appearance & Behavior > System Settings > Language and Region). For older IDEs, plugin's Chinese UI is applied if you download and enable the Chinese Language Pack plugin.

## 2024.5.1 (2024/06/15)
* replace the plugin's custom error reporter by the new IDE [Exception Analyzer](https://plugins.jetbrains.com/docs/marketplace/exception-analyzer.html). This is an easy way to report plugin exceptions from IntelliJ Platform-based products to plugin developers right on JetBrains Marketplace, instead of opening an issue on the plugin's GitHub repository.

## 2024.4.2 (2024/05/24)
* minor code rework.
* suppress the notification about the new subscription price update.
* internal: migrate to IntelliJ Platform Gradle Plugin v2, ensuring build compatibility with 2024.2+ IDEs.

## 2024.4.1 (2024/05/15)
* override Java Record icon (offer new colors).
* fix a case where user icons were loaded after bundled icons. User icons should always have the highest priority.
* fix usage of some JetBrains deprecated APIs.
* minor performance improvement when loading plugin.

## 2024.3.4 (2024/04/03)
* fix [#182](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/182) `java.lang.Throwable: Must be precomputed` error.

## 2024.3.3 (2024/03/19)
* support Doxygen `Doxyfile` files.
* **Important**: Starting from April 15th, 2024, I will increase the Extra Icons plugin's subscription price (€&nbsp;1.50&nbsp;/1st year, €&nbsp;1.20&nbsp;/2nd&nbsp;year, €&nbsp;0.90&nbsp;/3rd year onwards for monthly subscriptions, and €&nbsp;15/12/9 for annual subscriptions). **If you have enabled automatic license renewal, JetBrains will email you one week before the renewal**.<br>You can still cancel your automatic license renewal if you want, at any time.<br>I wanted to update the subscription price in order to reflect the amount of work I am dedicating to this project.<br>Perpetual fallback licenses are still available and, the day JetBrains allows plugins developers to sell lifetime licenses, I will be ready.

## 2024.3.2 (2024/03/02)
* lower minimum IDE version to 2023.1 (from 2023.2). It will also restore compatibility with the current stable release of Android Studio: Hedgehog. I will raise minimum IDE version to 2023.2 later, when the related deprecated used JetBrains API is removed. There's no impact on stability and performance.
* support some potential [Kotlin Multiplatform](https://kmp.jetbrains.com/) folders: `composeApp/src/androidMain`, `composeApp/src/commonMain`, `composeApp/src/desktopMain`, `composeApp/src/iosMain`, `iosMain`.
* support some potential [Fleet plugin](https://github.com/JetBrains/fleet-plugin-template) folders: `fleet-plugin`, `fleet-plugin/backendImpl`, `fleet-plugin/commonImpl`, `fleet-plugin/frontendImpl`, `fleet-plugin/workspaceImpl`.
* minor performance optimization: reduce threads spawning by using IDE thread pool and scheduler instead of starting new threads for scheduled tasks.

## 2024.3.1 (2024/02/21)
* fix compatibility with future builds of IntelliJ 2024.1 based IDEs.
* reduce usage of deprecated IntelliJ's API, and raise minimum IDE version to 2023.2 (from 2023.1).
* rework the `.idea`, `.idea-sandbox`, `idea-sandbox`, `.mps`, `node_modules`, Circle CI, Dependabot, Docker, Expo, Flyway, Gitlab, GitHub, Gradle, Liquibase, Maven wrapper, Nuget, Semaphore, Storybook and VSCode folder icons for the new UI.
* rework the `node_modules` folder icon.
* support `CODE_OF_CONDUCT` files (with `.md`, `.adoc`, `.txt`, `.rst` or no extension).
* support `intellij.yaml` files.
* support [LivePlugin](https://plugins.jetbrains.com/docs/intellij/plugin-alternatives.html#liveplugin) `.live-plugins` folders.
* support Qodana `qodana.yml` files.
* support some [Typos](https://github.com/crate-ci/typos/blob/master/docs/reference.md#sources) config files.
* don't throw an IDE error when failed to import/export/uninstall a user icon pack. Show a standard IntelliJ error dialog instead.
* move the list of known issues and workarounds and fixes to [online doc](https://plugins.jetbrains.com/plugin/11058-extra-icons/known-issues).
* plugin description: fix the link to Gateway support online documentation.
* hints and notifications now display Extra Icons' logo.
* replace some custom dialogs by standard IntelliJ dialogs.
* internal: minor performance improvements, improve code quality, code refactorings, improve stability and maintainability.
* internal: huge improvements to the plugin build script.

## 2024.2.2 (2024/01/18)
* fix `java.lang.IllegalArgumentException: Illegal character in path at index 8` errors when loading user icons with IntelliJ 2024.1 EAP on Windows.
* minor performance improvements.
* make loggers less verbose and rework some error messages.

## 2024.2.1 (2024/01/15)
* optimize icons size, reducing plugin's weight.
* fix [#176](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/176): clicking 'Go to Extra Icons settings' from the notification just throws an error.
* remove Gateway support. This now allows me to reintroduce some UI enhancements (like regex colouring in regex text fields), improve icons override in VCS dialogs, reduce plugin's weight by removing libraries that were needed by Gateway (and because they could also interfere with libraries bundled with IDE). [See details online](https://plugins.jetbrains.com/plugin/11058-extra-icons/gateway-support).
* minor performance improvements.
* improve licensing management.
* internal: important improvements to the plugin build script.

## 2024.1.1 (2024/01/02)
* **INFO**: JetBrains will introduce a new business model for paid/freemium plugins. This model will offer a perpetual license, **allowing users to make a one-time payment for the plugin and use it for a lifetime**. [Get more information here](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/docs/LICENSE_FAQ.md#how-to-get-a-lifetime-license). There is no ETA yet.
* build script: prepare the work for the introduction of lifetime licenses.
* fix [#175](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/175): `java.lang.Throwable: Must be precomputed`. UI Scale detection could fail at startup. It should be fixed now.
* initial support of [Flutter](https://flutter.dev) icons.
* improve Prettier support.
* improve Gradle support.
* add the new alternative GitLab "next" orange icon for `.gitlab-ci.yml` files.
* rework GitLab folder icon.
* rework Privacy icon.
* support `(.)log(s)` folders.
* support `.noai` files (which tell the AI Assistant plugin to block AI features for the containing project).
* support [Detekt](https://detekt.dev/docs/introduction/configurations/) files ending by `detekt-config.yml` or `detekt.yml` (I may try to pick the configured Detekt filename from the `build.gradle(.kts)` file later).
* support Qodana `qodana.yaml` files.
* plugin's license is now verified occasionally, in addition to the verification done by IDE at startup.
* starting from 2024, I will occasionally send some [coupons](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/docs/LICENSE_FAQ.md#i-received-a-coupon-for-a-free-license-how-does-it-work) for Free licenses on [Twitter](https://twitter.com/JLermitage) and [Bluesky](https://bsky.app/profile/jonathanlermitage.bsky.social). Stay tuned.

## 2023.4.2 (2023/12/03)
* **INFO**: JetBrains will introduce a new business model for paid/freemium plugins. This model will offer a perpetual license, **allowing users to make a one-time payment for the plugin and use it for a lifetime**. [Get more information here](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/docs/LICENSE_FAQ.md#how-to-get-a-lifetime-license).
* implement [IDEA-336801](https://youtrack.jetbrains.com/issue/IDEA-336801) workaround: IDE's icon cache prevented some icons override on refresh (per example, after you enabled or disabled a custom icon).
* implement a temporary and partial workaround for [IDEA-339254](https://youtrack.jetbrains.com/issue/IDEA-339254): can't refresh Java Exception and Java Abstract Exception IDE icons.
* settings panel: inform user about some IntelliJ issues affecting this plugin.
* support `.git-blame-ignore-revs` files.
* add Angular 17 "renaissance" alternative icon for `angular.json` files.

## 2023.4.1 (2023/11/20)
* improve Jenkins support. Thanks **lauragra-y (Laura Gray)** for her contribution!
* add new UI icon variants for Tox folders, Python `.egg-info` folders, Python PyTest cache folders and Python Virtual Environment folders.
* Extra Icons can now override *JPA Buddy* icons in recent IDEs. A new colorful icon theme is available online if you don't like JPA Buddy toolwindows gray icons. See [JPABuddyOldUITheme](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/themes/THEMES.md#jpabuddyolduitheme).
* Extra Icons can now override *Extra ToolWindow Colorful Icons* icons.
* Also added an icon theme for AI Assistant toolwindows icon. See [AIAssistantTheme](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/themes/THEMES.md#aiassistanttheme).
* implement [#170](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/170): when registering a user icon, conditions parameters (except for regex and Facets) should be automatically lower-cased as files and folders paths are also lower-cased before evaluation. Also, the model tester now detects if path contains a *parent folder*, allowing you to test *Parents* condition. The model tester also finds file name when you give a full path.

## 2023.3-231 (2023/11/05)
* important code rework [#166](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/166): improve the way Extra Icons plugin triggers icons refresh and IDE's filename index querying. You should see less `Slow operations are prohibited on EDT` and threading related issues like `...should be accessed only from EDT`.
* fix [#36](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/36): can't override Python icons in PyCharm, Go icons in GoLand, and Tree and List icons in WriterSide.
* try to fix [#165](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/165): `AlreadyDisposedException: Cannot create com.intellij.ui.docking.DockManager because container is already disposed` on IDE or plugin upgrade.
* fix new UI icon variants rendering when overriding IDE icons.
* performance improvement when displaying bundled icons (reduced CPU usage).
* performance improvement when displaying user icons (reduced I/O usage).
* performance improvement when refreshing icons (project views were refreshed 6 times, now reduced to a single refresh).
* support some [Writerside](https://www.jetbrains.com/writerside/) icons: `writerside.cfg`, `*.list` and `*.tree`. Please note Writerside IDE has similar [limitation](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/36) as PyCharm and GoLand IDEs: can't override some Writerside icons in project view. There is no problem with Writerside plugin running in IntelliJ.
* settings UI, fix compatibility with Gateway: some Apache Commons libraries are used by plugin, but they may not be bundled with Gateway.
* settings UI, *Quick action*: the icon group chooser now integrates the **Speed Search** functionality, [similar to Speed Search for Tool Windows](https://www.jetbrains.com/help/idea/speed-search-in-the-tool-windows.html). Select the icon group chooser, type text, and the selection moves to the first item that matches the specified string.
* support Microsoft [Bicep](https://learn.microsoft.com/en-us/azure/azure-resource-manager/bicep/overview) `*.bicep` files.
* rework Backup, Build, Cargo, Codefresh, Deptective, Glowroot, Hadolint, Jar/Dar, JVM config, JSBeautify, JSHint, Kibana, Lerna, Logstash, Muse, MySQL `my.ini`, MySQL Workbench, Nox, PDF, Roadmap, Stylus, To-Do, Tox, Windows executable and Zalando Zappr icons in order to make them compatible with Gateway.
* change default CSV and Video icons.
* add an alternative icon for CSV files.

## 2023.2.12-231 (2023/10/15)
* fix [157](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/157): `java.lang.Throwable: TreeUI should be accessed only from EDT` with Rider 2023.3 EAP 1.
* fix a bug that set `Use IDE filename index` feature to `false` when project-level settings were activated. With this release, this feature will be set to `true` just one time. Feel free to turn it to `false` if wanted (see Extra Icons settings, `Advanced` tab).
* silent some unwanted `Control-flow exceptions` error reports. They appeared on project loading, if IDE failed to load some user icons (for obscure reasons I can't detect nor fix).
* important code rework, which will hopefully prevent more `Slow operations are prohibited on EDT` warnings. I will continue my effort in a next release (I think I found some promising ideas, which would improve stability and performance).
* add a button to detect the correct *Additional UI Scale Factor*.
* settings UI: when registering a user icon, the icon chooser now integrates the **Speed Search** functionality, [similar to Speed Search for Tool Windows](https://www.jetbrains.com/help/idea/speed-search-in-the-tool-windows.html). Select the icon chooser, type text, and the selection moves to the first item that matches the specified string. A future update will bring the same functionality to the icons table.
* settings UI: add a button to reset Extra Icons startup hints.
* add *new UI* icons for Ini, Cfg, Conf, Config, C, C++ and H files.
* support `.gitlab/dependabot.yml` files. Thanks **mateusz-bajorek (Mateusz Bajorek)** for his contribution!
* support OpenTofu `*.tf` and `*.tf.json` files.
* rework Cerebro, Jenkins, Log4j, Logback and `package-lock.json` icons in order to make them compatible with Gateway.
* cleanup: remove unused icons.

## 2023.2.11-231 (2023/09/18)
* fix [156](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/156): important rework of SVG user icons scaling. Most icons should no longer be blurry. Only small SVG icons (with `width` or `height` attributes < 16) will be blurry. For these icons, you are invited to rework them and set higher `width` and `height` values (ideally 16). **Reminder**: if the operating system's **fractional scaling** is activated, please go to plugin's settings, *Advanced* tab, then adjust the *Additional UI Scale Factor* value (ex: for a 125% global scale factor, set it to 1.25). [See details here](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/156#issuecomment-1722545407).
* remove JSVG from dependencies. SVG icons are now handled by JetBrains API directly, which uses its own version of JSVG. 
* improve Markdown icon.
* rework Authors, Asciidoc, Bamboo, Berkshelf, Contact icons in order to make them compatible with Gateway.

## 2023.2.10-231 (2023/09/03)
* fix [151](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/151): NullPointerException in settings form (`pluginIconsSettingsTableModel` is null).
* fix [152](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/152): plugin not compatible with JetBrains Gateway.
* fix support of Caddy (Caddyfile) files.
* add new UI icon for JetBrains Fleet `.fleet` folders.
* rework the way IDE and project level settings are persisted. A limited set of IDE level settings may have been saved at project level (and vice-versa) in the past. This should be fixed now.
* use the new GitLab icon by default instead of the old one.
* settings: try to auto-detect default window scale (`Additional UI Scale Factor` in advanced settings) on new setups. This is useful only if you added user icons or downloaded icon packs. On existing setups, default value is still 1.
* internal: code cleanup, remove compatibility code for unsupported old IDEs. Minor performance improvement.
* info: JetBrains [**Gateway support is in progress**](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/milestone/27). Icons rendering works when plugin is installed in Host, or in both Host and Client, but not in Client only. Also, for now, plugin's settings panels are broken and some icons are too big, but icons override works. I have [plans](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/milestone/27) to fix these issues.
* rework Codacy icon in order to make it compatible with Gateway.
* rework Editorconfig icon in order to make it compatible with Gateway.

## 2023.2.9-231 (2023/08/12)
* rework settings panel.
* experimental: add an option to avoid usage of IDE filename index. Without IDE filename index, Angular, GraphQL and Helm support may be limited, but it may hopefully avoid some errors and warnings when using slow operations in EDT, things like that.
* internal: code rework, avoid potential future issues.
* improve Codacy support.
* support Codeception `codeception.dist.yml` files.
* support JetBrains Fleet `.fleet` folders.
* fix Lighttpd icon rendering on latest IDEs (JetBrains changed their SVG renderer for JSVG, which breaks some SVG files).
* internal: minor performance improvement.

## 2023.2.8-231 (2023/07/12)
* fix [146](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/146): NullPointerException in settings form (`additionalUIScale` is null).
* fix usage of a deprecated API in order to improve compatibility with future IDEs.
* internal: fix unit tests for IDE 2023.2 EAP7.

## 2023.2.7-231 (2023/07/07)
* **improve Git Submodules support**. Git Submodules are now automatically re-detected on every `.gitmodules` file update.
* support Coveralls, Elixir and Sonarqube files. Thanks **JuanDGiraldoM** for his contribution!
* support [Just](https://github.com/casey/just) `justfile` files.
* **you can now choose to use icons *new UI* variants** based on current active UI, or force the use of *old* or *new UI* variants, when they exist. See Extra Icons settings.
* add *new UI* icons for Java Enums.
* add *new UI* icons for Kotlin Enums.
* add *new UI* icons for Exceptions.
* add *new UI* icons for Abstract Exceptions.
* add *new UI* icons for Avro files.
* add *new UI* icons for various ONAP files.
* add *new UI* icons for JPA Buddy, Flyway, Liquibase and TeamCity folders.
* add *new UI* icons for various Helm files and folders.
* add *new UI* icons for various archive files.
* fix: restore missing Helm templates test folder icon.
* fix IDE icons display in settings panel.
* minor performance optimizations.
* just a reminder: *IDE icons* override (which targets a specific IDE icon like Java Enums, not a file path) may work randomly with the new UI. [An issue has been accepted](https://youtrack.jetbrains.com/issue/IDEA-321006) by JetBrains and, I guess, a future IDE update will fix this issue. Unfortunately, I can do nothing at the plugin level. We have to wait for a fix at the IDE level.

## 2023.2.6-231 (2023/06/11)
* fix [137](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/137). Silent `MissingResourceException` errors when trying to find the PSI object for a file. This should have no impact on user experience, as it seemed to happen when displaying files in folders such as `node_modules` only. Files in these folders will show their original icons.
* fix [140](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/140). Important code rework, which should fix some "slow operations are prohibited on EDT" error messages with EAP IDEs.
* fix [141](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/141). Enable antialiasing when displaying SVG user icons.
* fix: don't freeze settings UI when displaying invalid SVG user icons.
* internal: add the ability to create icons for the Old or New UI only. This way, I will be able to provide icons that fit better with the New UI.

## 2023.2.5-231 (2023/05/24)
* rework some SVG icons. File and tab icons were rendered correctly, but icons preview in config panel was a bit altered.
* fix some missing Chinese i18n.
* hide some useless warning messages about IDE filename index issues and icons refreshing. They're still logged, but you will no longer see alerts in your IDE.

## 2023.2.4-231 (2023/05/21)
* fix support of IDE 2023.2 EAP: replaced Apache Batik by JSVG for SVG rendering + some code rework. Batik were interfering with Grazie Lite, leading to its deactivation.
* rework ImgBot, Liquibase, Haxe, Cookie, JavaScript and LGTM SVG icons. IDE 2023.2 EAP raised an error when displaying these icons, or misunderstood some SVG attributes, leading to black&white or blank rendering. JetBrains changed their SVG renderer (replaced Apache Batik by JSVG), which is a bit less functional for now, but lighter.
* important code rework, which will hopefully prevent some weird IDE errors. Additional improvements will come soon.
* I think some annoying bugs have been solved since 2023.2.2 plugin release, which requires a 2023.1+ IDE (due to breaking changes in JetBrains APIs). I will maintain a branch for 2022 IDEs for a few months in order to backport fixes. I will definitively abandon 2022 IDEs once all critical issues have been solved or mitigated. Sorry for not having done it earlier.

## 2023.2.3 (2023/05/09)
* avoid unnecessary error when failed to query IDE filename index. Plugin will try again later.
* support [Vitest](https://vitest.dev/config/) config files.
* support Python Wheel files.

## 2023.2.2 (2023/05/05)
* set minimal IDE version to 2023.1 and fix [#121](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/121): "slow operations are prohibited on EDT". It occurred while querying the IDE filename index in order to detect some specific files (like Angular and Helm related files).
* fix [#126](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/126): "slow operations are prohibited on EDT" when invoking a file selector.
* various minor fixes in settings panels.
* fix support of [Poetry](https://python-poetry.org) files.
* fix model creation/update dialog: user should be allowed to select a single icon, not many.
* internal: some code rework, and improved code quality.
* nota: some *"slow operations are prohibited on EDT"* errors may still be logged (it breaks nothing, these are only warning messages) on EAP IDE builds, when you open a file chooser, while creating a new icon model, or when importing and exporting Icon Packs. You can disable these warnings by following [these instructions](https://plugins.jetbrains.com/plugin/11058-extra-icons/errors-workarounds). A fix will be provided later.

## 2023.2.1 (2023/04/21)
* you can now import and export user icons. See **Icon Pack** in Extra Icons settings. This also means you can easily share icon themes with friends.
* created a downloadable [Icon Pack](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/themes/THEMES.md#downloadable-icon-packs) which restores some old file and folder icons in the new UI.
* implement [#118](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/118): add a custom error reporter which prefills an issue on Extra Icons' GitHub repository.
* fix editor tabs icon reloading on config change.
* fix [#125](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/125): custom icon type errors after enabling Chinese UI. Thanks **Elziy** for helping!  
* fix [#120](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/120): minor UI improvement.
* nota: issue [#121](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/121) "Slow operations are prohibited on EDT" fix will be shipped with 2023.2.2 plugin release, as it requires IDE 2023.1. I wanted to support old IDEs in 2023.2.1 plugin release. This issue occurs mainly in EAP and RC IDE builds, and you will find a safe workaround [here](https://plugins.jetbrains.com/plugin/11058-extra-icons/errors-workarounds).
* improve Chinese UI translation. Thanks **Gerry** for helping!
* support Mermaid files: use the official Mermaid logo instead of the icon displayed by the JetBrains Mermaid plugin.
* support some [Poetry](https://python-poetry.org) files.
* support `pytest.ini` files.
* support `*.cc` and `*.hpp` files.
* add an alternative icon for Composer files.
* add an alternative icon for SVGO files.
* add an alternative icon for Markdown files.
* rework ArchUnit icon (PNG icon to SVG).
* rework Cirrus icon (PNG icon to SVG).
* rework Debian (deb files) icon (PNG icon to SVG).
* rework Elasticsearch icon (PNG icon to SVG).
* rework Gatling icon (based on new logo).
* rework GoCD icon (PNG icon to SVG).
* rework Readme icon.
* rework Redis icon.
* rework Version icon.
* rework a certificate icon (PNG icon to SVG).
* internal: some code rework, and improved code quality.

## 2023.1.2 (2023/03/07)
* support C, C++ and H files.
* plugin's internationalization: provide English and Chinese UI translations. Nota: Chinese translation is automatically applied only if the official IDE [Chinese Language Pack](https://plugins.jetbrains.com/plugin/13710-chinese-simplified-language-pack----) plugin is enabled.
* minor aesthetic bugfix in config panel.
* remove support of Caddy (Caddyfile). IDE 2023.1 EAP crashed loading this icon (SVG file with base64 encoded PNG). This is a known bug in IntelliJ.
* internal: important code rework on SVG rendering.
* [fix #119](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/119) can't display SVG icons with IDE 2023 EAP.

## 2023.1.1 (2023/02/11)
* minor UI improvement in settings panel: add icons to _Enable all... / Disable all..._ combobox items.
* minor UI improvement in Model dialog: hide tester's input label when type is set to IDE icon.
* rework JUnit5 icon (PNG icon to SVG), and add an alternative icon.
* rework LibreOffice icons (PNG icons to SVG). Also added LibreOffice 7.5 [new icons](https://wiki.documentfoundation.org/ReleaseNotes/7.5#Design).
* add alternative icons for MS Office Word, Excel and PowerPoint files.
* add an alternative icon for CSV files.
* add an alternative icon for KeePass files.

## 2023.1 (2023/01/21)
* settings: rework the icon which indicates if an IDE restart is needed.
* internal: clear ExtraIcons internal caches associated to closed projects. It should reduce memory usage (a little).
* internal: important code rework and refactoring, avoid some possible errors.
* support Kustomize `kustomization.yaml` files.
* remove Better Code Hub icons as [Better Code Hub has closed on December 31st 2022](https://github.com/marketplace/better-code-hub).

## 2022.1.14 (2022/12/04)
* [try to mitigate #114](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/114) PHPStorm freezes for a few seconds several times. The JetBrains Angular plugin may cause high CPU usage. Removed usage of the (optional) Angular plugin which was used to detect Angular projects. Angular projects are still detected by using the IDE filename index, so you should still see Angular icons. Unfortunately, you may still experience some IDE freezes: this is an issue with the Angular plugin itself. It seems to be [fixed](https://youtrack.jetbrains.com/issue/WEB-57461) in IntelliJ 2022.3 RC, so you may want to wait for the 2022.3 final release of your IDE, or temporarily disable the Angular plugin.
* internal: upgrade TwelveMonkeys library to 3.9.4 (used to preview and resize PNG icons).
* fix usage of a deprecated API in order to improve compatibility with future 2023 IDEs.
* improve Angular and JS/TS test icons.

## 2022.1.13 (2022/11/11)
* set minimal IDE version to 2022.1 and fix usage of a deprecated API used to query IDE filename index.
* [fix #113](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/113) can't load custom icons from local drive on Windows.
* internal: upgrade TwelveMonkeys library to 3.9.3 (used to preview and resize PNG icons).

## 2022.1.12 (2022/10/15)
* add graphql-resolver, Rego and Mongodb icons. Improve frameworks detection mechanism. Thx to contributor **fcannizzaro**.
* I'm working on tooling and preparing the support for the 2023 IDEs. That's why this changelog will probably be a bit poor until next year. Meanwhile, bugfixes remain my priority, so don't hesitate to raise issues.

## 2022.1.11 (2022/09/16)
* [feat #110](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/110) config panel: add a text input field to test the conditions for User icons.
* [fix #111](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/111) slow operations are prohibited on EDT.
* config panel: minor UI improvements in Plugin icons list.
* fix possible NPE when updating User icons conditions list.
* I think IDE filename index issues are gone! Sorry for this annoying issue, it made me crazy. Big thanks to [Andreas Perhab](https://youtrack.jetbrains.com/issue/IDEA-291382/Assertion-failed-at-VirtualDirectoryImpldoFindChildById#focus=Comments-27-6445738.0-0), who found a solution using an Index Listener. Some explanations: we are not allowed to query the index during indexing. Extra Icons plugin needs to query the index in order to locate some files (for Angular and Helm support) and, unfortunately, the IDE seems to try to update icons (which invokes Extra Icons plugin, which tries to query the index) during indexing. This is not new, but it's more frequent since IJ 2022. Starting from now, we will silent this error, and we query the index a second time, once indexing tasks completed.

## 2022.1.10 (2022/09/05)
* add an option in Extra Icons settings in order to force icons reloading on demand. Use it if you still see errors when querying IDE filename index: wait until indexing is done, go to File, Settings, Appearance & Behavior, Extra Icons, then hit the `Reload projects icons` button. Again, feel free to upvote [IDEA-289822](https://youtrack.jetbrains.com/issue/IDEA-289822), it would help.
* add an alternative Dependabot icon.
* add an alternative Draw.io icon.
* add an alternative Helm icon.
* add an alternative HTTP icon.
* support [YANG](https://network.developer.nokia.com/sr/learn/yang/understanding-yang/) files.
* rework some Docker icons, and fix a Docker Ignore dark icon.
* improve icons reloading on config change.

## 2022.1.9 (2022/08/27)
* support many audio/video files. Thx to contributor **Serial-ATA**.
* try to mitigate errors when querying IDE filename index (will now retry twice before failing). This is a JetBrains issue, **feel free to upvote** [**IDEA-289822**](https://youtrack.jetbrains.com/issue/IDEA-289822), it would help.
* internal: upgrade TwelveMonkeys library to 3.8.3 (used to preview PNG icons).
* rework Haxe icons (PNG icon to SVG).
* improve Dotenv and Cookie file support.
* support `go.mod` files (I am proud of this icon :smile:).
* support GitHub Actions YML files.
* disable plugin's dynamic reloading since it never worked. Plugin uses an IconPathPatcher implementation in order to override IDE icons, which prevents dynamic reloading.

## 2022.1.8 (2022/08/06)
* support Nx `nx.json` files.
* support many audio files. Thx to contributor **Serial-ATA**.
* support Cypress json files.
* add an alternative Prettier icon.
* add an alternative 7zip icon.
* internal: fix usage of some deprecated code.
* don't crash if plugin can't query the IDE filename index, and simply display a notification.
* add an option to ignore plugin's warnings.

## 2022.1.7 (2022/07/16)
* performance improvement.
* implement #100: add buttons to allow reordering of entries in the User Icons list.
* improve support of older IDEs (based on IJ 2021). IDE build number is detected at runtime and code is adapted when needed.
* improve Flyway and Liquibase support.
* support VSCode `*.code-workspace` files.
* support `*.tgz` files.
* support Deno json and jsonc files.
* refactoring and code cleanup.

## 2022.1.6 (2022/06/25)
* improve accuracy and performance of Angular support.
* support various Helm files and folders.
* support Cookie text files.
* support `node_modules` folders.
* add an alternative YAML icon.
* the icons list in the configuration panel now indicates if you need to restart the IDE to see changes.
* some code cleanup.

## 2022.1.5 (2022/06/14)
* fix #99: Angular support (when it looks for `angular.json` files) may freeze the IDE on some specific projects.
* prevent a possible crash with the new IntelliJ UI (currently in preview) for IntelliJ 2022.2+ EAP (222.2889.14+).
* rework Gradle icons.
* add an alternative Gitlab icon (the latest version from the Gitlab website).

## 2022.1.4 (2022/06/04)
* support cache2k xml config files.
* fix #96: rework Angular support.
* add a dedicated color to Angular controller icon.
* rework some Angular icons.

## 2022.1.3 (2022/05/07)
* internal: improve error handling.
* support `requirements.txt` files in Python projects and projects with the Python facet.
* support `lighttpd.conf` files.
* override MDX icon when the MDX plugin is installed.
* fix a Docker icon rendering issue in IJ 2022.1 in the _Services_ tool window and the _Run/Debug Configurations_ dialog.
* fix PostCSS, Sequelize and Tailwindcss support.
* config panel: UI improvements.
* config panel: some icons now have tags, and you can enable and disable multiple icons by tags.
* fix the priority of Angular icons when the Angular plugin is installed.
* add alternative icons for FAQ files, and support HELP files.
* fix #81: rework Angular support.
* support `pnpm-lock.yaml` and `vite.config.ts` files. Thx **iamKyun**.

## 2022.1.2 (2022/04/03)
* rework Babel icon, and provide an alternative icon.
* support more Prettier config files.
* internal: upgrade TwelveMonkeys library to 3.8.2 (used to preview PNG icons).
* improve support of IDE icons override.
* support `werf.yaml` files.
* support `FAQ` files (with `.md`, `.adoc`, `.txt`, `.rst` or no extension).
* support PostCSS, Sequelize, Svelte, Tailwindcss and Nodemon files. **Thx francmarin98**.
* support Commitlint config files.
* internal: some code rework.
* internal: remove usage of some deprecated code (replaced `com.intellij.util.Base64` by JDK11 implementation).
* IDE 2021.1.3 is now the minimal supported version. This is a requirement to enable plugin's dynamic reloading. Previous versions of IDE (IntelliJ and any IntelliJ-based IDE) are affected by a [bug](https://youtrack.jetbrains.com/issue/IDEA-262732) that deletes plugin's settings on dynamic reload. This bug was fixed in IJ 2021.1.3, this is why I finally updated minimal IDE version in order to enable plugin's dynamic reloading safely.
* experimental: plugin updates don't require IDE restart. It will probably apply with next releases. You may still have to restart IDE, I can't test for now, but at least it should be totally safe.

## 2022.1.1 (2022/03/05)
* **INFO**: Extra Icons is now a paid plugin: 5$ per year. Price decreases over time, and it's free for students, teachers and OSS developers. If you don't want to (or can't) support my work, feel free to use [previous releases](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/releases/tag/v1.69).
* add more alternative icons (popular web browsers) for HTML files.
* override YAML icon.
* override Bash file icon.
* support `.tox` folders.
* support `mkdocs.yml` files.
* support JSX files. Thx [koolskateguy89](https://github.com/koolskateguy89).
* support `.github/dependabot.yml` files. Thx [koolskateguy89](https://github.com/koolskateguy89).
* support [_gradle.lockfile_ and _buildscript-gradle.lockfile_](https://docs.gradle.org/current/userguide/dependency_locking.html) files.
* support Eslint files. Thx [francmarin98](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/85).
* add an alternative icon for SVG files. Thx [francmarin98](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/85).
* add an alternative icon for Vite files. Thx [francmarin98](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/85).

## 1.69.0 (2022/01/22)
* you can now override IDE icons by icon name. Per example, you can override the Kotlin Class icon. To find IDE icon names, visit [IntelliJ icons list](https://jetbrains.design/intellij/resources/icons_list/), pick an icon and open the ZIP file: you can use the SVG file name.
* improved support of Kotlin Gradle script icon.
* override Java Enum, Java exception class and Java abstract exception class icons.
* override Kotlin Enum icon.
* override Docker and Docker Compose icons.
* internal: minor code cleanup.
* fix HTML support.

## 1.68.0 (2021/12/28)
* config panel: add missing scrollbar to user icons list.
* config panel: minor UI improvements.
* fix support of Dotenv files ([#79](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/79)).
* add alternative icons for Gradle Kotlin Script files.
* add alternative icons for KeePass files.
* add alternative icons for Javascript (test) files.
* add alternative icons for Typescript (test) files.
* internal: upgrade TwelveMonkeys library to 3.8.1 (used to preview PNG icons).

## 1.67.0 (2021/12/01)
* support HtmlFiles, TypescriptDef, JSConfig, TSConfig, Typescript (test), and Javascript (test) files. Thx [franciscomarin98](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/78).
* add alternative icons for MDX files.
* support vanilla JS files.
* rework JsMap icon.

## 1.66.0 (2021/11/07)
* support CSS and CssMap files. Thx [franciscomarin98](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/76).
* support [jgitver](https://jgitver.github.io/#_maven_configuration) Maven configuration files.
* support `.goreleaser.yml` files.
* add alternatives icons for Kubernetes files.

## 1.65.0 (2021/10/31)
* add an alternative icon for JPA Buddy folders.
* support JsMap files. Thx [franciscomarin98](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/75).
* support PlantUML files.

## 1.64.0 (2021/10/24)
* support more NestJS files.
* add an alternative icon for TODO files.
* fix a typo. Thx [lromanov](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/74).

## 1.63.0 (2021/10/11)
* rework Nginx icon (PNG icon to SVG).
* add an alternative icon for PDF files.
* add an alternative icon for Tox files.
* add an alternative icon for Webpack files.
* support JFR snapshot files.
* support MDX files.
* fix ordering of icons in config panel, to show actual ordering priorities.
* support NestJS files (you may want to disable Angular support as both frameworks share some file patterns).

## 1.62.0 (2021/09/11)
* temporarily reintroduce IntelliJ internal code (SVGLoader) to load SVG user icons. It provides better rendering of SVG icons than TwelveMonkeys (used as a ImageIO plugin), but we should avoid usage of internal code. TwelveMonkeys is still used for PNG user icons (higher compatibility).
* performance improvements, especially in large projects. Thx [AlexPl292](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/68) for his investigations.
* icons table: you can now find the icons that are enabled or not enabled by typing "yes" or "no" in the regex filter.
* rework Storybook support: revert 1.61 patch and improve Storybook detection.

## 1.61.0 (2021/08/29)
* improve support of Storybook: include `*.jsx` and `*.tsx` files. Warning: the TSX extension is already associated to _Typescript + React_ files. You can deactivate the unwanted association in Extra Icons settings (tip: filter the icons table with "tsx", then disable _Storybook TSX_ or _Typescript + React_).
* support [Screwdriver](https://docs.screwdriver.cd) `screwdriver.yaml` files.
* support JPA Buddy `.jpb` folders.
* rework a README icon (PNG icon to SVG).
* add an alternative icon for README files.
* you can now reuse bundled icons when creating user rules.
* internal: code cleanup.

## 1.60.0 (2021/08/07)
* config panel: the regex filter is applied as you type.
* internal: fix usage of deprecated code.
* rework Android icon.
* support [Nox](https://nox.thea.codes/en/stable/config.html) `noxfile.py` files.

## 1.59.0 (2021/07/30)
* support `AndroidManifest.xml` files.
* support `proguard-rules.pro` files.
* support Maven Shade Plugin `dependency-reduced-pom.xml` files.

## 1.58.0 (2021/07/17)
* improve Dockerfile support.
* improve Backup files support.
* support DLL files.

## 1.57.0 (2021/07/01)
* minimal IDE version is now 2020.3.
* add alternative icons for MS Office files (MS Office 2019 SVG icons).
* improve rendering quality of user SVG icons.

## 1.56.0 (2021/06/20)
* rework Draw.io icon and add an alternative icon.
* rework Dependabot icons (PNG icon to SVG).
* support Stylus (browser extension) `*.styl` files.
* support Mergify YML files.
* support `.*\.egg-info` folders.
* internal: remove usage of code deprecated in IJ 2021.2.
* internal: minor refactoring.

## 1.55.0 (2021/05/29)
* rework support of `*.spec.tsx` files.
* support `Caddyfile` files.

## 1.54.0 (2021/05/15)
* improve rendering of custom PNG icons.
* support [Vite](https://vitejs.dev) `vite.config.js` files.
* support [Muse](https://docs.muse.dev) `.muse.toml` files.
* support [Hadolint](https://github.com/hadolint/hadolint) `.hadolint.y(a)ml` files.

## 1.53.0 (2021/05/08)
* improve Storybook support.
* experimental: try to fix `ArrayIndexOutOfBoundsException: Coordinate out of bounds!` error when loading some custom PNG icons.
* internal: removed dependencies to IJ internal code used to load custom SVG icons. Replaced by TwelveMonkeys + Apache Batik libraries. This way, future IDE updates won't break this plugin.

## 1.52.0 (2021/05/03)
* fix Dotenv files support. Thx Philipp Voronin.
* add an alternative icon for TODO files.
* add an alternative icon for CodeShip files.
* support TSX files.
* support `jitpack.yml` files.
* improve Storybook support.

## 1.51.0 (2021/04/20)
* add an alternative icon for EditorConfig files.
* add an alternative icon for Binary files.
* add an alternative icon for Contribution files.
* support Storybook files and folders. Thx [Gaëtan Maisse](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/53).
* rework Postman support.
* support GoLang Arch Linter `.go-arch-lint.yml` files.

## 1.50.0 (2021/04/06)
* rework `.keep`, `.gitkeep`, `.hgkeep`, `.svnkeep` icon.
* rework Python Virtual Environment folders.
* rework Pytest folders.
* rework all folder SVG icons: use opacity instead of light/dark gray colors.
* support Python `venv` Virtual Environment folders. This is a less common name than `.venv`, but some users may prefer this alternative.
* support SVGO 2 `svgo.config.js` files.
* internal: migrate to [SVGO](https://github.com/svg/svgo) 2 (a tool to optimize SVG files): all SVG icons are optimized again with new default settings and some configuration.

## 1.49.0 (2021/03/24)
* improve Babel support. Thx [Alan Bouteiller](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/51).
* support Python `.venv` Virtual Environment folders.
* support `.pytest_cache` folders.

## 1.48.0 (2021/03/20)

Thx [Alan Bouteiller](https://github.com/bouteillerAlan) for his contributions:

* support Babel config files.
* support Expo files and folders.
* support Metro config files.

## 1.47.0 (2021/03/05)
* support Lerna configuration file: `lerna.json`. Thx [Yoav Vainrich](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/46).

## 1.46.1 (2021/02/14)
* try to fix [#39](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/39): error badge saying project is disposed.
* fix [#40](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/40): EDT warning when creating a custom icon model.
* improve Webpack support. Thx [Yoav Vainrich](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/45).

## 1.46.0 (2021/02/07)
* support `.env` files.
* force IDE to restart when installing or upgrading Extra Icons plugin: there may be a bug with [Dynamic Plugins](https://plugins.jetbrains.com/docs/intellij/dynamic-plugins.html). I hope it will fix [#44](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/44): some settings were lost when hot-reloading plugin (upgrade without restart).

## 1.45.1 (2021/02/02)
* fix a NPE that may occur when multiple projects are opened and icons are being refreshed.

## 1.45.0 (2021/01/31)
* internal: minor code cleanup.
* rework a help message about icons priority.
* handle non-square SVG images (backport from [Icon Viewer 2](https://github.com/jonathanlermitage/IconViewer) plugin).
* rework GraphQL files.
* rework WAR files.
* provide an alternative icon for IDEA MindMap plugin files.

## 1.44.0 (2021/01/16)
* internal: minor code optimization.
* support [Semaphore](https://semaphoreci.com) `.semaphore` folders and `.semaphore/semaphore.yml` files.
* support `*.bin` files.
* support LGTM yml files.
* support [MPS](https://www.jetbrains.com/mps/) `.mps` folders.
* support [Renovate](https://docs.renovatebot.com/configuration-options/) files.

## 1.43.0 (2020/12/16)
* support Git Submodules: `.gitmodules` files can be parsed to customize icon of Submodules folders. Nested Submodules are also supported.
* provide some alternative icons for PDF files.
* provide an alternative icon for JVM files.
* adjust the red color of Git files to make them less eye-catching.

## 1.42.0 (2020/12/12)
* support `RELEASENOTES` files.
* fix [#43](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/43): restore compatibility with IDE running on JRE8.
* apply a workaround for a bug in IJ 2020.3 that makes IDE loading icons from bundled plugins (like Lombok) instead of ExtraIcons.
* rework Lombok files (PNG icon to SVG).

## 1.41.0 (2020/11/30)
* provide some alternative icons for `.travis.yml` files ([official logos](https://travis-ci.com/logo)).
* support `.coveragerc` files.
* rework TypeScript files.
* rework CHANGELOG dark icon.
* provide alternative icons for Dockerfile, Docker Compose and Docker Ignore files.

## 1.40.0 (2020/11/15)
* support [All Contributors](https://github.com/all-contributors/all-contributors) `.all-contributorsrc` files.
* provide an alternative icon for Contribution files.

## 1.39.0 (2020/10/30)
* support [Mockk](https://mockk.io/#settings-file) `io/mockk/settings.properties` files. 
* support [gradle-intellij-plugin](https://github.com/JetBrains/gradle-intellij-plugin) `idea-sandbox` folders.
* internal: improve Gradle build performance.
* internal: migrate Gradle build to Kotlin DSL.
* add an option to adjust UI Scale Factor for user icons. Useful if you run IDE with `-Dsun.java2d.uiScale.enabled=false` flag.
* support `KNOWN_ISSUES` and `OPEN_ISSUES` files.
* settings: minor UI improvements.

## 1.38.0 (2020/09/30)
* rework `package-info.java` files.
* support `module-info.java` files.
* provide some alternative icons for `AUTHORS` files.
* support `netlify.toml` files.
* rework Cassandra files (PNG icon to SVG).

## 1.37.1 (2020/09/13)
* bugfix [#39](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/39): Exception when plugin tries to load icons in a project that is being disposed or is already disposed.

## 1.37.0 (2020/09/12)
* bugfix: NPE while refreshing project panel icons.
* support Restyled `.restyled.yaml` files.
* provide an alternative icon for Robot Framework files (based on GitLab Robot icon).
* support Weblate `.weblate` files.

## 1.36.0 (2020/08/30)
* rework Markdown files (PNG icon to SVG) and provide 2 alternative icons. To use an alternative icon, simply deactivate the other(s).
* provide some alternative icons for Video files.
* provide an alternative icon for EditorConfig files.
* provide an alternative icon for Swagger files.
* provide an alternative icon for Asciidoc files.
* provide an alternative icon for Certificate files.
* provide an alternative icon for License files.
* provide an alternative icon for Readme files (based on GitHub Readme icon).
* add new configuration condition: facets. You can now choose to activate a custom icon if a specific facet is activated in project (as in `Project Structure > Project Settings > Facets`), like "Spring", "JPA", etc. This condition must be associated with one other condition like "Names", "Regex", etc.
* removed usage of `sun.awt.image.ToolkitImage`. It will fix some Gradle build errors with JDK9+, but some IDE like PyCharm *may* (I did not reproduce) not display a few icons. If it doesn't work for you, please open an issue (and attach IDE logs) and I will probably revert this change.
* support [Tinylog](https://tinylog.org) `tinylog.properties` files.

## 1.35.0 (2020/08/22)
* rework Nginx files.
* rework Better Code Hub files.

## 1.34.0 (2020/08/08)
* rework Git files (PNG icon to SVG).
* rework Gradle files (PNG icon to SVG).
* rework Archive files.
* rework Maven wrapper files (PNG icon to SVG).
* rework Gradle wrapper files (PNG icon to SVG).
* rework Gitlab files (PNG icon to SVG).
* rework Apache htaccess files.
* rework HTML5 files.
* rework PID files.
* improve FlyWay and Liquibase support: detect HSQLDB, H2 and IBM DB2 databases.
* rework Angular files.
* rework Appveyor files.
* rework Asciidoc files.
* rework GitHub files.
* rework SASS files (PNG icon to SVG).
* rework Terraform files (PNG icon to SVG).
* rework Typescript files.
* rework Liquibase files.
* rework Swagger files.
* support PDD (Puzzle Driven Development) `.pdd` and `.0pdd.yml` files.
* rework and ungroup `*.ini`, `*.cfg`, `*.conf`, `*.config` Configuration files support: users may prefer icons provided by the [Ini plugin](https://plugins.jetbrains.com/plugin/6981-ini/).
* rework Postman files (improve dark icon).
* rework Codecov files (PNG icon to SVG).
* rework `.mailmap` files (PNG icon to SVG).
* rework LESS files (PNG icon to SVG).
* support Velocity `*.vtl` files.
* rework CHANGELOG files (PNG icon to SVG).
* support Jinja `*.jinja` and `*.jinja2` files.
* Plugin icons table filter now filters on label column only.
* rework Jest files.
* rework WAR files.
* rework TESTING files (PNG icon to SVG).
* rework `.form` files (PNG icon to SVG).
* rework NSIS files (PNG icon to SVG).
* support `swagger-config.yaml` files.

Some icons come from the [Hiberbee Theme family](https://github.com/hiberbee/jetbrains-ide-theme) project.

## 1.33.0 (2020/07/31)
* support Robot Framework `.robot` files.
* try to fix [issue #32](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/32): Markdown file icons are overridden by Markdown Navigator plugin.
* add a filter to Plugin icons table.
* rework `.htaccess` files (PNG icon to SVG).
* rework `kubernetes*.yml` files (PNG icon to SVG).
* rework `*.toml` files (PNG icon to SVG).
* rework Vagrant files (PNG icon to SVG).
* rework Puppet files (PNG icon to SVG).
* rework Privacy files.
* rework Security files.
* rework Redis files (PNG icon to SVG).
* support `(.)version(s).xml` files.

## 1.32.0 (2020/07/11)
* improve VERSION files detection.
* rework ONAP files: files generated by CBA enrichment now use gold color.

## 1.31.0 (2020/07/05)
* improve CHANGELOG and VERSION files detection.
* support `.gitreview` files.
* support some [ONAP Controller Blueprint Archive (CBA)](https://wiki.onap.org/display/DW/Modeling+Concepts) files. Full ONAP support will be implemented in a separate plugin.

## 1.30.0 (2020/06/18)
* rework Bash terminal icon.
* rework Windows terminal icon.
* support `hibernate.properties` files.
* support [Rultor](http://www.rultor.com) `.rultor.yml` files.

## 1.29.0 (2020/06/10)
* support `.teamcity` folder.
* support `.exe` files.
* support NuGet `.nuget` folder and `.nuget/packages.config` file.
* support [Tox](https://github.com/tox-dev/tox) `tox.ini` files.
* rework `LICENSE` icon.
* rework `NOTICE` icon.
* rework certificate icon.
* rework Gradle folder icon.
* restore missing Deptective icon. Probably dropped by mistake.
* improve Config files support.
* improve Microsoft Azure support.

## 1.28.0 (2020/05/23)
* support Composer `composer.json` and `composer.lock` files.
* *IDE 2020.1+ (201.5239+ builds):* implement [#28](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/28) show customized icons in Repositories tool window.

## 1.27.0 (2020/05/15)
* support `svgo.yml` files.
* optimized SVG logos with [SVGO](https://github.com/svg/svgo).
* fix [issue 35](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/35): clarify usage of regex. 
* support some [RST](https://en.wikipedia.org/wiki/ReStructuredText) files: `README.rst`, `CHANGELOG.rst`, etc.
* support `.run` folder used by IntelliJ IDEA 2020+ to store Run/Debug Configurations.
* support [Draw.io](https://app.diagrams.net) `.drawio` and `.dio` files.

## 1.26.0 (2020/05/01)
* settings: refresh project views on icons list update.
* support Prettier `.prettierignore` files.
* support Scalingo `scalingo.json` files.
* support Snapcraft `snapcraft.yaml` files.
* support `SECURITY` files (with `.md`, `.adoc`, `.txt` or no extension).
* support Bitnami Stacksmith `stackerfile.yml` files.
* support [Karate](https://github.com/intuit/karate) `karate-config.js` files.
* support [Crowdin](https://crowdin.com) `.crowdin.yml` files.
* rework 16x16 icon for `package-info.java` files.
* improve FlyWay and Liquibase support: detect some databases (MariaDB, MySQL, Oracle, PostgreSQL, Sqlite, MS SQL Server) in FlyWay and Liquibase locations path, e.g., customize `mysql` folder icon in `db/migration/mysql/`.

## 1.25.0 (2020/04/18)
* support Epub `.epub`, `.mobi`, `.azw`, `.azw3` files.
* support [Rollup](https://github.com/rollup/rollup) `rollup.config.js` files.
* support Prettier `.prettierrc` files.

## 1.24.0 (2020/04/10)
* support Webpack `webpack.config.js` files.
* support Gitpod `.gitpod.yml` files.

## 1.23.0 (2020/03/28)
* replace Travis PNG icon by SVG.
* new plugin icon.

## 1.22.0 (2020/03/19)
* internal: fix usage of a `UIUtil.createImage` method deprecated in IDE 193.
* internal: log (at INFO level) some ignored errors while loading icon.
* rework Browserslist icon.

## 1.21.1 (2020/03/13)
* fix background color of regex field with non-dark theme.

## 1.21.0 (2020/02/27)
* fix model type selection when edit dialog is opened. Thx [Florian Böhm](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/25).
* support Liquibase files. Thx [Mateusz Bajorek](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/24).

## 1.20.2 (2020/02/27)
* restore missing Angular icon.

## 1.20.1 (2020/02/26)
* fix [issue 26](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/26): can't open the Extra Icons settings if Angular plugin is disabled (or not installed, so IJ Community was affected too). Thx contributors!

## 1.20.0 (2020/02/21)
* user can now add custom icons, path ignore regex now works on relative path to project base dir. Thx [Florian Böhm](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/21).

## 1.19.0 (2020/02/13)
* add an option to ignore file/folder pattern.
* rework MySQL Workbench dark icon.
* support `.bak` files.

## 1.18.0 (2020/02/08)

* make Angular regexes more strict. Thx [Bert Verhelst](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/20).

Thx [Florian Böhm](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/19) for his contributions:

* settings can now be overridden on project level.
* improve Angular files detection.
* improve Gradle Wrapper files detection.
* support Browserslist `browserslist`, `.browserslistrc` files.
* replace Typescript and SASS PNG icons by SVG.
* internal improvements.

## 1.17.0 (2020/01/31)
* make settings changes apply without having to restart. Thx [Florian Böhm](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/18).
* fix Angular icons size. Thx [Florian Böhm](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/17).

## 1.16.0 (2020/01/01)
* support Bitbucket `bitbucket-pipelines.yml` files.
* support Grafana `grafana.ini` files.
* support Prometheus `prometheus.yml` files.
* rework SVG file icon.

## 1.15.0 (2019/12/07)
* improve Logstash detection.
* support `.jceks` files.
* support MySQL Workbench `.mwb` files.

## 1.14.0 (2019/11/23)
* rework `.idea` folder icon.

## 1.13.0 (2019/11/07)
* support Appveyor `.appveyor.yml` files.

## 1.12.0 (2019/10/23)
* support `.pid` files.
* support `.jar.original` files.
* support `.npmrc` files.
* support `.npmignore` files.
* support `jest.config.js` files.
* support `karma.conf.js` files.
* rework many icons.

## 1.11.0 (2019/10/10)
* improve AngularJS files support. Thx [diakovidis](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/16).

## 1.10.0 (2019/10/03)
* support Postman `*postman_collection.json`, `*postman_environment.json` files.
* support `.swagger-codegen-ignore` files.
* support Diffusion Archive / XL Deploy Archive `*.dar` files.

## 1.9.0 (2019/09/22)
* rework `azure-pipelines.yml` file icon (use official SVG icon).
* support `.p12` files.

## 1.8.0 (2019/09/09)
* support `.keep`, `.gitkeep`, `.hgkeep`, `.svnkeep` files used by various VCS.
* improve Logback detection.
* support `.pubkey` files.
* support Postman `*.postman.json` files.
* restore support of backup `*.versionBackup`, `*.back`, `*.backup`, `*.old`, `*.prev`, `*.revert` files. Probably dropped by mistake.
* rework `.mvn` folder icon.

## 1.7.0 (2019/07/20)
* support [Glowroot](https://glowroot.org) `glowroot/admin.json`, `glowroot/config.json` files.

## 1.6.0 (2019/06/15)
* support [ImgBot](https://imgbot.net) `.imgbotconfig` files.
* support `*.xz` archive files.
* support `jvm.config` files.
* support Avro `*.avsc` files.
* use icon from [IntelliJ Rust](https://github.com/intellij-rust/intellij-rust) for `cargo.toml` files (a future update will introduce a white-list to ignore some file patterns).
* rework Docker-compose files: Docker files remain blue, and Docker-compose files become pink.

## 1.5.1 (2019/05/25)
* revert changes to folders detection (1.5.0) to fix [#13](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/13): StringIndexOutOfBoundsException and NullPointerException errors.

## 1.5.0 (2019/05/24)
* rework Gradle files.
* rework folders detection.
* support `/docker` folders.
* restore missing `test-ts.svg` icon in [ide173](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/tree/ide173) branch.

## 1.4.0 (2019/05/18)
* support [Better Code Hub](https://bettercodehub.com) `.bettercodehub.yml` files. I'd like to thank the Better Code Hub team for sending me SVG files!
* support FlyWay SQL files `.*/db/migration/.*\.sql`.
* support APK `.apk`, `.xapk` files.
* detect more Docker files.
* introduce regex support.
* switched from DevKit to Gradle workflow: you can now build plugin without IDE, and integration with online CI like Travis will be easy.

## 1.3.0 (2019/04/24)
* support Microsoft Azure `azure-pipelines.yml` files.
* support Dependabot `.dependabot/config.yml` files and `.dependabot` folders.
* fix: Windows `mvnw.bat`, `mvnw.cmd` and Linux `mvnw` files were registered with the same id. That means disabling Windows icons disabled Linux icon too. 

## 1.2.0 (2019/04/12)
* improve file detection capabilities: can work with folders.
* improve CircleCI detection: add `.circleci/config.yml` files.
* support `Gruntfile.js` files.
* support LESS `*.less` files.
* support Visual Studio Code `.vscode/settings.json` files.
* support `intellijcodestyle.xml` files.
* support `.circleci`, `.github`, `.gitlab`, `gradle`, `.idea`, `.mvn`, `.vscode` folders.
* rework SASS `*.sass`, `*.scss` files. Also, they don't need *Sass support* plugin anymore.
* rework `.dockerignore` files.

## 1.1.0 (2019/04/05)
* rework setting table's vertical scrolling.
* fix detection of modifications in setting table.
* add plugin's icon (effective with 2019.1 IDE).

## 1.0.0 (2019/04/01)
* add a graphical config panel to select extra icons to (de)activate. See `Settings > Appearance & Behavior > Extra Icons`.
* changed version numbers: `x.y.z.173` plugins are compatible with 173.0+ IDE builds (2017.3 and newer), `x.y.z.183` plugins are compatible with 183.0+ IDE builds (2018.3 and newer) and provide some additional icons.

## 0.27 and 0.28 (2019/03/29)
* support `*.http` files (requests to be played by IntelliJ HTTP client).
* plugin's configuration: you can now select extra icons to deactivate via a config file. A future release will bring a graphical config panel (but contributions are welcome!).

## 0.25 and 0.26 (2019/02/17)
* fixed ArchUnit files detection.
* support GraphQL `*.graphqls`, `graphql.schema.json`, `graphql.config.json` files.
* support `jvm.properties` files.
* support JUnit `junit-platform.properties` files.
* support SVG `*.svg` files.
* support `*.3g2`, `*.3gp`, `*.avi`, `*.divx`, `*.f4a`, `*.f4b`, `*.f4p`, `*.f4v`, `*.flv`, `*.m4p`, `*.m4v`, `*.mkv`, `*.mp4`, `*.mpg`, `*.mpeg`, `*.mov`, `*.ogm`, `*.ogv`, `*.vob`, `*.webm`, `*.wmv`, `*.xvid` video files.

## 0.23 and 0.24 (2019/01/19)
* support [ArchUnit](https://github.com/TNG/ArchUnit) `archunit.properties` files.
* support [Deptective](https://github.com/moditect/deptective) `deptective.json` files.
* improved Jenkins file detection accuracy.

## 0.21 and 0.22 (2019/01/10)
* rework Haxe `*.hxml` files (new icons).
* support `package-info.java` files.
* support NSIS `*.nsi` files.
* improved file detection accuracy.

## 0.19 and 0.20 (2018/12/21)
* starting from 0.19, there are two builds:
  * odd minor revision number (0.19, 0.21, 1.1, 1.3...): compatible with 173.0 IDE builds (aka 2017.3). This build doesn't bundle features that need 2018.3 IDE builds: AngularJS, SASS, Javascript. They're excluded because Extra Icons plugin reads project's type in order to activate some file recognition (AngularJS, SASS, Javascrip): it is based on 2018.3 IDE features. Other files detection is simply based on files pattern, that's why it works with older IDE builds, and I will maintain a branch (`ide173`) to keep support.
  * even minor revision number (0.20, 0.22, 1.0, 1.2...): compatible with latest IDE builds (183.0, aka 2018.3).
  
This way, you simply have to download the latest version offered by the plugin manager: on older IDE, you'll get the latest odd minor revision number. On recent IDE, you get the latest even minor revision number that ships same features as odd version, plus features that comes with recent IDE builds.

## 0.18 (2018/12/10)
* plugin is no more compatible with 173.0 IDE builds: minimum version is now 183.0 (2018.3).

## 0.17 (2018/12/08)
* support `gradle.properties`, `LISEZMOI.*` files.
* support AngularJS `*.module.ts`, `*.component.ts`, `*.service.ts`, `*.pipe.ts`, `*.directive.ts`, `*.guard.ts` files (optional, needs *AngularJS* plugin from IntelliJ Ultimate, WebStorm, PhpStorm). Enabled in AngularJS projects only. Thx to Edoardo Luppi.
* support SASS `*.sass`, `*.scss` files (optional, needs *Sass support* plugin) Thx to Edoardo Luppi.
* support [Haxe](https://haxe.org) `*.hx`, `*.hxml` files.
* support [Cerebro](https://github.com/lmenezes/cerebro) `cerebro*.conf` files.

## 0.16 (2018/11/15)
* support Gatling `gatling*.conf` files.
* support Lombok `lombok.config` files.
* support `kubernetes*.yml` files.

## 0.15 (2018/11/03)
* improved file detection accuracy.
* support Cassandra `cassandra*.yml`, Redis `redis*.conf` files.

## 0.14 (2018/10/28)
* support [Codacy](https://support.codacy.com/hc/en-us/articles/115002130625-Codacy-Configuration-File) `.codacy.yaml`, `.codacy.yml` files.
* support Elasticsearch `elastic*.yml`, Logstash `logstash*.yml`, Kibana `kibana*.yml` files.

## 0.13 (2018/10/21)
* detect more Docker and Docker Compose files.
* detect more Jenkins files.
* support [Zappr](https://zappr.opensource.zalan.do) `.zappr.yaml` files.
* support [Codecov](https://docs.codecov.io/docs/codecov-yaml) `codecov.yml`, `.codecov.yml` files. 
* support `.dockerignore` files.

## 0.12 (2018/10/10)
* rework Gitlab files (reduced margin by 1px).
* rework Maven and Gradle wrapper files (new icons).
* rework `package.json`, `package-lock.json` files (new icons).
* support [Terraform](https://www.terraform.io) `*.hcl`, `*.tf`, `*.tf.json` files.
* support [Cirrus](https://cirrus-ci.org) `.cirrus.yml` files.
* support `*.rpm`, `*.deb`, `*.zip`, `*.7z`, `*.tar`, `*.gz`, `*.bz2` files.
* support `bower.json`, `.bowerrc`, `.jsbeautifyrc`, `.jshintrc` files.

## 0.11 (2018/09/29)
* support `*.jks`, `BUILD`, `BUILDING`, `PRIVACY`, `ROADMAP`, `CONTRIBUTE`, `CONTRIBUTION`, `TODO`, `TEST`, `TESTING`, `LICENSE_INFO`, `ADDITIONAL_LICENSE_INFO` files.

## 0.10 (2018/09/22)
* rework `*.iml`, `*.war` files.
* support `nginx.conf`, `.htaccess`, `puppet.conf`, `my.ini`, `*.ini`, `*.cfg`, `*.conf`, `*.toml`, `*.asciidoc`, `*.versionsbackup` files.

## 0.9 (2018/09/15)
* rework `LICENSE`, `berksfile.lock` files.
* support `log4j`, `log4j-test`, `logback`, `logback-test` files with `.xml`, `.yml`, `yaml`, `.properties`, `.json` extensions.
* support `*.csv`, `*.md`, `*.adoc`, `*.cert`, `*.war`, `*.form` extensions.
* support backup `*.versionBackup`, `*.back`, `*.backup`, `*.old`, `*.prev`, `*.revert` files.

## 0.8.1 (2018/09/10)
* rework Microsoft Word, Excel, PowerPoint, OneNote, Visio, Project files (added dark theme).
* rework `LICENSE`, `appveyor.yml`, `*.kdbx` files.

## 0.8 (2018/09/09)
* some code refactoring.
* rework `*.iml`, `.travis.yml`, `jenkins`, `jenkinsfile`, `NOTICE` files.
* support `.gitmodules`, `*.pdf`, `.mailmap`, `*.kdbx` (KeePass), `version` (with `.md`, `.adoc`, `.txt` or no extension) files.
* support `*.pem`, `*.crt`, `*.ca-bundle`, `*.cer`, `*.p7b`, `*.p7s`, `*.pfx` certificate files.
* support `*.doc`, `*.docx`, `*.xls`, `*.xlsx`, `*.ppt`, `*.pptx`, `*.one`, `*.onetoc2`, `*.vsd`, `*.vsdx`, `*.vss`, `*.vssx`, `*.vst`, `*.vstx`, `*.mpd`, `*.mpp`, `*.mpt` Microsoft Word, Excel, PowerPoint, OneNote, Visio, Project files.
* support `*.odt`, `*.ods`, `*.odp`, `*.odg` , `*.odf` LibreOffice Writer, Calc, Impress, Draw, Math files.
* increase plugin's icons priority. This way, IDE will prefer plugin's icons instead of default *unknown file types* icon for some file types.

## 0.7 (2018/09/06)
* support `vagrantfile`, `*.iml`, `.gitattributes`, `.gitignore`, `.editorconfig`, `berksfile`, `berksfile.lock`, `dockerfile`, `docker-compose.yml` files.

## 0.6 (2018/09/02)
* rework `.gitlab-ci.yml`, `appveyor.yml`, `NOTICE` files.
* support `*.jar` files.

## 0.5 (2018/09/01)
* some code refactoring.
* support `bamboo.yml`, `circle.yml`, `codeship-steps.yml`, `codeship-steps.json`, `codefresh.yml`, `.gocd.yaml`, `package.json`, `package-lock.json`, files.
* support `jenkins`, `NOTICE`, `CONTACT` files (with `.md`, `.adoc`, `.txt` or no extension).

## 0.4 (2018/08/26)
* enabled compatibility with all products (WebStorm, etc.).
* rework Maven wrapper `mvnw`, Gradle wrapper `gradlew` files.
* support `README`, `CHANGELOG`, `CHANGES`, `LICENSE`, `COPYING`, `CONTRIBUTING`, `AUTHORS` files (with `.md`, `.adoc`, `.txt` or no extension).

## 0.3 (2018/08/25)
* support Windows script `*.cmd`, `*.bat`, Powershell `*.ps1`, Bash `*.sh`, Maven wrapper `mvnw`, Gradle wrapper `gradlew` files.

## 0.2 (2018/08/25)
* support Jenkins `jenkinsfile`, Gitlab `.gitlab-ci.yml` files.

## 0.1 (2018/08/23)
* support Travis `.travis.yml`, Appveyor `appveyor.yml` files.
