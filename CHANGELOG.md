## Extra Icons Change Log

### 1.15.0 (WIP)
* improve Logstash detection.

### 1.14.0 (2019/11/23)
* rework `.idea` folder icon.

### 1.13.0 (2019/11/07)
* support Appveyor `.appveyor.yml` files.

### 1.12.0 (2019/10/23)
* support `.pid` files.
* support `.jar.original` files.
* support `.npmrc` files.
* support `.npmignore` files.
* support `jest.config.js` files.
* support `karma.conf.js` files.
* rework many icons.

### 1.11.0 (2019/10/10)
* improve AngularJS files support. Thx [diakovidis](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/pull/16).

### 1.10.0 (2019/10/03)
* support Postman `*postman_collection.json`, `*postman_environment.json` files.
* support `.swagger-codegen-ignore` files.
* support Diffusion Archive / XL Deploy Archive `*.dar` files.

### 1.9.0 (2019/09/22)
* rework `azure-pipelines.yml` file icon (use official SVG icon).
* support `.p12` files.

### 1.8.0 (2019/09/09)
* support `.keep`, `.gitkeep`, `.hgkeep`, `.svnkeep` files used by various VCS.
* improve Logback detection.
* support `.pubkey` files.
* support Postman `*.postman.json` files.
* restore support of backup `*.versionBackup`, `*.back`, `*.backup`, `*.old`, `*.prev`, `*.revert` files. Probably dropped by mistake.
* rework `.mvn` folder icon.

### 1.7.0 (2019/07/20)
* support [Glowroot](https://glowroot.org) `glowroot/admin.json`, `glowroot/config.json` files.

### 1.6.0 (2019/06/15)
* support [ImgBot](https://imgbot.net) `.imgbotconfig` files.
* support `*.xz` archive files.
* support `jvm.config` files.
* support Avro `*.avsc` files.
* use icon from [IntelliJ Rust](https://github.com/intellij-rust/intellij-rust) for `cargo.toml` files (a future update will introduce a white-list to ignore some file patterns).
* rework Docker-compose files: Docker files remain blue, and Docker-compose files become pink.

### 1.5.1 (2019/05/25)
* revert changes to folders detection (1.5.0) to fix [#13](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/issues/13): StringIndexOutOfBoundsException and NullPointerException errors.

### 1.5.0 (2019/05/24)
* rework Gradle files.
* rework folders detection.
* support `/docker` folders.
* restore missing `test-ts.svg` icon in [ide173](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/tree/ide173) branch.

### 1.4.0 (2019/05/18)
* support [Better Code Hub](https://bettercodehub.com) `.bettercodehub.yml` files. I'd like to thank the Better Code Hub team for sending me SVG files!
* support FlyWay SQL files `.*/db/migration/.*\.sql`.
* support APK `.apk`, `.xapk` files.
* detect more Docker files.
* introduce regex support.
* switched from DevKit to Gradle workflow: you can now build plugin without IDE, and integration with online CI like Travis will be easy.

### 1.3.0 (2019/04/24)
* support Microsoft Azure `azure-pipelines.yml` files.
* support Dependabot `.dependabot/config.yml` files and `.dependabot` folders.
* fix: Windows `mvnw.bat`, `mvnw.cmd` and Linux `mvnw` files were registered with the same id. That means disabling Windows icons disabled Linux icon too. 

### 1.2.0 (2019/04/12)
* improve file detection capabilities: can work with folders.
* improve CircleCI detection: add `.circleci/config.yml` files.
* support `Gruntfile.js` files.
* support LESS `*.less` files.
* support Visual Studio Code `.vscode/settings.json` files.
* support `intellijcodestyle.xml` files.
* support `.circleci`, `.github`, `.gitlab`, `gradle`, `.idea`, `.mvn`, `.vscode` folders.
* rework SASS `*.sass`, `*.scss` files. Also, they don't need *Sass support* plugin anymore.
* rework `.dockerignore` files.

### 1.1.0 (2019/04/05)
* rework setting table's vertical scrolling.
* fix detection of modifications in setting table.
* add plugin's icon (effective with 2019.1 IDE).

### 1.0.0 (2019/04/01)
* add a graphical config panel to select extra icons to (de)activate. See `Settings > Appearance & Behavior > Extra Icons`.
* changed version numbers: `x.y.z.173` plugins are compatible with 173.0+ IDE builds (2017.3 and newer), `x.y.z.183` plugins are compatible with 183.0+ IDE builds (2018.3 and newer) and provide some additional icons.

### 0.27 and 0.28 (2019/03/29)
* support `*.http` files (requests to be played by IntelliJ HTTP client).
* plugin's configuration: you can now select extra icons to deactivate via a config file. A future release will bring a graphical config panel (but contributions are welcome!).

### 0.25 and 0.26 (2019/02/17)
* fixed ArchUnit files detection.
* support GraphQL `*.graphqls`, `graphql.schema.json`, `graphql.config.json` files.
* support `jvm.properties` files.
* support JUnit `junit-platform.properties` files.
* support SVG `*.svg` files.
* support `*.3g2`, `*.3gp`, `*.avi`, `*.divx`, `*.f4a`, `*.f4b`, `*.f4p`, `*.f4v`, `*.flv`, `*.m4p`, `*.m4v`, `*.mkv`, `*.mp4`, `*.mpg`, `*.mpeg`, `*.mov`, `*.ogm`, `*.ogv`, `*.vob`, `*.webm`, `*.wmv`, `*.xvid` video files.

### 0.23 and 0.24 (2019/01/19)
* support [ArchUnit](https://github.com/TNG/ArchUnit) `archunit.properties` files.
* support [Deptective](https://github.com/moditect/deptective) `deptective.json` files.
* improved Jenkins file detection accuracy.

### 0.21 and 0.22 (2019/01/10)
* reworked Haxe `*.hxml` files (new icons).
* support `package-info.java` files.
* support NSIS `*.nsi` files.
* improved file detection accuracy.

### 0.19 and 0.20 (2018/12/21)
* starting from 0.19, there are two builds:
  * odd minor revision number (0.19, 0.21, 1.1, 1.3...): compatible with 173.0 IDE builds (aka 2017.3). This build doesn't bundles features that need 2018.3 IDE builds: AngularJS, SASS, Javascript. They're excluded because Extra Icons plugin reads project's type in order to activate some file recognition (AngularJS, SASS, Javascrip): it is based on 2018.3 IDE features. Other files detection is simply based on files pattern, that's why it works with older IDE builds, and I will maintain a branch (`ide173`) to keep support.
  * even minor revision number (0.20, 0.22, 1.0, 1.2...): compatible with latest IDE builds (183.0, aka 2018.3).
  
This way, you simply have to download the latest version offered by the plugins manager: on older IDE, you'll get the latest odd minor revision number. On recent IDE, you get the latest even minor revision number that sheeps same features as odd version, plus features that comes with recent IDE builds.

### 0.18 (2018/12/10)
* plugin is no more compatible with 173.0 IDE builds: minimum version is now 183.0 (2018.3).

### 0.17 (2018/12/08)
* support `gradle.properties`, `LISEZMOI.*` files.
* support AngularJS `*.module.ts`, `*.component.ts`, `*.service.ts`, `*.pipe.ts`, `*.directive.ts`, `*.guard.ts` files (optional, needs *AngularJS* plugin from IntelliJ Ultimate, WebStorm, PhpStorm). Enabled in AngularJS projects only. Thx to Edoardo Luppi.
* support SASS `*.sass`, `*.scss` files (optional, needs *Sass support* plugin) Thx to Edoardo Luppi.
* support [Haxe](https://haxe.org) `*.hx`, `*.hxml` files.
* support [Cerebro](https://github.com/lmenezes/cerebro) `cerebro*.conf` files.

### 0.16 (2018/11/15)
* support Gatling `gatling*.conf` files.
* support Lombok `lombok.config` files.
* support `kubernetes*.yml` files.

### 0.15 (2018/11/03)
* improved file detection accuracy.
* support Cassandra `cassandra*.yml`, Redis `redis*.conf` files.

### 0.14 (2018/10/28)
* support [Codacy](https://support.codacy.com/hc/en-us/articles/115002130625-Codacy-Configuration-File) `.codacy.yaml`, `.codacy.yml` files.
* support Elasticsearch `elastic*.yml`, Logstash `logstash*.yml`, Kibana `kibana*.yml` files.

### 0.13 (2018/10/21)
* detect more Docker and Docker Compose files.
* detect more Jenkins files.
* support [Zappr](https://zappr.opensource.zalan.do) `.zappr.yaml` files.
* support [Codecov](https://docs.codecov.io/docs/codecov-yaml) `codecov.yml`, `.codecov.yml` files. 
* support `.dockerignore` files.

### 0.12 (2018/10/10)
* reworked Gitlab files (reduced margin by 1px).
* reworked Maven and Gradle wrapper files (new icons).
* reworked `package.json`, `package-lock.json` files (new icons).
* support [Terraform](https://www.terraform.io) `*.hcl`, `*.tf`, `*.tf.json` files.
* support [Cirrus](https://cirrus-ci.org) `.cirrus.yml` files.
* support `*.rpm`, `*.deb`, `*.zip`, `*.7z`, `*.tar`, `*.gz`, `*.bz2` files.
* support `bower.json`, `.bowerrc`, `.jsbeautifyrc`, `.jshintrc` files.

### 0.11 (2018/09/29)
* support `*.jks`, `BUILD`, `BUILDING`, `PRIVACY`, `ROADMAP`, `CONTRIBUTE`, `CONTRIBUTION`, `TODO`, `TEST`, `TESTING`, `LICENSE_INFO`, `ADDITIONAL_LICENSE_INFO` files.

### 0.10 (2018/09/22)
* reworked `*.iml`, `*.war` files.
* support `nginx.conf`, `.htaccess`, `puppet.conf`, `my.ini`, `*.ini`, `*.cfg`, `*.conf`, `*.toml`, `*.asciidoc`, `*.versionsbackup` files.

### 0.9 (2018/09/15)
* reworked `LICENSE`, `berksfile.lock` files.
* support `log4j`, `log4j-test`, `logback`, `logback-test` files with `.xml`, `.yml`, `yaml`, `.properties`, `.json` extensions.
* support `*.csv`, `*.md`, `*.adoc`, `*.cert`, `*.war`, `*.form` extensions.
* support backup `*.versionBackup`, `*.back`, `*.backup`, `*.old`, `*.prev`, `*.revert` files.

### 0.8.1 (2018/09/10)
* reworked Microsoft Word, Excel, PowerPoint, OneNote, Visio, Project files (added dark theme).
* reworked `LICENSE`, `appveyor.yml`, `*.kdbx` files.

### 0.8 (2018/09/09)
* some code refactoring.
* reworked `*.iml`, `.travis.yml`, `jenkins`, `jenkinsfile`, `NOTICE` files.
* support `.gitmodules`, `*.pdf`, `.mailmap`, `*.kdbx` (KeePass), `version` (with `.md`, `.adoc`, `.txt` or no extension) files.
* support `*.pem`, `*.crt`, `*.ca-bundle`, `*.cer`, `*.p7b`, `*.p7s`, `*.pfx` certificate files.
* support `*.doc`, `*.docx`, `*.xls`, `*.xlsx`, `*.ppt`, `*.pptx`, `*.one`, `*.onetoc2`, `*.vsd`, `*.vsdx`, `*.vss`, `*.vssx`, `*.vst`, `*.vstx`, `*.mpd`, `*.mpp`, `*.mpt` Microsoft Word, Excel, PowerPoint, OneNote, Visio, Project files.
* support `*.odt`, `*.ods`, `*.odp`, `*.odg` , `*.odf` LibreOffice Writer, Calc, Impress, Draw, Math files.
* increase plugin's icons priority. This way, IDE will prefer plugin's icons instead of default *unknown file types* icon for some file types.

### 0.7 (2018/09/06)
* support `vagrantfile`, `*.iml`, `.gitattributes`, `.gitignore`, `.editorconfig`, `berksfile`, `berksfile.lock`, `dockerfile`, `docker-compose.yml` files.

### 0.6 (2018/09/02)
* reworked `.gitlab-ci.yml`, `appveyor.yml`, `NOTICE` files.
* support `*.jar` files.

### 0.5 (2018/09/01)
* some code refactoring.
* support `bamboo.yml`, `circle.yml`, `codeship-steps.yml`, `codeship-steps.json`, `codefresh.yml`, `.gocd.yaml`, `package.json`, `package-lock.json`, files.
* support `jenkins`, `NOTICE`, `CONTACT` files (with `.md`, `.adoc`, `.txt` or no extension).

### 0.4 (2018/08/26)
* enabled compatibility with all products (WebStorm, etc).
* reworked Maven wrapper `mvnw`, Gradle wrapper `gradlew` files.
* support `README`, `CHANGELOG`, `CHANGES`, `LICENSE`, `COPYING`, `CONTRIBUTING`, `AUTHORS` files (with `.md`, `.adoc`, `.txt` or no extension).

### 0.3 (2018/08/25)
* support Windows script `*.cmd`, `*.bat`, Powershell `*.ps1`, Bash `*.sh`, Maven wrapper `mvnw`, Gradle wrapper `gradlew` files.

### 0.2 (2018/08/25)
* support Jenkins `jenkinsfile`, Gitlab `.gitlab-ci.yml` files.

### 0.1 (2018/08/23)
* support Travis `.travis.yml`, Appveyor `appveyor.yml` files.
