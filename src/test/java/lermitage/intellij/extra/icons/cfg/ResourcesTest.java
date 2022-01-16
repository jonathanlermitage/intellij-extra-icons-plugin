// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg;

import lermitage.intellij.extra.icons.ExtraIconProvider;
import lermitage.intellij.extra.icons.providers.Angular2IconProvider;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourcesTest {

    static List<File> icons;
    static List<File> svgIcons;
    static List<File> pngIcons;

    @BeforeAll
    static void setUp() {
        icons = new ArrayList<>();
        List<File> iconsFolders = Arrays.asList(
            new File("src/main/resources/extra-icons/"),
            new File("src/main/resources/extra-icons/ide/"),
            new File("src/main/resources/extra-icons/officedocs/"));
        iconsFolders.forEach(iconsFolder -> icons.addAll(Arrays.asList(Objects.requireNonNull(
            iconsFolder.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".svg"))
        ))));
        pngIcons = icons.stream().filter(file -> file.getName().endsWith(".png")).collect(Collectors.toList());
        svgIcons = icons.stream().filter(file -> file.getName().endsWith(".svg")).collect(Collectors.toList());
        assertTrue(svgIcons.size() > 0);
        assertTrue(pngIcons.size() > 0);
        icons.forEach(file -> assertTrue(file.getName().endsWith(".png") || file.getName().endsWith(".svg")));
    }

    @Test
    public void icons_should_not_exist_as_svg_and_png() {
        Set<String> pngIconNames = pngIcons.stream()
            .map(file -> file.getName().replace(".png", ""))
            .collect(Collectors.toSet());

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
        Set<String> pngIconNames = pngIcons.stream()
            .map(file -> file.getName().replace(".png", ""))
            .collect(Collectors.toSet());
        List<String> errors = new ArrayList<>();

        for (String icon : pngIconNames) {
            if (icon.endsWith("_dark")) {
                if (icon.endsWith("@2x_dark")) {
                    if (!pngIconNames.contains(icon.replace("@2x_dark", "_dark"))) {
                        errors.add(icon + " should be coupled with _dark");
                    }
                } else {
                    if (!pngIconNames.contains(icon.replace("_dark", "@2x_dark"))) {
                        errors.add(icon + " should be coupled with @2x_dark");
                    }
                }
            } else {
                if (icon.endsWith("@2x")) {
                    if (!pngIconNames.contains(icon.substring(0, icon.length() - "@2x".length()))) {
                        errors.add(icon + " should be coupled with non-@2x");
                    }
                } else {
                    if (!pngIconNames.contains(icon + "@2x")) {
                        errors.add(icon + " should be coupled with @2x");
                    }
                }
            }
        }
        if (!errors.isEmpty()) {
            fail("some PNG icons don't have 2x definition : " + errors);
        }
    }

    @Test
    public void dark_icon_should_be_coupled_with_light_icon() {
        Set<String> iconNames = icons.stream()
            .map(File::getName)
            .collect(Collectors.toSet());
        List<String> errors = new ArrayList<>();

        for (String icon : iconNames) {
            if (icon.contains("_dark") && !iconNames.contains(icon.replace("_dark", ""))) {
                errors.add(icon);
            }
        }
        if (!errors.isEmpty()) {
            fail("some dark icons are not coupled with non-dark icons: " + errors);
        }
    }

    @Test
    public void svg_icons_should_be_16x16() {
        List<String> errors = new ArrayList<>();

        svgIcons.forEach(file -> {
            try {
                String fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                boolean goodWidth = fileContent.contains("width=\"16\"") || fileContent.contains("width=\"16px\"");
                boolean goodHeight = fileContent.contains("height=\"16\"") || fileContent.contains("height=\"16px\"");
                if (!goodWidth || !goodHeight) {
                    errors.add(file.getName());
                }
            } catch (IOException e) {
                fail(e);
            }
        });
        if (!errors.isEmpty()) {
            fail("some SVG icons are not 16x16: " + errors);
        }
    }

    @Test
    public void extra_icons_provider_icons_should_exist() {
        List<String> errors = new ArrayList<>();

        List<String> iconNames = icons.stream().map(file -> {
            String relativePath = file.getAbsolutePath().replaceAll("\\\\", "/");
            relativePath = relativePath.substring(relativePath.lastIndexOf("/extra-icons/") + "/extra-icons/".length());
            return relativePath;
        }).collect(Collectors.toList());

        ExtraIconProvider.allModels().forEach(model -> {
            String extraIconName = model.getIcon().replace("/extra-icons/", "");
            if (!iconNames.contains(extraIconName)) {
                errors.add(model.getId() + " model's icon " + extraIconName + " not found");
            }
        });

        if (!errors.isEmpty()) {
            fail("some ExtraIconProvider model icons not found: " + errors);
        }
    }

    @Test
    public void angular2_icons_provider_icons_should_exist() {
        List<String> errors = new ArrayList<>();

        List<String> iconNames = icons.stream().map(file -> {
            String relativePath = file.getAbsolutePath().replaceAll("\\\\", "/");
            relativePath = relativePath.substring(relativePath.lastIndexOf("/extra-icons/") + "/extra-icons/".length());
            return relativePath;
        }).collect(Collectors.toList());

        Angular2IconProvider.allModels().forEach(model -> {
            String extraIconName = model.getIcon().replace("/extra-icons/", "");
            if (!iconNames.contains(extraIconName)) {
                errors.add(model.getId() + " model's icon " + extraIconName + " not found");
            }
        });

        if (!errors.isEmpty()) {
            fail("some Angular2IconProvider model icons not found: " + errors);
        }
    }
}
