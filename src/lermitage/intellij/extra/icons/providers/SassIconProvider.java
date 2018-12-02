package lermitage.intellij.extra.icons.providers;

import com.intellij.psi.PsiFile;
import lermitage.intellij.extra.icons.BaseIconProvider;
import lermitage.intellij.extra.icons.Model;
import org.jetbrains.annotations.NotNull;

/**
 * Icon provider specific for SASS files.
 * This should be registered only if the bundled SASS plugin is enabled.
 *
 * @author Edoardo Luppi
 */
class SassIconProvider extends BaseIconProvider {
	@NotNull
	@Override
	protected Model[] getModels() {
		return new Model[]{
				new Model("/icons/sass.svg").end(".sass", ".scss")
		};
	}

	@Override
	protected boolean isSupported(@NotNull final PsiFile psiFile) {
		return true;
	}
}
