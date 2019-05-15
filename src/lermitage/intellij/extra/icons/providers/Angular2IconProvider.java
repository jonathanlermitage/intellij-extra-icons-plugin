package lermitage.intellij.extra.icons.providers;

import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;

import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import org.angular2.lang.Angular2LangUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static lermitage.intellij.extra.icons.Model.ofFile;

import static java.util.Arrays.asList;

/**
 * Icon provider specific for Angular2+ modules.
 * This should be registered only if the bundled AngularJS plugin is enabled.
 *
 * @author Edoardo Luppi
 */
public class Angular2IconProvider extends BaseIconProvider implements DumbAware {
    
    @NotNull
    public static List<Model> allModels() {
        return asList(
                ofFile("angular_module", "/icons/angular-module.svg", "AngularJS (in AngularJS projects only): *.module.ts")
                        .end(".module.ts"),
                ofFile("angular_component", "/icons/angular-component.svg", "AngularJS (in AngularJS projects only): *.component.ts")
                        .end(".component.ts"),
                ofFile("angular_service", "/icons/angular-service.svg", "AngularJS (in AngularJS projects only): *.service.ts")
                        .end(".service.ts"),
                ofFile("angular_pipe", "/icons/angular-pipe.svg", "AngularJS (in AngularJS projects only): *.pipe.ts")
                        .end(".pipe.ts"),
                ofFile("angular_directive", "/icons/angular-directive.svg", "AngularJS (in AngularJS projects only): *.directive.ts")
                        .end(".directive.ts"),
                ofFile("angular_guard", "/icons/angular-guard.svg", "AngularJS (in AngularJS projects only): *.guard.ts")
                        .end(".guard.ts"),
                ofFile("angular_resolver", "/icons/angular-resolver.svg", "AngularJS (in AngularJS projects only): *.resolver.ts")
                        .end(".resolver.ts"),
                ofFile("angular_spec", "/icons/test-ts.svg", "AngularJS (in AngularJS projects only): *.spec.ts")
                        .end(".spec.ts"),
                ofFile("angular_html", "/icons/html5.svg", "AngularJS (in AngularJS projects only): *.html")
                        .end(".html")
        );
    }
    
    public Angular2IconProvider() {
        super();
    }
    
    @Override
    protected List<Model> getAllModels() {
        return allModels();
    }
    
    @Override
    protected boolean isSupported(@NotNull final PsiFile psiFile) {
        return Angular2LangUtil.isAngular2Context(psiFile);
    }
}
