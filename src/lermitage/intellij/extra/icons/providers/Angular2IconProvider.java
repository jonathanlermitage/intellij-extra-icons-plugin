package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import org.angular2.lang.Angular2LangUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.Arrays.asList;
import static lermitage.intellij.extra.icons.Model.ofFile;

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
            ofFile("angular_module", "/icons/angular-module.svg", "AngularJS (in AngularJS projects only): *.module.(js|ts)")
                .regex(".*module\\.(js|ts)"),
            ofFile("angular_component", "/icons/angular-component.svg", "AngularJS (in AngularJS projects only): *.(component|controller).(js|ts)")
                .regex(".*component\\.(js|ts)|.*controller\\.(js|ts)"),
            ofFile("angular_service", "/icons/angular-service.svg", "AngularJS (in AngularJS projects only): *.service.(js|ts)")
                .regex(".*\\.service\\.(js|ts)"),
            ofFile("angular_pipe", "/icons/angular-pipe.svg", "AngularJS (in AngularJS projects only): *.pipe.(js|ts)")
                .regex("pipe\\.(js|ts)"),
            ofFile("angular_directive", "/icons/angular-directive.svg", "AngularJS (in AngularJS projects only): *.directive.(js|ts)")
                .regex("directive(s)?\\.(js|ts)"),
            ofFile("angular_guard", "/icons/angular-guard.svg", "AngularJS (in AngularJS projects only): *.guard.(js|ts)")
                .regex("guard"),
            ofFile("angular_resolver", "/icons/angular-resolver.svg", "AngularJS (in AngularJS projects only): *.resolver.(js|ts)")
                .regex("resolver"),
            ofFile("angular_spec", "/icons/test-ts.svg", "AngularJS (in AngularJS projects only): *.spec.(js|ts)")
                .regex("spec"),
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
        return true;
    }
}
