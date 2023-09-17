// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg;

import lermitage.intellij.extra.icons.ExtraIconProvider;
import lermitage.intellij.extra.icons.IconType;
import lermitage.intellij.extra.icons.utils.IconUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("HardCodedStringLiteral")
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
            new File("src/main/resources/extra-icons/officedocs/"),
            new File("src/main/resources/extra-icons/newui/"),
            new File("src/main/resources/extra-icons/newui/ide/"),
            new File("src/test/resources/issue141/"));
        iconsFolders.forEach(iconsFolder -> {
            assertTrue(iconsFolder.exists());
            assertTrue(iconsFolder.isDirectory());
            icons.addAll(Arrays.asList(Objects.requireNonNull(
                iconsFolder.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".svg"))
            )));
        });
        pngIcons = icons.stream().filter(file -> file.getName().endsWith(".png")).collect(Collectors.toList());
        svgIcons = icons.stream().filter(file -> file.getName().endsWith(".svg")).collect(Collectors.toList());
        assertFalse(svgIcons.isEmpty());
        assertFalse(pngIcons.isEmpty());
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
            fail("some PNG icons don't have 2x definition : " + arrayToString(errors));
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
            fail("some dark icons are not coupled with non-dark icons: " + arrayToString(errors));
        }
    }

    @Test
    public void svg_icons_should_be_16x16() {
        List<String> errors = new ArrayList<>();

        svgIcons.forEach(file -> {
            try {
                String fileContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
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
            fail("some SVG icons are not 16x16: " + arrayToString(errors));
        }
    }

    @Test
    public void svg_icons_should_not_contain_unsupported_style_attributes() {
        List<String> errors = new ArrayList<>();

        svgIcons.forEach(file -> {
            try {
                String fileContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                List<String> removableAttributes = List.of("<switch>", "enable-background");
                removableAttributes.forEach(removableAttribute -> {
                    if (fileContent.contains(removableAttribute)) {
                        errors.add(file.getName() + ": removable element or attribute is '" + removableAttribute + "'. " +
                            "You can safely remove this attribute from file");
                    }
                });
            } catch (IOException e) {
                fail(e);
            }
        });
        if (!errors.isEmpty()) {
            fail("some SVG icons have unsupported style attributes: " + arrayToString(errors));
        }
    }

    /**
     * SVG icons loaded by "IconLoader.getIcon" are loaded with IDE's bundled JSVG, so
     * we want to check if everything goes well (even if IDE adds some magic... ^_^).
     */
    @Disabled("SVG loading is now handled by IDE - Should run IDE integration tests")
    @Test
    public void svg_icons_should_load_with_jsvg() {
        List<String> errors = new ArrayList<>();

        svgIcons.forEach(file -> {
            try {
                byte[] fileContent = Files.readAllBytes(file.toPath());
                IconUtils.ImageWrapper imageWrapper = IconUtils.loadImage(fileContent, IconType.SVG, 1f);
                if (imageWrapper == null) {
                    errors.add(file.getName() + ": can't be rendered by JSVG");
                }
            } catch (IOException e) {
                errors.add(file.getName() + ": can't be loaded by JSVG. I/O error is: " + e.getMessage());
            }
        });
        if (!errors.isEmpty()) {
            fail("some SVG icons did not load: " + arrayToString(errors));
        }
    }

    @Test
    public void extra_icons_provider_icons_should_exist() {
        List<String> errors = new ArrayList<>();

        List<String> iconNames = icons.stream()
            .map(file -> {
                String relativePath = file.getAbsolutePath().replaceAll("\\\\", "/");
                relativePath = relativePath.substring(relativePath.lastIndexOf("/extra-icons/") + "/extra-icons/".length());
                return relativePath;
            }).toList();
        ExtraIconProvider.allModels().forEach(model -> {
            String extraIconName = model.getIcon().replace("extra-icons/", "");
            if (!iconNames.contains(extraIconName)) {
                errors.add(model.getId() + " model's icon " + extraIconName + " not found");
            }
        });

        if (!errors.isEmpty()) {
            fail("some ExtraIconProvider model icons not found: " + arrayToString(errors));
        }
    }

    @Test
    public void extra_icons_provider_newUI_icons_should_exist_when_needed() {
        List<String> errors = new ArrayList<>();

        List<String> newUIIconNamesWithParentDir = icons.stream()
            .filter(file -> file.getAbsolutePath().contains("newui"))
            .map(file -> {
                String relativePath = file.getAbsolutePath().replaceAll("\\\\", "/");
                relativePath = relativePath.substring(relativePath.lastIndexOf("/extra-icons/") + "/extra-icons/".length());
                return relativePath;
            }).toList();
        ExtraIconProvider.allModels().forEach(model -> {
            String newUIIconName = "newui/" + model.getIcon().replace("extra-icons/", "");
            if (model.isAutoLoadNewUIIconVariant()) {
                if (!newUIIconNamesWithParentDir.contains(newUIIconName)) {
                    errors.add(model.getId() + " model's new UI icon " + newUIIconName + " not found");
                }
            }
        });

        if (!errors.isEmpty()) {
            fail("some ExtraIconProvider model new UI icons not found: " + arrayToString(errors));
        }
    }

    /**
     * If an IDE icon named "foo.svg" is overridden by a custom IDE icon with the same name, the icon displayed in
     * the icons list in settings will also be replaced by current active icon override. This is why their name
     * should be different. In practice, we consider that IDE's icon "foo.svg" should be overridden by "foo_.svg".
     * Alt icons are not affected, because they already have different name.
     */
    @Test
    public void ide_icons_name_should_differ_from_actual_icon_file_name() {
        List<String> errors = new ArrayList<>();

        ExtraIconProvider.allModels().forEach(model -> {
            if (model.getIdeIcon() != null) {
                if (model.getIcon().endsWith(model.getIdeIcon())) {
                    errors.add(model.getId() + " IDE model's icon " + model.getIdeIcon() + " and custom icon "
                        + model.getIcon() + " should be different");
                }
            }
        });

        if (!errors.isEmpty()) {
            fail("some ExtraIconProvider model IDE icons have bad name: " + arrayToString(errors));
        }
    }

    private String arrayToString(List<String> items) {
        StringBuilder sb = new StringBuilder();
        items.forEach(s -> sb.append("\n - ").append(s));
        sb.append("\n");
        return sb.toString();
    }
}
