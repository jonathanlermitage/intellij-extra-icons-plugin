// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

@SuppressWarnings("HardCodedStringLiteral")
public enum ModelTag {

    ANGULAR2("Angular", "extra-icons/angular2.svg"),
    DOCKER("Docker", "extra-icons/docker.svg"),
    FLYWAY("Flyway", "extra-icons/flyway.svg"),
    HELM("Helm", "extra-icons/helm.svg"),
    HTML("HTML", "extra-icons/html5.svg"),
    KUBERNETES("Kubernetes", "extra-icons/kubernetes.svg"),
    LIBRE_OFFICE("LibreOffice", "extra-icons/officedocs/lowriter.svg"),
    LIQUIBASE("Liquibase", "extra-icons/liquibase.svg"),
    MS_OFFICE("MS Office", "extra-icons/officedocs/msword-2019.svg"),
    NESTJS("NestJS", "extra-icons/nestjs.svg"),
    PRETTIER("Prettier", "extra-icons/prettier.svg"),
    STORYBOOK("Storybook", "extra-icons/storybook.svg"),
    TRAVIS("Travis CI", "extra-icons/travis.svg");

    ModelTag(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    private final String name;
    private final String icon;

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

}
