// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.cfg;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

@SuppressWarnings("HardCodedStringLiteral")
public class ArchTest {

    private static final JavaClasses PROJECT = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("lermitage..");

    /**
     * JetBrains Gateway supports only 'com.intellij.modules.platform' module.
     * We should NOT use modules like 'com.intellij.modules.platform.lang'.
     */
    @Test
    public void should_not_use_intellij_lang_module() {
        ArchRuleDefinition.classes()
            .should().onlyAccessClassesThat().resideOutsideOfPackages("com.intellij.lang..")
            .check(PROJECT);
    }

    @Test
    void should_not_depend_on_JDK_internals() {
        ArchRuleDefinition.classes()
            .should().onlyAccessClassesThat().resideOutsideOfPackages("com.sun..", "sun..")
            .check(PROJECT);
    }
}
