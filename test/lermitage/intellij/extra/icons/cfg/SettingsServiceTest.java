package lermitage.intellij.extra.icons.cfg;

import lermitage.intellij.extra.icons.Model;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
