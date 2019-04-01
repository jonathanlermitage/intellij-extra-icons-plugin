package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.cfg.SettingsService;
import org.angular2.lang.Angular2LangUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Icon provider specific for Angular2+ modules.
 * This should be registered only if the bundled AngularJS plugin is enabled.
 *
 * @author Edoardo Luppi
 */
public class Angular2IconProvider extends BaseIconProvider implements DumbAware {
    
    private List<Model> models;
    
    @NotNull
    public static List<Model> allModels() {
        return asList(
                new Model("angular_module", "/icons/angular-module.svg", "AngularJS (in AngularJS projects only): *.module.ts")
                        .end(".module.ts"),
                new Model("angular_component", "/icons/angular-component.svg", "AngularJS (in AngularJS projects only): *.component.ts")
                        .end(".component.ts"),
                new Model("angular_service", "/icons/angular-service.svg", "AngularJS (in AngularJS projects only): *.service.ts")
                        .end(".service.ts"),
                new Model("angular_pipe", "/icons/angular-pipe.svg", "AngularJS (in AngularJS projects only): *.pipe.ts")
                        .end(".pipe.ts"),
                new Model("angular_directive", "/icons/angular-directive.svg", "AngularJS (in AngularJS projects only): *.directive.ts")
                        .end(".directive.ts"),
                new Model("angular_guard", "/icons/angular-guard.svg", "AngularJS (in AngularJS projects only): *.guard.ts")
                        .end(".guard.ts"),
                new Model("angular_resolver", "/icons/angular-resolver.svg", "AngularJS (in AngularJS projects only): *.resolver.ts")
                        .end(".resolver.ts"),
                new Model("angular_spec", "/icons/test-ts.svg", "AngularJS (in AngularJS projects only): *.spec.ts")
                        .end(".spec.ts"),
                new Model("angular_html", "/icons/html5.svg", "AngularJS (in AngularJS projects only): *.html")
                        .end(".html")
        );
    }
    
    public Angular2IconProvider() {
        super();
        models = allModels();
        List<String> disabledModelIds = SettingsService.getDisabledModelIds();
        models.forEach(model -> model.setEnabled(!disabledModelIds.contains(model.getId())));
    }
    
    @NotNull
    @Override
    protected List<Model> getModels() {
        return models;
    }
    
    @Override
    protected boolean isSupported(@NotNull final PsiFile psiFile) {
        return Angular2LangUtil.isAngular2Context(psiFile);
    }
}
