// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg.providers;


import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import lermitage.intellij.extra.icons.cfg.SettingsForm;
import org.jetbrains.annotations.Nullable;

public class IDEConfigurableProvider extends ConfigurableProvider {

    @Nullable
    @Override
    public Configurable createConfigurable() {
        return new SettingsForm();
    }
}
