// SPDX-License-Identifier: MIT

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
            ofFile("angular_json", "/extra-icons/angular2.svg", "Angular (in Angular 2+ projects only): angular.json")
                .eq("angular.json"),
            ofFile("angular_module", "/extra-icons/angular-module.svg", "Angular (in Angular 2+ projects only): *.module.ts")
                .end(".module.ts"),
            ofFile("angular_component", "/extra-icons/angular-component.svg", "Angular (in Angular 2+ projects only): *.component.ts")
                .end(".component.ts"),
            ofFile("angular_service", "/extra-icons/angular-service.svg", "Angular (in Angular 2+ projects only): *.service.ts")
                .end(".service.ts"),
            ofFile("angular_pipe", "/extra-icons/angular-pipe.svg", "Angular (in Angular 2+ projects only): *.pipe.ts")
                .end(".pipe.ts"),
            ofFile("angular_directive", "/extra-icons/angular-directive.svg", "Angular (in Angular 2+ projects only): *.directive.ts")
                .end(".directive.ts"),
            ofFile("angular_guard", "/extra-icons/angular-guard.svg", "Angular (in Angular 2+ projects only): *.guard.ts")
                .end(".guard.ts"),
            ofFile("angular_resolver", "/extra-icons/angular-resolver.svg", "Angular (in Angular 2+ projects only): *.resolver.ts")
                .end(".resolver.ts"),
            ofFile("angular_spec", "/extra-icons/test-ts.svg", "Angular (in Angular 2+ projects only): *.spec.ts")
                .end(".spec.ts"),
            ofFile("angular_html", "/extra-icons/html5.svg", "Angular (in Angular 2+ projects only): *.html")
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
