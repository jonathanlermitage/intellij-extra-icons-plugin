package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Icon provider specific for Javascript-related files.
 * This should be registered only if the bundled Javascript Support plugin is enabled.
 *
 * @author Edoardo Luppi
 */
public class JavascriptIconProvider extends BaseIconProvider implements DumbAware {
    
    @NotNull
    public static List<Model> allModels() {
        return Collections.singletonList(
                new Model("typescript", "/icons/test-ts.svg", "Typescript: *.spec.ts")
                        .end(".spec.ts")
        );
    }
    
    public JavascriptIconProvider() {
        super();
    }
    
    @Override
    protected List<Model> getAllModels() {
        return allModels();
    }
}
