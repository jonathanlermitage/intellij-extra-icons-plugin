// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg;

import lermitage.intellij.extra.icons.Model;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SettingsServiceTest {

    @Test
    public void registered_models_should_be_unique() {
        List<Model> allRegisteredModels = SettingsService.getAllRegisteredModels();
        Set<String> ids = new HashSet<>();
        Set<String> duplicatedIds = new HashSet<>();
        for (Model model : allRegisteredModels) {
            String id = model.getId();
            if (ids.contains(id)) {
                duplicatedIds.add(id);
            }
            ids.add(id);
        }
        if (!duplicatedIds.isEmpty()) {
            Assert.fail("model ids are registered more than once: " + duplicatedIds);
        }
    }

    @Test
    public void icons_should_not_exist_as_svg_and_png() {
        File iconsFolder = new File("resources/icons/");
        File[] svgIcons = iconsFolder.listFiles((dir, name) -> name.endsWith(".svg"));
        File[] pngIcons = iconsFolder.listFiles((dir, name) -> name.endsWith(".png"));
        Set<String> pngIconNames = Stream.of(pngIcons)
            .map(file -> file.getName().replace(".png", ""))
            .collect(Collectors.toSet());
        Assert.assertTrue(svgIcons.length > 0);
        Assert.assertTrue(pngIcons.length > 0);

        for (File svgIcon : svgIcons) {
            String svgIconName = svgIcon.getName().replace(".svg", "");
            Assert.assertFalse(svgIconName, pngIconNames.contains(svgIconName));
            Assert.assertFalse(svgIconName, pngIconNames.contains(svgIconName + "_dark"));
            Assert.assertFalse(svgIconName, pngIconNames.contains(svgIconName + "@2x"));
            Assert.assertFalse(svgIconName, pngIconNames.contains(svgIconName + "@2x_dark"));
        }
    }
}
