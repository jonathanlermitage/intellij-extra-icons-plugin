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

    @Test
    public void png_icons_should_have_2x_definition() {
        File iconsFolder = new File("resources/icons/");
        File[] pngIcons = iconsFolder.listFiles((dir, name) -> name.endsWith(".png"));
        Set<String> pngIconNames = Stream.of(pngIcons)
            .map(file -> file.getName().replace(".png", ""))
            .collect(Collectors.toSet());

        for (String icon : pngIconNames) {
            if (icon.endsWith("_dark")) {
                if (icon.endsWith("@2x_dark")) {
                    Assert.assertTrue(icon, pngIconNames.contains(icon.replace("@2x_dark", "_dark")));
                } else {
                    Assert.assertTrue(icon, pngIconNames.contains(icon.replace("_dark", "@2x_dark")));
                }
            } else {
                if (icon.endsWith("@2x")) {
                    Assert.assertTrue(icon, pngIconNames.contains(icon.substring(0, icon.length() - "@2x".length())));
                } else {
                    Assert.assertTrue(icon, pngIconNames.contains(icon + "@2x"));
                }
            }
        }
    }

    @Test
    public void dark_icon_should_be_coupled_with_light_icon() {
        File iconsFolder = new File("resources/icons/");
        File[] icons = iconsFolder.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".svg"));
        Set<String> iconNames = Stream.of(icons)
            .map(File::getName)
            .collect(Collectors.toSet());

        for (String icon : iconNames) {
            if (icon.contains("_dark")) {
                Assert.assertTrue(icon, iconNames.contains(icon.replace("_dark", "")));
            }
        }
    }
}
