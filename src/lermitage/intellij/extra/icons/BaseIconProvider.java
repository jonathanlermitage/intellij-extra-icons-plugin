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
	private Model[] models;

	/**
	 * Returns the Model(s) supported by this icon provider.
	 * This method will be called exactly once, so there is no need
	 * to provide a static array, as the array returned by this method is cached.
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
			final Model[] models = lazyGetModels();

			for (final Model model : models) {
				if (model.check(fileName)) {
					return IconLoader.getIcon(model.getIcon());
				}
			}
		}

		return null;
	}

	/**
	 * Lazily computes the Model(s) used by this icon provider.
	 * This should be already thread safe in the IDEA platform context.
	 */
	private Model[] lazyGetModels() {
		return models == null ? (models = getModels()) : models;
	}
}
