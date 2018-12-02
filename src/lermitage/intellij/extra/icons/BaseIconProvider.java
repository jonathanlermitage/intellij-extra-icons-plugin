package lermitage.intellij.extra.icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Optional;

/**
 * @author Edoardo Luppi
 */
public abstract class BaseIconProvider extends IconProvider {
	/**
	 * Returns the Model(s) supported by this icon provider.
	 */
	@NotNull
	protected abstract Model[] getModels();

	/**
	 * Check whether this icon provider supports the input file.
	 */
	protected abstract boolean isSupported(@NotNull final PsiFile psiFile);

	@Nullable
	@Override
	public final Icon getIcon(@NotNull final PsiElement psiElement, final int flags) {
		final Optional<PsiFile> optFile = Optional.ofNullable(psiElement.getContainingFile());
		final PsiFile file;

		if (optFile.isPresent() && isSupported(file = optFile.get())) {
			final String fileName = file.getName().toLowerCase();
			final Model[] models = getModels();

			for (final Model model : models) {
				if (model.check(fileName)) {
					return IconLoader.getIcon(model.getIcon());
				}
			}
		}

		return null;
	}
}
