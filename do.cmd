@echo off
@echo --------- %date% %time% ---------

if [%1] == [help] (
  echo w $V:    set or upgrade gradle wrapper to version $V
  echo fixgit:  fix permission flag on git index for required files
  echo run :    run plugin in IntelliJ Ultimate
  echo runeap:  run plugin in latest IntelliJ Ultimate EAP Snapshot
  echo runold:  run plugin in oldest supported IntelliJ Ultimate version
  echo release: package plugin
  echo test:    run unit tests
  echo cv:      check dependencies and Gradle updates
  echo oga:     check for deprecated groupId and artifactId couples
  echo svgo:    optimize SVG icons with SVGO. SVGO must be present, type 'npm install -g svgo' if needed
  echo lock:    write gradle dependency versions lock file
)

if [%1] == [w] (
  gradle wrapper --gradle-version=%2 --no-daemon
)
if [%1] == [fixgit] (
  git update-index --chmod=+x gradlew
  echo "gradlew" has now executable flag on git
  git update-index --chmod=+x do
  echo "do" has now executable flag on git
)
if [%1] == [run] (
  gradlew buildPlugin runIde --warning-mode all
)
if [%1] == [runeap] (
  gradlew buildPlugin runIde --warning-mode all -PpluginIdeaVersion=IU-LATEST-EAP-SNAPSHOT -PpluginDownloadIdeaSources=false
)
if [%1] == [runold] (
  gradlew buildPlugin runIde --warning-mode all -PpluginIdeaVersion=IU-2021.1.3 -PpluginDownloadIdeaSources=false
)
if [%1] == [release] (
  gradlew clean buildPlugin test verifyPlugin --warning-mode all
)
if [%1] == [test] (
  gradlew cleanTest test verifyPlugin --warning-mode all
)
if [%1] == [cv] (
  gradlew dependencyUpdates --warning-mode all
)
if [%1] == [cvnd] (
  gradlew dependencyUpdates --warning-mode all --no-daemon
)
if [%1] == [oga] (
  gradlew biz-lermitage-oga-gradle-check
)
if [%1] == [svgo] (
  svgo --folder=src/main/resources/extra-icons/ --recursive
)
if [%1] == [lock] (
  gradlew dependencies --write-locks
)
