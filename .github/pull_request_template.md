* ensure unit tests are green by running `./gradlew test verifyPlugin`
* please reformat new and modified code only: do not reformat the whole project or entire existing file (in other words,
  try to limit the amount of changes in order to speed up code review)
* please write comments in english only
* to finish, don't hesitate to add your name or nickname (and LinkedIn profile, etc.) to the contributors list ;-)

Tip: to support new icons, you will want to
edit [this file](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/src/main/java/lermitage/intellij/extra/icons/ExtraIconProvider.java#L24)
. All icons and selection rules are registered here. Icons are stored in `src/main/resources/extra-icons/`.  
Try the plugin in a standalone IDE with `./gradlew runIde`.
