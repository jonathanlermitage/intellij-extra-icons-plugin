// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg;

import lermitage.intellij.extra.icons.Model;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
            fail("model ids are registered more than once: " + duplicatedIds);
        }
    }

    @Test
    public void registered_models_should_have_existing_icon_file() {
        List<Model> allRegisteredModels = SettingsService.getAllRegisteredModels();
        List<String> errors = new ArrayList<>();
        allRegisteredModels.forEach(model -> {
            if (!new File("resources", model.getIcon()).exists()) {
                errors.add(model.getId() + ": " + model.getIcon());
            }
        });
        if (!errors.isEmpty()) {
            fail("some icons are missing in resources folder:" + errors);
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
        assertTrue(svgIcons.length > 0);
        assertTrue(pngIcons.length > 0);

        for (File svgIcon : svgIcons) {
            String svgIconName = svgIcon.getName().replace(".svg", "");
            assertFalse(pngIconNames.contains(svgIconName), svgIconName);
            assertFalse(pngIconNames.contains(svgIconName + "_dark"), svgIconName);
            assertFalse(pngIconNames.contains(svgIconName + "@2x"), svgIconName);
            assertFalse(pngIconNames.contains(svgIconName + "@2x_dark"), svgIconName);
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
                    assertTrue(pngIconNames.contains(icon.replace("@2x_dark", "_dark")), icon);
                } else {
                    assertTrue(pngIconNames.contains(icon.replace("_dark", "@2x_dark")), icon);
                }
            } else {
                if (icon.endsWith("@2x")) {
                    assertTrue(pngIconNames.contains(icon.substring(0, icon.length() - "@2x".length())), icon);
                } else {
                    assertTrue(pngIconNames.contains(icon + "@2x"), icon);
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
                assertTrue(iconNames.contains(icon.replace("_dark", "")), icon);
            }
        }
    }
}
