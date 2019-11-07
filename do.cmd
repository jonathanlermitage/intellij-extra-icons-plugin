@echo off

if [%1] == [help] (
  echo  w $V:   set gradle wrapper
  echo  fixgit: fix permission flag on git index for required files
)

if [%1] == [w] (
  gradle wrapper --gradle-version=%2 --distribution-type all --no-daemon
)
if [%1] == [fixgit] (
  echo git update-index --chmod=+x gradlew
  git update-index --chmod=+x gradlew
)
