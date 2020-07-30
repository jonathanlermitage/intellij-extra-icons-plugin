@echo off

if [%1] == [help] (
  echo  w $V:    set gradle wrapper
  echo  fixgit:  fix permission flag on git index for required files
  echo  run :    run plugin in IntelliJ Ultimate 2020.2
  echo  runeap:  run plugin in latest IntelliJ Ultimate EAP Snapshot
  echo  release: package plugin
  echo  test:    run unit tests
  echo  cv:      check dependencies and Gradle updates
  echo  svgo:    optimize SVG icons with SGVO. SVGO must be present, type 'npm install -g svgo' if needed)
)

if [%1] == [w] (
  gradle wrapper --gradle-version=%2 --no-daemon
)
if [%1] == [fixgit] (
  echo git update-index --chmod=+x gradlew
  git update-index --chmod=+x gradlew
)
if [%1] == [run] (
  gradlew buildPlugin runIde --warning-mode all
)
if [%1] == [runeap] (
  gradlew buildPlugin runIde --warning-mode all -PideaVersion=IU-LATEST-EAP-SNAPSHOT
)
if [%1] == [release] (
  gradlew clean buildPlugin test verifyPlugin --warning-mode all
)
if [%1] == [test] (
  gradlew cleanTest test verifyPlugin --warning-mode all
)
if [%1] == [cv] (
  gradlew dependencyUpdates -Drevision=release -DoutputFormatter=plain -DoutputDir=./build/ --warning-mode all
)
if [%1] == [svgo] (
  svgo --folder=resources/icons/ --multipass --config=svgo.yml
)
