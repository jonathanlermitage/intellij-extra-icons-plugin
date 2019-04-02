package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Icon provider specific for SASS files.
 * This should be registered only if the bundled SASS plugin is enabled.
 *
 * @author Edoardo Luppi
 */
public class SassIconProvider extends BaseIconProvider implements DumbAware {
    
    @NotNull
    public static List<Model> allModels() {
        return Collections.singletonList(
                new Model("sass", "/icons/sass.svg", "SASS: *.sass, *.scss")
                        .end(".sass", ".scss")
        );
    }
    
    public SassIconProvider() {
        super();
    }
    
    @Override
    protected List<Model> getAllModels() {
        return allModels();
    }
}
