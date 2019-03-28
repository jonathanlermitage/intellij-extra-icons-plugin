You can select extra icons to deactivate: create a file `lermitage-extra-icons.xml` located in your IDE's options folder (something like `~\.IntelliJIdea1234.5\config\options\`)
. This file contains icon names to deactivate:

```xml
<application>
  <component name="ExtraIconsSettings">
    <option name="disabledModelIds">
      <list>
        <option value="htaccess" />
        <option value="editorconfig" />
        <option value="sass" />
      </list>
    </option>
  </component>
</application>
```
Find icon names in plugin's [IconProviders source code](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/tree/master/src/lermitage/intellij/extra/icons/providers) like [ExtraIconProvider.java](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/src/lermitage/intellij/extra/icons/ExtraIconProvider.java) or [SassIconProvider.java](https://github.com/jonathanlermitage/intellij-extra-icons-plugin/blob/master/src/lermitage/intellij/extra/icons/providers/SassIconProvider.java) (e.g. the first argument of `m("htaccess", "/icons/apache.png").eq(".htaccess"),` denotates an icon called "htaccess" for `.htaccess` files).

**A future release will bring a graphical config panel (but contributions are welcome!).**
