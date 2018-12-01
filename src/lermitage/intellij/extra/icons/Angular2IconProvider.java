package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.angular2.lang.Angular2LangUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Optional;

/**
 * Icon provider specific for Angular2+ modules.
 *
 * @author Edoardo Luppi
 */
class Angular2IconProvider extends IconProvider {
	private static final Model[] MODELS = {
			new Model("/icons/angular-module.svg").end(".module.ts"),
			new Model("/icons/angular-component.svg").end(".component.ts"),
			new Model("/icons/angular-service.svg").end(".service.ts"),
			new Model("/icons/angular-pipe.svg").end(".pipe.ts"),
			new Model("/icons/angular-directive.svg").end(".directive.ts"),
			new Model("/icons/angular-guard.svg").end(".guard.ts")
	};

	@Nullable
	@Override
	public Icon getIcon(@NotNull final PsiElement psiElement, final int flags) {
		final Optional<PsiFile> optFile = Optional.ofNullable(psiElement.getContainingFile());
		final PsiFile file;

		if (optFile.isPresent() && Angular2LangUtil.isAngular2Context(file = optFile.get())) {
			final String fileName = file.getName().toLowerCase();

			for (final Model model : MODELS) {
				if (model.check(fileName)) {
					return IconLoader.getIcon(model.getIcon());
				}
			}
		}

		return null;
	}
}
