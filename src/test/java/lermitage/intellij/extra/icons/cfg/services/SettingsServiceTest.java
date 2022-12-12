// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.services;

import lermitage.intellij.extra.icons.Model;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void registered_models_description_should_be_unique() {
        List<Model> allRegisteredModels = SettingsService.getAllRegisteredModels();
        Set<String> descriptions = new HashSet<>();
        Set<String> duplicatedDescriptions = new HashSet<>();

        for (Model model : allRegisteredModels) {
            String description = model.getDescription();
            if (descriptions.contains(description)) {
                duplicatedDescriptions.add(description);
            }
            descriptions.add(description);
        }
        if (!duplicatedDescriptions.isEmpty()) {
            fail("model descriptions are registered more than once: " + duplicatedDescriptions);
        }
    }

    @Test
    public void registered_models_should_have_existing_icon_file() {
        List<Model> allRegisteredModels = SettingsService.getAllRegisteredModels();
        List<String> errors = new ArrayList<>();

        allRegisteredModels.forEach(model -> {
            if (!new File("src/main/resources", model.getIcon()).exists()) {
                errors.add(model.getId() + ": " + model.getIcon());
            }
        });
        if (!errors.isEmpty()) {
            fail("some icons are missing in resources folder: " + errors);
        }
    }
}
