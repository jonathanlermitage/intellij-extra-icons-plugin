## Extra Icons Change Log

### TODO, ideas
* add a panel in `Settings > Other Settings` to select extra icons to activate.

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
