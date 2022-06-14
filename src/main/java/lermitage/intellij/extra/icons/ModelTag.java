// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

public enum ModelTag {

    ANGULAR2("Angular"),
    DOCKER("Docker"),
    FLYWAY("Flyway"),
    HELM("Helm"),
    HTML("HTML"),
    KUBERNETES("Kubernetes"),
    LIBRE_OFFICE("LibreOffice"),
    LIQUIBASE("Liquibase"),
    MS_OFFICE("MS Office"),
    NESTJS("NestJS"),
    PRETTIER("Prettier"),
    STORYBOOK("Storybook"),
    TRAVIS("Travis CI");

    ModelTag(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
