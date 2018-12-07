package lermitage.intellij.extra.icons.providers;

import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import org.jetbrains.annotations.NotNull;

/**
 * Icon provider specific for Javascript-related files.
 * This should be registered only if the bundled Javascript Support plugin is enabled.
 *
 * @author Edoardo Luppi
 */
class JavascriptIconProvider extends BaseIconProvider implements DumbAware {
	@NotNull
	@Override
	protected Model[] getModels() {
		return new Model[]{
				new Model("/icons/test-ts.svg").end(".spec.ts")
		};
	}

	@Override
	protected boolean isSupported(@NotNull final PsiFile psiFile) {
		return true;
	}
}
