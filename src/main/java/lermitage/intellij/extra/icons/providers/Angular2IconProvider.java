// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.ExtraIconProvider;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.ModelTag;
import org.angular2.lang.Angular2LangUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return Stream.of(
                ofFile("angular_prj_module_generic", "extra-icons/angular-module.svg", "Angular (in Angular 2+ projects only): *.module.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]module\\.(js|ts)")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_prj_component_generic", "extra-icons/angular-component.svg", "Angular (in Angular 2+ projects only): *.component.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]component\\.(js|ts)")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_prj_controller_generic", "extra-icons/angular-controller.svg", "Angular (in Angular 2+ projects only): *.controller.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]controller\\.(js|ts)")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_prj_service_generic", "extra-icons/angular-service.svg", "Angular (in Angular 2+ projects only): *.service.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]service\\.(js|ts)")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_prj_pipe_generic", "extra-icons/angular-pipe.svg", "Angular (in Angular 2+ projects only): *.pipe.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]pipe\\.(js|ts)")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_prj_directive_generic", "extra-icons/angular-directive.svg", "Angular (in Angular 2+ projects only): *.directive.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]directive(s)?\\.(js|ts)")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_prj_guard_generic", "extra-icons/angular-guard.svg", "Angular (in Angular 2+ projects only): *.guard.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]guard\\.(js|ts)")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_prj_resolver_generic", "extra-icons/angular-resolver.svg", "Angular (in Angular 2+ projects only): *.resolver.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]resolver\\.(js|ts)")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_prj_spec_generic", "extra-icons/test-ts.svg", "Angular (in Angular 2+ projects only): *.spec.(js|ts|tsx)")
                    .regex(".*[^a-zA-Z\\d]spec\\.(js|ts|tsx)")
                    .altIcons("extra-icons/test-ts_alt.svg")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_prj_html", "extra-icons/html5.svg", "Angular (in Angular 2+ projects only): *.html")
                    .end(".html")
                    .tags(ModelTag.ANGULAR2, ModelTag.HTML)
            )
            .flatMap(ExtraIconProvider::modelList)
            .collect(Collectors.toList());
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
