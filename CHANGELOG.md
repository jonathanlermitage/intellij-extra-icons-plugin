# Extra Icons Change Log

## 2023.2.12-231 (WIP)
* fix calculation of global scale factor. Now, it's accurate on Windows/Linux/macOS. As a consequence, in plugin's settings, *Advanced* tab, the *Additional UI Scale Factor* parameter will be reset a single time with the correct value.
* add a button to detect the correct *Additional UI Scale Factor*.
* add *new UI* icons for Ini, Cfg, Conf, Config, C, C++ and H files.
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
  
This way, you simply have to download the latest version offered by the plugin manager: on older IDE, you'll get the latest odd minor revision number. On recent IDE, you get the latest even minor revision number that sheeps same features as odd version, plus features that comes with recent IDE builds.

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
* enabled compatibility with all products (WebStorm, etc).
* rework Maven wrapper `mvnw`, Gradle wrapper `gradlew` files.
* support `README`, `CHANGELOG`, `CHANGES`, `LICENSE`, `COPYING`, `CONTRIBUTING`, `AUTHORS` files (with `.md`, `.adoc`, `.txt` or no extension).

## 0.3 (2018/08/25)
* support Windows script `*.cmd`, `*.bat`, Powershell `*.ps1`, Bash `*.sh`, Maven wrapper `mvnw`, Gradle wrapper `gradlew` files.

## 0.2 (2018/08/25)
* support Jenkins `jenkinsfile`, Gitlab `.gitlab-ci.yml` files.

## 0.1 (2018/08/23)
* support Travis `.travis.yml`, Appveyor `appveyor.yml` files.
