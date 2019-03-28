package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import org.angular2.lang.Angular2LangUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Icon provider specific for Angular2+ modules.
 * This should be registered only if the bundled AngularJS plugin is enabled.
 *
 * @author Edoardo Luppi
 */
class Angular2IconProvider extends BaseIconProvider implements DumbAware {
    @NotNull
    @Override
    protected List<Model> getModels() {
        return Arrays.asList(
                new Model("angular", "/icons/angular-module.svg").end(".module.ts"),
                new Model("angular", "/icons/angular-component.svg").end(".component.ts"),
                new Model("angular", "/icons/angular-service.svg").end(".service.ts"),
                new Model("angular", "/icons/angular-pipe.svg").end(".pipe.ts"),
                new Model("angular", "/icons/angular-directive.svg").end(".directive.ts"),
                new Model("angular", "/icons/angular-guard.svg").end(".guard.ts"),
                new Model("angular", "/icons/angular-resolver.svg").end(".resolver.ts"),
                new Model("angular", "/icons/test-ts.svg").end(".spec.ts"),
                new Model("angular", "/icons/html5.svg").end(".html")
        );
    }
    
    @Override
    protected boolean isSupported(@NotNull final PsiFile psiFile) {
        return Angular2LangUtil.isAngular2Context(psiFile);
    }
}
