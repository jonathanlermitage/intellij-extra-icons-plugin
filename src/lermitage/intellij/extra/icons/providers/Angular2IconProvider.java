package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import org.angular2.lang.Angular2LangUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Icon provider specific for Angular2+ modules.
 * This should be registered only if the bundled AngularJS plugin is enabled.
 *
 * @author Edoardo Luppi
 */
class Angular2IconProvider extends BaseIconProvider implements DumbAware {
	@NotNull
	@Override
	protected Model[] getModels() {
		return new Model[]{
				new Model("/icons/angular-module.svg").end(".module.ts"),
				new Model("/icons/angular-component.svg").end(".component.ts"),
				new Model("/icons/angular-service.svg").end(".service.ts"),
				new Model("/icons/angular-pipe.svg").end(".pipe.ts"),
				new Model("/icons/angular-directive.svg").end(".directive.ts"),
				new Model("/icons/angular-guard.svg").end(".guard.ts"),
				new Model("/icons/angular-resolver.svg").end(".resolver.ts"),
				new Model("/icons/test-ts.svg").end(".spec.ts"),
				new Model("/icons/html5.svg").end(".html")
		};
	}

	@Override
	protected boolean isSupported(@NotNull final PsiFile psiFile) {
		return Angular2LangUtil.isAngular2Context(psiFile);
	}
}
