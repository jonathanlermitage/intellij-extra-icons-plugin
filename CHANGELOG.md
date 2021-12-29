# Extra Icons Change Log

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
* add new configuration condition: facets. You can now choose to activate a custom icon if a specific facet is activated in project (as in `Project Structure > Project Settings > Facets`), like "Spring", "JPA", etc. This condition must be associated with an other condition like "Names", "Regex", etc.
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
* reworked Haxe `*.hxml` files (new icons).
* support `package-info.java` files.
* support NSIS `*.nsi` files.
* improved file detection accuracy.

## 0.19 and 0.20 (2018/12/21)
* starting from 0.19, there are two builds:
  * odd minor revision number (0.19, 0.21, 1.1, 1.3...): compatible with 173.0 IDE builds (aka 2017.3). This build doesn't bundles features that need 2018.3 IDE builds: AngularJS, SASS, Javascript. They're excluded because Extra Icons plugin reads project's type in order to activate some file recognition (AngularJS, SASS, Javascrip): it is based on 2018.3 IDE features. Other files detection is simply based on files pattern, that's why it works with older IDE builds, and I will maintain a branch (`ide173`) to keep support.
  * even minor revision number (0.20, 0.22, 1.0, 1.2...): compatible with latest IDE builds (183.0, aka 2018.3).
  
This way, you simply have to download the latest version offered by the plugins manager: on older IDE, you'll get the latest odd minor revision number. On recent IDE, you get the latest even minor revision number that sheeps same features as odd version, plus features that comes with recent IDE builds.

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
* reworked Gitlab files (reduced margin by 1px).
* reworked Maven and Gradle wrapper files (new icons).
* reworked `package.json`, `package-lock.json` files (new icons).
* support [Terraform](https://www.terraform.io) `*.hcl`, `*.tf`, `*.tf.json` files.
* support [Cirrus](https://cirrus-ci.org) `.cirrus.yml` files.
* support `*.rpm`, `*.deb`, `*.zip`, `*.7z`, `*.tar`, `*.gz`, `*.bz2` files.
* support `bower.json`, `.bowerrc`, `.jsbeautifyrc`, `.jshintrc` files.

## 0.11 (2018/09/29)
* support `*.jks`, `BUILD`, `BUILDING`, `PRIVACY`, `ROADMAP`, `CONTRIBUTE`, `CONTRIBUTION`, `TODO`, `TEST`, `TESTING`, `LICENSE_INFO`, `ADDITIONAL_LICENSE_INFO` files.

## 0.10 (2018/09/22)
* reworked `*.iml`, `*.war` files.
* support `nginx.conf`, `.htaccess`, `puppet.conf`, `my.ini`, `*.ini`, `*.cfg`, `*.conf`, `*.toml`, `*.asciidoc`, `*.versionsbackup` files.

## 0.9 (2018/09/15)
* reworked `LICENSE`, `berksfile.lock` files.
* support `log4j`, `log4j-test`, `logback`, `logback-test` files with `.xml`, `.yml`, `yaml`, `.properties`, `.json` extensions.
* support `*.csv`, `*.md`, `*.adoc`, `*.cert`, `*.war`, `*.form` extensions.
* support backup `*.versionBackup`, `*.back`, `*.backup`, `*.old`, `*.prev`, `*.revert` files.

## 0.8.1 (2018/09/10)
* reworked Microsoft Word, Excel, PowerPoint, OneNote, Visio, Project files (added dark theme).
* reworked `LICENSE`, `appveyor.yml`, `*.kdbx` files.

## 0.8 (2018/09/09)
* some code refactoring.
* reworked `*.iml`, `.travis.yml`, `jenkins`, `jenkinsfile`, `NOTICE` files.
* support `.gitmodules`, `*.pdf`, `.mailmap`, `*.kdbx` (KeePass), `version` (with `.md`, `.adoc`, `.txt` or no extension) files.
* support `*.pem`, `*.crt`, `*.ca-bundle`, `*.cer`, `*.p7b`, `*.p7s`, `*.pfx` certificate files.
* support `*.doc`, `*.docx`, `*.xls`, `*.xlsx`, `*.ppt`, `*.pptx`, `*.one`, `*.onetoc2`, `*.vsd`, `*.vsdx`, `*.vss`, `*.vssx`, `*.vst`, `*.vstx`, `*.mpd`, `*.mpp`, `*.mpt` Microsoft Word, Excel, PowerPoint, OneNote, Visio, Project files.
* support `*.odt`, `*.ods`, `*.odp`, `*.odg` , `*.odf` LibreOffice Writer, Calc, Impress, Draw, Math files.
* increase plugin's icons priority. This way, IDE will prefer plugin's icons instead of default *unknown file types* icon for some file types.

## 0.7 (2018/09/06)
* support `vagrantfile`, `*.iml`, `.gitattributes`, `.gitignore`, `.editorconfig`, `berksfile`, `berksfile.lock`, `dockerfile`, `docker-compose.yml` files.

## 0.6 (2018/09/02)
* reworked `.gitlab-ci.yml`, `appveyor.yml`, `NOTICE` files.
* support `*.jar` files.

## 0.5 (2018/09/01)
* some code refactoring.
* support `bamboo.yml`, `circle.yml`, `codeship-steps.yml`, `codeship-steps.json`, `codefresh.yml`, `.gocd.yaml`, `package.json`, `package-lock.json`, files.
* support `jenkins`, `NOTICE`, `CONTACT` files (with `.md`, `.adoc`, `.txt` or no extension).

## 0.4 (2018/08/26)
* enabled compatibility with all products (WebStorm, etc).
* reworked Maven wrapper `mvnw`, Gradle wrapper `gradlew` files.
* support `README`, `CHANGELOG`, `CHANGES`, `LICENSE`, `COPYING`, `CONTRIBUTING`, `AUTHORS` files (with `.md`, `.adoc`, `.txt` or no extension).

## 0.3 (2018/08/25)
* support Windows script `*.cmd`, `*.bat`, Powershell `*.ps1`, Bash `*.sh`, Maven wrapper `mvnw`, Gradle wrapper `gradlew` files.

## 0.2 (2018/08/25)
* support Jenkins `jenkinsfile`, Gitlab `.gitlab-ci.yml` files.

## 0.1 (2018/08/23)
* support Travis `.travis.yml`, Appveyor `appveyor.yml` files.
