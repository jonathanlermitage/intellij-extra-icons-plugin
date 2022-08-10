// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.openapi.project.DumbAware;
import lermitage.intellij.extra.icons.enablers.IconEnablerType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static lermitage.intellij.extra.icons.Model.ofDir;
import static lermitage.intellij.extra.icons.Model.ofFile;
import static lermitage.intellij.extra.icons.Model.ofIcon;

@SuppressWarnings({"SpellCheckingInspection", "GrazieInspection"})
public class ExtraIconProvider extends BaseIconProvider implements DumbAware {

    private static final String[] TXT = new String[]{".md", ".txt", ".adoc", ".rst"};
    private static final String[] CFG = new String[]{".xml", ".yml", ".yaml", ".properties", ".json", ".cfg", ".conf", ".ini", ".txt"};
    private static final String[] YML = new String[]{".yaml", ".yml"};

    @NotNull
    public static List<Model> allModels() {
        return Stream.of(

                //
                // IDE icon (lowest priority)
                // Tip: to find IDE icon names, visit https://jetbrains.design/intellij/resources/icons_list/, pick
                // an icon and open the ZIP file: you can use the non-dark SVG file name.
                //
                // docker
                ofIcon("ide_docker",
                    "Docker.svg", "extra-icons/docker.svg",
                    "IDE icon: Docker")
                    .altIcons("extra-icons/docker_alt.png", "extra-icons/docker_alt2.svg")
                    .tags(ModelTag.DOCKER),
                // docker compose
                ofIcon("ide_docker_compose",
                    "DockerCompose.svg", "extra-icons/dockercompose.svg",
                    "IDE icon: Docker Compose")
                    .altIcons("extra-icons/dockercompose_alt.png", "extra-icons/dockercompose_alt2.svg")
                    .tags(ModelTag.DOCKER),
                // html
                ofIcon("ide_html",
                    "html.svg", "extra-icons/html5.svg",
                    "IDE icon: HTML")
                    .altIcons("extra-icons/html5_alt.svg", "extra-icons/html5_alt2.svg",
                        "extra-icons/ide/firefox.svg", "extra-icons/ide/firefoxDeveloper.svg",
                        "extra-icons/ide/vivaldi.svg", "extra-icons/ide/edge.svg",
                        "extra-icons/ide/explorer.svg", "extra-icons/ide/opera.svg",
                        "extra-icons/ide/chrome.svg", "extra-icons/ide/canary.svg",
                        "extra-icons/ide/safari.svg")
                    .tags(ModelTag.HTML),
                // java
                ofIcon("ide_java_enum_class",
                    "enum.svg", "extra-icons/ide/enum.svg",
                    "IDE icon: Java enum")
                    .altIcons("extra-icons/ide/enum_alt.svg", "extra-icons/ide/enum_alt2.svg"),
                ofIcon("ide_java_abstract_exception",
                    "abstractException.svg", "extra-icons/ide/abstractException.svg",
                    "IDE icon: Java abstract exception class"),
                ofIcon("ide_java_exception_class",
                    "exceptionClass.svg", "extra-icons/ide/exceptionClass.svg",
                    "IDE icon: Java exception class"),
                // kotlin
                ofIcon("ide_kotlin_enum_class",
                    "enumKotlin.svg", "extra-icons/ide/enumKotlin.svg",
                    "IDE icon: Kotlin enum")
                    .altIcons("extra-icons/ide/enumKotlin_alt.svg", "extra-icons/ide/enumKotlin_alt2.svg"),
                // kotlin gradle script
                ofIcon("ide_kotlin_gradle_script",
                    "kotlin_gradle_script.svg", "extra-icons/gradle_kotlin_colored.svg",
                    "IDE icon: Kotlin Gradle script")
                    .altIcons("extra-icons/gradle_kotlin.svg"),
                // yaml
                ofIcon("ide_sh_file",
                    "shFile.svg", "extra-icons/bash.svg",
                    "IDE icon: Bash file"),
                // yaml
                ofIcon("ide_yaml",
                    "yaml.svg", "extra-icons/ide/yaml.svg",
                    "IDE icon: YAML")
                    .altIcons("extra-icons/ide/yaml_alt.svg", "extra-icons/ide/yaml_alt2.svg"),

                //
                // folder
                //
                ofDir("dir_circleci", "extra-icons/folder_circleci.svg", "Circle CI: .circleci folder")
                    .eq(".circleci"),
                ofDir("dir_dependabot", "extra-icons/folder_dependabot.svg", "Dependabot: .dependabot folder")
                    .eq(".dependabot"),
                ofDir("dir_docker", "extra-icons/folder_docker.svg", "Docker: docker folder")
                    .eq("docker")
                    .tags(ModelTag.DOCKER),
                ofDir("dir_github", "extra-icons/folder_github.svg", "Github: .github folder")
                    .eq(".github"),
                ofDir("dir_gitlab", "extra-icons/folder_gitlab.svg", "Gitlab: .gitlab folder")
                    .eq(".gitlab"),
                ofDir("dir_gradle", "extra-icons/folder_gradle.svg", "Gradle: gradle folder")
                    .eq("gradle"),
                ofDir("dir_idea", "extra-icons/folder_idea.svg", "IntelliJ IDEA: .idea folder")
                    .eq(".idea"),
                ofDir("dir_ideasandbox", "extra-icons/folder_idea.svg", "IntelliJ IDEA: .idea-sandbox and idea-sandbox folders")
                    .eq(".idea-sandbox", "idea-sandbox"),
                ofDir("dir_idearun", "extra-icons/folder_idea.svg", "IntelliJ IDEA 2020+ Run/Debug Configurations: .run folder")
                    .eq(".run"),
                ofDir("dir_jpabuddy", "extra-icons/folder_jpabuddy.svg", "JPA Buddy: .jpb folder")
                    .eq(".jpb")
                    .altIcons("extra-icons/folder_jpabuddy_alt.svg", "extra-icons/folder_jpabuddy_alt2.svg",
                        "extra-icons/folder_jpabuddy_alt3.svg"),
                ofDir("dir_mergify", "extra-icons/folder_mergify.svg", "Mergify: .mergify folder")
                    .eq(".mergify"),
                ofDir("dir_mps", "extra-icons/folder_idea.svg", "JetBrains MPS: .mps folder")
                    .eq(".mps"),
                ofDir("dir_mvn", "extra-icons/folder_mvnw.svg", "Maven: .mvn folder")
                    .eq(".mvn"),
                ofDir("dir_node_modules", "extra-icons/folder_node_modules.svg", "NodeJS: node_modules folder")
                    .eq("node_modules")
                    .altIcons("extra-icons/folder_node_modules_alt.svg", "extra-icons/folder_tmp.svg"),
                ofDir("dir_nuget", "extra-icons/folder_nuget.svg", "Nuget: .nuget folder")
                    .eq(".nuget"),
                ofDir("dir_pytest_cache", "extra-icons/folder_tmp.svg", "PyTest cache: .pytest_cache folder")
                    .eq(".pytest_cache")
                    .altIcons("extra-icons/folder_pytest_cache.svg", "extra-icons/folder_pytest_cache_alt.svg"),
                ofDir("dir_python_venv", "extra-icons/folder_python_venv.svg", "Python Virtual Environment: .venv folder")
                    .eq(".venv")
                    .altIcons("extra-icons/folder_python_venv_alt.svg"),
                ofDir("dir_python_venv_nodot", "extra-icons/folder_python_venv.svg", "Python Virtual Environment: venv folder")
                    .eq("venv")
                    .altIcons("extra-icons/folder_python_venv_alt.svg"),
                ofDir("dir_python_egginfo", "extra-icons/folder_tmp.svg", "Python: .egg-info folder")
                    .end(".egg-info"),
                ofDir("dir_python_dottox", "extra-icons/folder_tmp.svg", "Python: .tox folder")
                    .eq(".tox"),
                ofDir("dir_teamcity", "extra-icons/folder_teamcity.svg", "TeamCity: .teamcity folder")
                    .eq(".teamcity"),
                ofDir("dir_vscode_settings", "extra-icons/folder_vscode.svg", "Visual Studio Code: .vscode folder")
                    .eq(".vscode"),
                ofDir("dir_semaphoreci", "extra-icons/folder_semaphoreci.svg", "Semaphore: .semaphore folder")
                    .eq(".semaphore"),
                ofDir("dir_expo", "extra-icons/expo_folder.svg", "Expo: .expo folder")
                    .eq(".expo"),
                ofDir("dir_storybook", "extra-icons/folder_storybook.svg", "Storybook: .storybook folder")
                    .eq(".storybook")
                    .tags(ModelTag.STORYBOOK),
                // - Flyway databases
                ofDir("dir_flyway", "extra-icons/folder_flyway.svg", "Flyway (regex): '.*/db/migration/?'")
                    .regex(".*/db/migration/?")
                    .tags(ModelTag.FLYWAY),
                ofDir("dir_flyway_db2", "extra-icons/folder_db2.svg", "Flyway, IBM DB2 database folder (regex): '.*/db/migration/db2[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/migration/db2[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.FLYWAY),
                ofDir("dir_flyway_h2", "extra-icons/folder_h2.svg", "Flyway, H2 database folder (regex): '.*/db/migration/h2[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/migration/h2[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.FLYWAY),
                ofDir("dir_flyway_hsqldb", "extra-icons/folder_hsqldb.svg", "Flyway, HSQLDB database folder (regex): '.*/db/migration/hsqldb[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/migration/hsqldb[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.FLYWAY),
                ofDir("dir_flyway_mariadb", "extra-icons/folder_mariadb.svg", "Flyway, MariaDB database folder (regex): '.*/db/migration/maria[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/migration/maria[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.FLYWAY),
                ofDir("dir_flyway_mysql", "extra-icons/folder_mysql.svg", "Flyway, MySQL database folder (regex): '.*/db/migration/mysql[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/migration/mysql[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.FLYWAY),
                ofDir("dir_flyway_postgresql", "extra-icons/folder_postgresql.svg", "Flyway, PostgreSQL database folder (regex): '.*/db/migration/postgre[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/migration/postgre[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.FLYWAY),
                ofDir("dir_flyway_sqlite", "extra-icons/folder_sqlite.svg", "Flyway, Sqlite database folder (regex): '.*/db/migration/sqlite[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/migration/sqlite[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.FLYWAY),
                ofDir("dir_flyway_oracle", "extra-icons/folder_oracle.svg", "Flyway, Oracle database folder (regex): '.*/db/migration/oracle[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/migration/oracle[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.FLYWAY),
                ofDir("dir_flyway_sqlserver", "extra-icons/folder_sqlserver.svg", "Flyway, MS SQL Server database folder (regex): '.*/db/migration/sqlserver[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/migration/sqlserver[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.FLYWAY),
                ofDir("dir_flyway_mssql", "extra-icons/folder_sqlserver.svg", "Flyway, MS SQL Server database folder (regex): '.*/db/migration/mssql[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/migration/mssql[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.FLYWAY),
                // - Liquibase databases
                ofDir("dir_liquibase", "extra-icons/folder_liquibase.svg", "Liquibase (regex): '.*/db/changelog/?'")
                    .regex(".*/db/changelog/?")
                    .tags(ModelTag.LIQUIBASE),
                ofDir("dir_liquibase_db2", "extra-icons/folder_db2.svg", "Liquibase, IBM DB2 database folder (regex): '.*/db/changelog/db2[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/changelog/db2[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.LIQUIBASE),
                ofDir("dir_liquibase_h2", "extra-icons/folder_h2.svg", "Liquibase, H2 database folder (regex): '.*/db/changelog/h2[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/changelog/h2[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.LIQUIBASE),
                ofDir("dir_liquibase_hsqldb", "extra-icons/folder_hsqldb.svg", "Liquibase, HSQLDB database folder (regex): '.*/db/changelog/hsqldb[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/changelog/hsqldb[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.LIQUIBASE),
                ofDir("dir_liquibase_mariadb", "extra-icons/folder_mariadb.svg", "Liquibase, MariaDB database folder (regex): '.*/db/changelog/maria[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/changelog/maria[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.LIQUIBASE),
                ofDir("dir_liquibase_mysql", "extra-icons/folder_mysql.svg", "Liquibase, MySQL database folder (regex): '.*/db/changelog/mysql[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/changelog/mysql[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.LIQUIBASE),
                ofDir("dir_liquibase_postgresql", "extra-icons/folder_postgresql.svg", "Liquibase, PostgreSQL database folder (regex): '.*/db/changelog/postgre[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/changelog/postgre[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.LIQUIBASE),
                ofDir("dir_liquibase_sqlite", "extra-icons/folder_sqlite.svg", "Liquibase, Sqlite database folder (regex): '.*/db/changelog/sqlite[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/changelog/sqlite[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.LIQUIBASE),
                ofDir("dir_liquibase_oracle", "extra-icons/folder_oracle.svg", "Liquibase, Oracle database folder (regex): '.*/db/changelog/oracle[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/changelog/oracle[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.LIQUIBASE),
                ofDir("dir_liquibase_sqlserver", "extra-icons/folder_sqlserver.svg", "Liquibase, MS SQL Server database folder (regex): '.*/db/changelog/sqlserver[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/changelog/sqlserver[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.LIQUIBASE),
                ofDir("dir_liquibase_mssql", "extra-icons/folder_sqlserver.svg", "Liquibase, MS SQL Server database folder (regex): '.*/db/changelog/mssql[a-zA-Z0-9._\\-]*'")
                    .regex(".*/db/changelog/mssql[a-zA-Z\\d._\\-]*")
                    .tags(ModelTag.LIQUIBASE),

                //
                // programmable
                //
                // git
                ofDir("gitsubmodule", "extra-icons/git_submodule.svg", "Git sub-module containing folder")
                    .iconEnabler(IconEnablerType.IS_GIT_SUBMODULE_FOLDER)
                    .altIcons("extra-icons/git_submodule_alt.svg"),
                // helm
                ofDir("helm_dir", "extra-icons/helmDirectory.svg", "Helm chart containing folder")
                    .iconEnabler(IconEnablerType.IS_HELM_FOLDER)
                    .altIcons("extra-icons/helmlogo.svg")
                    .tags(ModelTag.HELM),
                ofFile("helm_chart_yaml", "extra-icons/helmChart.svg", "Helm: Chart.yaml")
                    .eq("chart.yaml")
                    .iconEnabler(IconEnablerType.IS_IN_HELM_FOLDER)
                    .tags(ModelTag.HELM),
                ofFile("helm_requirements_yaml", "extra-icons/helmRequirementsYaml.svg", "Helm: requirements.yaml")
                    .eq("requirements.yaml")
                    .iconEnabler(IconEnablerType.IS_IN_HELM_FOLDER)
                    .tags(ModelTag.HELM),
                ofFile("helm_values_yaml", "extra-icons/helmValues.svg", "Helm: values.yaml")
                    .eq("values.yaml")
                    .iconEnabler(IconEnablerType.IS_IN_HELM_FOLDER)
                    .tags(ModelTag.HELM),
                ofDir("helm_charts_dir", "extra-icons/helmChartsDirectory.svg", "Helm: charts folder")
                    .eq("charts")
                    .iconEnabler(IconEnablerType.IS_IN_HELM_FOLDER)
                    .tags(ModelTag.HELM),
                ofDir("helm_templates_dir", "extra-icons/helmTemplatesDirectory.svg", "Helm: templates folder")
                    .eq("templates")
                    .iconEnabler(IconEnablerType.IS_IN_HELM_FOLDER)
                    .tags(ModelTag.HELM),
                ofFile("helm_helpers.tpl", "extra-icons/helmUndeployable.svg", "Helm: templates/_helpers.tpl")
                    .eq("_helpers.tpl")
                    .parents("templates")
                    .iconEnabler(IconEnablerType.IS_IN_HELM_FOLDER)
                    .tags(ModelTag.HELM),
                ofFile("helm_notes_txt", "extra-icons/helmNotes.svg", "Helm: templates/NOTES.txt")
                    .eq("notes.txt")
                    .parents("templates")
                    .iconEnabler(IconEnablerType.IS_IN_HELM_FOLDER)
                    .tags(ModelTag.HELM),
                ofFile("helm_all_yaml", "extra-icons/helm.svg", "Helm: *.yaml")
                    .end(".yaml")
                    .iconEnabler(IconEnablerType.IS_IN_HELM_FOLDER)
                    .altIcons("extra-icons/helmlogo.svg")
                    .tags(ModelTag.HELM),

                // angular
                ofFile("angular_json", "extra-icons/angular2.svg", "Angular: angular.json")
                    .eq("angular.json")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_module_generic", "extra-icons/angular-module.svg", "Angular (if angular.json exists): *.module.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]module\\.(js|ts)")
                    .iconEnabler(IconEnablerType.IS_IN_ANGULAR_FOLDER)
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_component_generic", "extra-icons/angular-component.svg", "Angular (if angular.json exists): *.component.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]component\\.(js|ts)")
                    .iconEnabler(IconEnablerType.IS_IN_ANGULAR_FOLDER)
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_controller_generic", "extra-icons/angular-controller.svg", "Angular (if angular.json exists): *.controller.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]controller\\.(js|ts)")
                    .iconEnabler(IconEnablerType.IS_IN_ANGULAR_FOLDER)
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_service_generic", "extra-icons/angular-service.svg", "Angular (if angular.json exists): *.service.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]service\\.(js|ts)")
                    .iconEnabler(IconEnablerType.IS_IN_ANGULAR_FOLDER)
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_pipe_generic", "extra-icons/angular-pipe.svg", "Angular (if angular.json exists): *.pipe.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]pipe\\.(js|ts)")
                    .iconEnabler(IconEnablerType.IS_IN_ANGULAR_FOLDER)
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_directive_generic", "extra-icons/angular-directive.svg", "Angular (if angular.json exists): *.directive.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]directive(s)?\\.(js|ts)")
                    .iconEnabler(IconEnablerType.IS_IN_ANGULAR_FOLDER)
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_guard_generic", "extra-icons/angular-guard.svg", "Angular (if angular.json exists): *.guard.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]guard\\.(js|ts)")
                    .iconEnabler(IconEnablerType.IS_IN_ANGULAR_FOLDER)
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_resolver_generic", "extra-icons/angular-resolver.svg", "Angular (if angular.json exists): *.resolver.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]resolver\\.(js|ts)")
                    .iconEnabler(IconEnablerType.IS_IN_ANGULAR_FOLDER)
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_spec_generic", "extra-icons/test-ts.svg", "Angular (if angular.json exists): *.spec.(js|ts|tsx)")
                    .regex(".*[^a-zA-Z\\d]spec\\.(js|ts|tsx)")
                    .iconEnabler(IconEnablerType.IS_IN_ANGULAR_FOLDER)
                    .altIcons("extra-icons/test-ts_alt.svg")
                    .tags(ModelTag.ANGULAR2),
                ofFile("angular_html", "extra-icons/html5.svg", "Angular (if angular.json exists): *.html")
                    .end(".html")
                    .iconEnabler(IconEnablerType.IS_IN_ANGULAR_FOLDER)
                    .tags(ModelTag.ANGULAR2, ModelTag.HTML),

                // nestjs
                ofFile("nestjs", "extra-icons/nestjs.svg", "NestJS: .nest-cli.json, nest-cli.json, nestconfig.json, .nestconfig.json")
                    .eq(".nest-cli.json", "nest-cli.json", "nestconfig.json", ".nestconfig.json")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_adapter", "extra-icons/nestjs_adapter.svg", "NestJS: *.adapter.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]adapter\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_controller", "extra-icons/nestjs_controller.svg", "NestJS: *.controller.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]controller\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_controller_test", "extra-icons/nestjs_controller_test.svg", "NestJS: *.controller.spec.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]controller\\.spec\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_decorator", "extra-icons/nestjs_decorator.svg", "NestJS: *.decorator.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]decorator\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_dto", "extra-icons/nestjs_dto.svg", "NestJS: *.dto.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]dto\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_entity", "extra-icons/nestjs_entity.svg", "NestJS: *.entity.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]entity\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_filter", "extra-icons/nestjs_filter.svg", "NestJS: *.filter.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]filter\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_gateway", "extra-icons/nestjs_gateway.svg", "NestJS: *.gateway.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]gateway\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_guard", "extra-icons/nestjs_guard.svg", "NestJS: *.guard.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]guard\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_interceptor", "extra-icons/nestjs_interceptor.svg", "NestJS: *.interceptor.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]interceptor\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_middleware", "extra-icons/nestjs_middleware.svg", "NestJS: *.middleware.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]middleware\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_model", "extra-icons/nestjs_model.svg", "NestJS: *.model.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]model\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_module", "extra-icons/nestjs_module.svg", "NestJS: *.module.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]module\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_pipe", "extra-icons/nestjs_pipe.svg", "NestJS: *.pipe.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]pipe\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_service", "extra-icons/nestjs_service.svg", "NestJS: *.service.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]service\\.(js|ts)")
                    .tags(ModelTag.NESTJS),
                ofFile("nestjs_service_test", "extra-icons/nestjs_service_test.svg", "NestJS: *.service.spec.(js|ts)")
                    .regex(".*[^a-zA-Z\\d]service\\.spec\\.(js|ts)")
                    .tags(ModelTag.NESTJS),

                //
                // regex (file)
                //
                ofFile("babel", "extra-icons/babel.svg", "Babel: babel.config.[json|js|cjs|mjs]|.babelrc.[json|js|cjs|mjs]|.babelrc")
                    .regex(".*/babel\\.config\\.(?:js(?:on)?|[cm]js)|\\.babelrc(?:\\.(?:js(?:on)?|[cm]js))?")
                    .altIcons("extra-icons/babel_alt.svg"),
                ofFile("cookie", "extra-icons/cookie.svg", "Cookie (regex): .*cookie[a-zA-Z\\d_-]*\\.txt")
                    .regex(".*cookie[a-zA-Z\\d_-]*\\.txt"),
                ofFile("dotenv", "extra-icons/env.svg", "Dotenv: .*\\.env(\\.[a-zA-Z0-9]+)*")
                    .regex(".*\\.env(\\.[a-zA-Z\\d]+)*")
                    .altIcons("extra-icons/env_alt.svg", "extra-icons/env_alt2.svg"),
                ofFile("eslint", "extra-icons/eslint.svg", "Eslint (regex): .*\\.eslintrc(\\.[cm]?js(on)?|\\.y(a)?ml)?$")
                    .regex(".*\\.eslintrc(\\.[cm]?js(on)?|\\.y(a)?ml)?$"),
                ofFile("nodemon", "extra-icons/nodemon.svg", "Nodemon: ^\\.?nodemon\\.json$")
                    .regex(".^\\.?nodemon\\.json$"),
                ofFile("tsconfig", "extra-icons/tsconfig.svg", "TSConfig: .*/\\.?tsconfig.*\\.(js|json)$")
                    .regex(".*/\\.?tsconfig.*\\.(js|json)$"),
                ofFile("jsconfig", "extra-icons/jsconfig.svg", "JSConfig: .*/\\.?jsconfig.*\\.(js|json)$")
                    .regex(".*/\\.?jsconfig.*\\.(js|json)$"),
                ofFile("typescript-def", "extra-icons/typescript-def.svg", "TypescriptDef: .*\\.d\\.ts$")
                    .regex(".*\\.d\\.ts$"),
                ofFile("post-css", "extra-icons/postcss.svg", "PostCSS: .*\\.(pcss|postcss)$")
                    .regex(".*\\.(pcss|postcss)$"),
                ofFile("post-css-config", "extra-icons/postcss.svg", "PostCSS Configuration: .*(\\.postcssrc|postcss\\.config)(\\.[cm]?js|ts|json|ya?ml)?$")
                    .regex(".*(\\.postcssrc|postcss\\.config)(\\.[cm]?js|ts|json|ya?ml)?$"),
                ofFile("sequelize", "extra-icons/sequelize.svg", "Sequelize: .*\\.sequelizerc(\\.[cm]?js|\\.json)?$")
                    .regex(".*\\.sequelizerc(\\.[cm]?js|\\.json)?$"),
                ofFile("svelte", "extra-icons/svelte.svg", "Svelte: .*\\.(svelte|svx)")
                    .regex(".*\\.(svelte|svx)"),
                ofFile("tailwindcss", "extra-icons/tailwindcss.svg", "Tailwindcss Configuration: .*\\.?tailwind.*\\.[cm]?[jt]s$")
                    .regex(".*\\.?tailwind.*\\.[cm]?[jt]s$"),
                ofFile("test-typescript", "extra-icons/test-ts.svg", "TestTypescript: .*\\.?(test|spec)\\.(ts|tsx)$")
                    .regex(".*\\.?(test|spec)\\.(ts|tsx)$")
                    .altIcons("extra-icons/test-ts_alt.svg"),
                ofFile("test-javascript", "extra-icons/test-javascript.svg", "TestJavascript: .*\\.?(test|spec)\\.js$")
                    .regex(".*\\.?(test|spec)\\.js$")
                    .altIcons("extra-icons/test-javascript_alt.svg"),
                ofFile("flyway", "extra-icons/flyway.svg", "Flyway (regex): '.*/db/migration/.*\\.sql'")
                    .regex(".*/db/migration/.*\\.sql")
                    .tags(ModelTag.FLYWAY)
                    .altIcons("extra-icons/flyway_alt.svg", "extra-icons/flyway_alt2.png"),
                ofFile("liquibase", "extra-icons/liquibase.svg", "Liquibase (regex): '.*/db/changelog/.*\\.(sql|xml)'")
                    .regex(".*/db/changelog/.*\\.(sql|xml)")
                    .tags(ModelTag.LIQUIBASE)
                    .altIcons("extra-icons/liquibase_alt.svg"),
                ofFile("mockk", "extra-icons/mockk.svg", "Mockk: io/mockk/settings.properties")
                    .regex(".*/io/mockk/settings\\.properties"),
                ofFile("storybook", "extra-icons/storybook.svg", "Storybook: *.stor(y|ies).(js|jsx|ts|tsx|mdx)")
                    .regex(".*\\.stor(y|ies)\\.(js|jsx|ts|tsx|mdx)$")
                    .tags(ModelTag.STORYBOOK),

                //
                // file plus its containing folder
                //
                ofFile("dependabot_config", "extra-icons/dependabot.svg", "Dependabot: .dependabot/config.yml")
                    .eq("config.yml").parents(".dependabot"),
                ofFile("dependabot", "extra-icons/dependabot.svg", "Dependabot: .github/dependabot.yml")
                    .eq("dependabot").end(YML).parents(".github"),
                ofFile("golwroot", "extra-icons/glowroot.png", "Glowroot: glowroot/admin.json and glowroot/config.json")
                    .eq("admin.json", "config.json").parents("glowroot"),
                ofFile("jgitver", "extra-icons/jgitver.svg", "jgitver: .mvn/jgitver.config.xml")
                    .eq("jgitver.config.xml").parents(".mvn"),
                ofFile("mergify1", "extra-icons/mergify.svg", "Mergify: .mergify.yml")
                    .eq(".mergify.yml"),
                ofFile("mergify2", "extra-icons/mergify.svg", "Mergify: .mergify/config.yml")
                    .eq("config.yml").parents(".mergify"),
                ofFile("mergify3", "extra-icons/mergify.svg", "Mergify: .github/mergify.yml")
                    .eq("mergify.yml").parents(".github"),
                ofFile("nuget", "extra-icons/nuget.svg", "Nuget: .nuget/packages.config")
                    .eq("packages.config").parents(".nuget"),
                ofFile("onap_cba_blueprint", "extra-icons/onap-cba-blueprint.svg", "ONAP CBA: Definitions/*blueprint.json")
                    .end("blueprint.json").parents("definitions"),
                ofFile("onap_cba_definitions", "extra-icons/onap-cba-definitions.svg", "ONAP CBA: Definitions/*(artifact_types, data_types, node_types, relationship_types, resources_definition_types).json")
                    .end("artifact_types.json", "data_types.json", "node_types.json", "relationship_types.json", "resources_definition_types.json").parents("definitions"),
                ofFile("onap_cba_tosca", "extra-icons/onap-cba-tosca.svg", "ONAP CBA: TOSCA-Metadata/*TOSCA.meta")
                    .end("tosca.meta").parents("tosca-metadata"),
                ofFile("semaphoreci", "extra-icons/semaphoreci.svg", "Semaphore: .semaphore/semaphore.yml")
                    .eq("semaphore.yml").parents(".semaphore"),
                ofFile("vscode_settings", "extra-icons/vscode.svg", "Visual Studio Code: .vscode/settings.json")
                    .eq("settings.json").parents(".vscode"),

                //
                // file plus extension
                //
                ofFile("vcskeep", "extra-icons/keep.svg", "Various VCS: .keep, .gitkeep, .hgkeep, .svnkeep")
                    .eq(".keep", ".gitkeep", ".hgkeep", ".svnkeep"),
                ofFile("htaccess", "extra-icons/htaccess.svg", "Apache: .htaccess")
                    .eq(".htaccess"),
                ofFile("android_manifest", "extra-icons/android.svg", "Android: androidmanifest.xml")
                    .eq("androidmanifest.xml")
                    .altIcons("extra-icons/android_alt.svg"),
                ofFile("appveyor", "extra-icons/appveyor.svg", "Appveyor: appveyor.yml, .appveyor.yml")
                    .eq("appveyor.yml", ".appveyor.yml"),
                ofFile("allcontributors", "extra-icons/allcontributors.svg", "All Contributors: .all-contributorsrc")
                    .eq(".all-contributorsrc"),
                ofFile("archunit", "extra-icons/archunit.png", "ArchUnit: archunit.properties")
                    .eq("archunit.properties"),
                ofFile("author", "extra-icons/authors.png", "Author: author(.md,.txt,.adoc,.rst), authors")
                    .eq("author", "authors").mayEnd(TXT)
                    .altIcons("extra-icons/authors_alt.svg", "extra-icons/authors_alt2.svg",
                        "extra-icons/authors_alt3.svg", "extra-icons/authors_alt4.svg"),
                ofFile("bamboo", "extra-icons/bamboo.png", "Bamboo: bamboo.yml")
                    .eq("bamboo.yml"),
                ofFile("caddyfile", "extra-icons/caddy.svg", "Caddy: Caddyfile")
                    .eq("caddyfile"),
                ofFile("cargo", "extra-icons/cargo.png", "Cargo: cargo.toml")
                    .eq("cargo.toml"),
                ofFile("commitlint", "extra-icons/commitlint.svg", "Commitlint: .commitlintrc, .commitlintrc.json, .commitlintrc.yaml, etc.")
                    .eq(".commitlintrc", ".commitlintrc.json", ".commitlintrc.yaml", ".commitlintrc.yml",
                        ".commitlintrc.js", ".commitlintrc.cjs", ".commitlintrc.ts", "commitlint.config.js",
                        "commitlint.config.cjs", "commitlint.config.ts"),
                ofFile("gradle_main", "extra-icons/gradle.svg", "Gradle: build.gradle, gradle.properties, settings.gradle")
                    .eq("build.gradle", "gradle.properties", "settings.gradle"),
                ofFile("gradle_kotlin", "extra-icons/gradle_kotlin_colored.svg", "Gradle (Kotlin DSL): build.gradle.kts, settings.gradle.kts")
                    .eq("build.gradle.kts", "settings.gradle.kts")
                    .altIcons("extra-icons/gradle_kotlin.svg"),
                ofFile("gradle", "extra-icons/gradlew.svg", "Gradle: gradlew")
                    .eq("gradlew"),
                ofFile("gradlew", "extra-icons/gradlew.svg", "Gradle (Windows): gradlew.bat, gradlew.cmd")
                    .eq("gradlew.bat", "gradlew.cmd"),
                ofFile("gradle_wrapper", "extra-icons/gradle.svg", "Gradle Wrapper: gradle-wrapper.jar, gradle-wrapper.properties")
                    .eq("gradle-wrapper.jar", "gradle-wrapper.properties"),
                ofFile("gradle_lock", "extra-icons/gradle_lock.svg", "Gradle dependency versions lock: gradle.lockfile")
                    .eq("gradle.lockfile"),
                ofFile("gradle_buildscriptlock", "extra-icons/gradle_lock.svg", "Gradle dependency versions lock: buildscript-gradle.lockfile")
                    .eq("buildscript-gradle.lockfile"),
                ofFile("mvnw_linux", "extra-icons/mvnw.svg", "Maven: mvnw")
                    .eq("mvnw"),
                ofFile("berksfile", "extra-icons/berkshelf.png", "Berkshelf: berksfile")
                    .eq("berksfile"),
                ofFile("berksfilelock", "extra-icons/berkshelflock.png", "Berkshelf: berksfile.lock")
                    .eq("berksfile.lock"),
                ofFile("bettercodehub", "extra-icons/bettercodehub.svg", "Better Code Hub: .bettercodehub.yml")
                    .eq(".bettercodehub.yml"),
                ofFile("bitbucket", "extra-icons/bitbucket.svg", "Bitbucket: bitbucket-pipelines.yml")
                    .eq("bitbucket-pipelines").end(YML),
                ofFile("bower", "extra-icons/bower.svg", "Bower: bower.json, .bowerrc")
                    .eq("bower.json", ".bowerrc"),
                ofFile("browserslist", "extra-icons/browserslist.svg", "Browserslist: browserslist, .browserslistrc")
                    .eq("browserslist", ".browserslistrc"),
                ofFile("build", "extra-icons/build.png", "Build: build(.md,.txt,.adoc,.rst), building")
                    .eq("build", "building").mayEnd(TXT),
                ofFile("cache2k", "extra-icons/cache2k.svg", "cache2k: cache2k.xml")
                    .eq("cache2k.xml"),
                ofFile("cassandra", "extra-icons/cassandra.svg", "Cassandra: cassandra(.yml,.yaml)")
                    .eq("cassandra").mayEnd(YML),
                ofFile("cassandra1", "extra-icons/cassandra.svg", "Cassandra: start by 'cassandra' and end by '.yml,.yaml'")
                    .start("cassandra").end(YML),
                ofFile("changelog", "extra-icons/changelog.svg", "Changelog: changelog(.md,.txt,.adoc,.rst), changes, release-notes, release_notes, releasenotes")
                    .eq("changelog", "changes", "release-notes", "release_notes", "releasenotes").mayEnd(TXT),
                ofFile("circle", "extra-icons/circleci.svg", "Circle CI: circle.yml")
                    .eq("circle.yml"),
                ofFile("circle1", "extra-icons/circleci.svg", "Circle CI: .circleci/config.yml")
                    .eq("config.yml").parents(".circleci"),
                ofFile("cirrus", "extra-icons/cirrus.png", "Cirrus CI: .cirrus.yml")
                    .eq(".cirrus.yml"),
                ofFile("composer", "extra-icons/composer.svg", "Composer: composer.json, composer.lock")
                    .eq("composer.json", "composer.lock"),
                ofFile("cypress", "extra-icons/cypress.svg", "Cypress: cypress.json")
                    .eq("cypress.json"),
                ofFile("mvnw_windows", "extra-icons/mvnw.svg", "Maven (Windows): mvnw.bat, mvnw.cmd")
                    .eq("mvnw.bat", "mvnw.cmd"),
                ofFile("codecov", "extra-icons/codecov.svg", "CodeCov: .codecov.yml, codecov.yml")
                    .eq(".codecov.yml", "codecov.yml"),
                ofFile("codefresh", "extra-icons/codefresh.png", "Codefresh: codefresh.yml")
                    .eq("codefresh.yml"),
                ofFile("codacy", "extra-icons/codacy.png", "Codacy: .codacy(.yml,.yaml)")
                    .eq(".codacy").mayEnd(YML),
                ofFile("codeship", "extra-icons/codeship.svg", "Codeship steps: codeship-steps(.xml,.yml,...)")
                    .eq("codeship-steps").mayEnd(CFG)
                    .altIcons("extra-icons/codeship_alt.png"),
                ofFile("contact", "extra-icons/contact.png", "Contact: contact(.md,.txt,.adoc,.rst), contacts")
                    .eq("contact", "contacts").mayEnd(TXT),
                ofFile("contribute", "extra-icons/contributing.svg", "Contribution: contribute(.md,.txt,.adoc,.rst), contributing, contribution")
                    .eq("contribute", "contributing", "contribution").mayEnd(TXT)
                    .altIcons("extra-icons/contributing_alt.png", "extra-icons/contributing_alt2.svg"),
                ofFile("coveragerc", "extra-icons/coveragerc.svg", "Python coverage: .coveragerc")
                    .eq(".coveragerc"),
                ofFile("crowdin", "extra-icons/crowdin.svg", "Crowdin: .crowdin.yml")
                    .eq(".crowdin.yml"),
                ofFile("deno", "extra-icons/deno.svg", "Deno: deno.json, deno.jsonc")
                    .eq("deno.json", "deno.jsonc"),
                ofFile("deptective", "extra-icons/deptective.png", "Deptective: deptective.json")
                    .eq("deptective.json"),
                ofFile("docker", "extra-icons/docker.svg", "Docker: dockerfile(.xml,.yml,...)")
                    .eq("dockerfile").mayEnd(CFG)
                    .altIcons("extra-icons/docker_alt.png", "extra-icons/docker_alt2.svg")
                    .tags(ModelTag.DOCKER),
                ofFile("dockercompose", "extra-icons/dockercompose.svg", "Docker: docker-compose")
                    .eq("docker-compose").mayEnd(CFG)
                    .altIcons("extra-icons/dockercompose_alt.png", "extra-icons/dockercompose_alt2.svg")
                    .tags(ModelTag.DOCKER),
                ofFile("docker1", "extra-icons/docker.svg", "Docker: start by 'dockerfile' and end by '.xml,.yml,...'")
                    .start("dockerfile").end(CFG)
                    .altIcons("extra-icons/docker_alt.png", "extra-icons/docker_alt2.svg")
                    .tags(ModelTag.DOCKER),
                ofFile("dockercompose1", "extra-icons/dockercompose.svg", "Docker: start by 'docker-compose' and end by '.xml,.yml,...'")
                    .start("docker-compose").end(CFG)
                    .altIcons("extra-icons/dockercompose_alt.png", "extra-icons/dockercompose_alt2.svg")
                    .tags(ModelTag.DOCKER),
                ofFile("dockerignore", "extra-icons/dockerignore.svg", "Docker: .dockerignore")
                    .eq(".dockerignore")
                    .altIcons("extra-icons/dockerignore_alt.png", "extra-icons/dockerignore_alt2.svg")
                    .tags(ModelTag.DOCKER),
                ofFile("editorconfig", "extra-icons/editorconfig.png", "EditorConfig: .editorconfig")
                    .eq(".editorconfig").mayEnd(CFG)
                    .altIcons("extra-icons/editorconfig_alt.svg", "extra-icons/editorconfig_alt2.svg"),
                ofFile("elastic", "extra-icons/elasticsearch.png", "Elasticsearch: elastic(.yml,.yaml)")
                    .eq("elastic").mayEnd(YML),
                ofFile("elastic1", "extra-icons/elasticsearch.png", "Elasticsearch: start by 'elastic' and end by '.yml,.yaml'")
                    .start("elastic").end(YML),
                ofFile("expo_eas", "extra-icons/expo.svg", "Expo EAS: eas.json")
                    .eq("eas.json"),
                ofFile("cerebro", "extra-icons/elastic-cerebro.png", "Cerebro: cerebro(.conf)")
                    .eq("cerebro").mayEnd(".conf"),
                ofFile("cerebro1", "extra-icons/elastic-cerebro.png", "Cerebro: start by 'cerebro' and end by '.conf'")
                    .start("cerebro").end(".conf"),
                ofFile("faq", "extra-icons/faq.svg", "FAQ: faq(.md,.txt,.adoc,.rst)")
                    .eq("faq").mayEnd(TXT)
                    .altIcons("extra-icons/faq_alt.svg", "extra-icons/faq_alt2.svg",
                        "extra-icons/faq_alt3.svg", "extra-icons/faq_alt4.svg"),
                ofFile("help", "extra-icons/faq.svg", "HELP: help(.md,.txt,.adoc,.rst)")
                    .eq("help").mayEnd(TXT)
                    .altIcons("extra-icons/faq_alt.svg", "extra-icons/faq_alt2.svg",
                        "extra-icons/faq_alt3.svg", "extra-icons/faq_alt4.svg"),
                ofFile("intellijcodestyle", "extra-icons/jetbrains.svg", "IntelliJ: intellijcodestyle.xml")
                    .eq("intellijcodestyle.xml"),
                ofFile("mailmap", "extra-icons/email.svg", "Mailmap: .mailmap")
                    .eq(".mailmap"),
                ofFile("msazure", "extra-icons/msazure.svg", "Microsoft Azure: azure-pipelines.yml")
                    .eq("azure-pipelines.yml"),
                ofFile("msazure1", "extra-icons/msazure.svg", "Microsoft Azure: start by 'azure-pipelines' and end by '.yml'")
                    .start("azure-pipelines").end(".yml"),
                ofFile("gatling", "extra-icons/gatling.png", "Gatling: gatling(.conf)")
                    .eq("gatling").mayEnd(".conf"),
                ofFile("gatling1", "extra-icons/gatling.png", "Gatling: start by 'gatling' and end by '.conf'")
                    .start("gatling").end(".conf"),
                ofFile("gitpod", "extra-icons/gitpod.svg", "Gitpod: .gitpod.yml")
                    .start(".gitpod").end(YML),
                ofFile("golangarchlinter", "extra-icons/golangarchlinter.svg", "GoLang Arch Linter: .go-arch-lint.yml")
                    .start(".go-arch-lint.yml"),
                ofFile("goreleaser", "extra-icons/goreleaser.svg", "GoReleaser: .goreleaser.yml")
                    .start(".goreleaser.yml")
                    .altIcons("extra-icons/goreleaser_alt.svg"),
                ofFile("grunt", "extra-icons/grunt.svg", "Grunt: Gruntfile.js")
                    .eq("gruntfile.js"),
                ofFile("git", "extra-icons/git.svg", "Git: .gitattributes, .gitignore, .gitmodules, .gitreview")
                    .eq(".gitattributes", ".gitignore", ".gitmodules", ".gitreview"),
                ofFile("gitlab", "extra-icons/gitlab.svg", "Gitlab: .gitlab-ci.yml")
                    .eq(".gitlab-ci.yml")
                    .altIcons("extra-icons/gitlab_alt.svg"),
                ofFile("grafana", "extra-icons/grafana.svg", "Grafana: grafana.ini")
                    .eq("grafana.ini"),
                ofFile("graphqlconfig", "extra-icons/graphql_config.svg", "GraphQL: graphql.config.json")
                    .eq("graphql.config.json"),
                ofFile("graphqlschema", "extra-icons/graphql_schema.svg", "GraphQL: graphql.schema.json")
                    .eq("graphql.schema.json"),
                ofFile("gocd", "extra-icons/gocd.png", "Gocd: .gocd(.xml,.yml,...)")
                    .eq(".gocd").mayEnd(CFG),
                ofFile("hadolint", "extra-icons/hadolint.png", "Hadolint: .hadolint.yaml, .hadolint.yml")
                    .eq(".hadolint").end(YML),
                ofFile("hibernate", "extra-icons/hibernate.svg", "Hibernate: hibernate.properties")
                    .eq("hibernate.properties"),
                ofFile("java", "extra-icons/java.png", "JVM properties (e.g. Tomcat or Maven JVM configuration): jvm.properties, jvm.config")
                    .eq("jvm.properties", "jvm.config")
                    .altIcons("extra-icons/java_alt.svg"),
                ofFile("imgbot", "extra-icons/imgbot.svg", "ImgBot: .imgbotconfig")
                    .eq(".imgbotconfig"),
                ofFile("jenkins", "extra-icons/jenkins.png", "Jenkins: jenkinsfile(.xml,.yml,...), jenkins")
                    .eq("jenkinsfile", "jenkins").mayEnd(CFG),
                ofFile("jenkins1", "extra-icons/jenkins.png", "Jenkins: start by 'jenkins and end by '.xml,.yml,...'")
                    .start("jenkins").end(CFG),
                ofFile("jenkins2", "extra-icons/jenkins.png", "Jenkins: start by 'jenkins' and contain no dot")
                    .start("jenkins").noDot(),
                ofFile("jest_js", "extra-icons/jest.svg", "Jest: jest.config.js")
                    .eq("jest.config.js"),
                ofFile("jest_ts", "extra-icons/jest.svg", "Jest: jest.config.ts")
                    .eq("jest.config.ts"),
                ofFile("jitpack", "extra-icons/jitpack.svg", "JitPack: jitpack.yml")
                    .eq("jitpack.yml"),
                ofFile("jsbeautify", "extra-icons/jsbeautify.png", "JSBeautify: .jsbeautifyrc(.xml,.yml,...)")
                    .eq(".jsbeautifyrc").mayEnd(CFG),
                ofFile("jshint", "extra-icons/jshint.png", "JSHint: .jshintrc(.xml,.yml,...)")
                    .eq(".jshintrc").mayEnd(CFG),
                ofFile("junit", "extra-icons/junit5.png", "JUnit: junit-platform.properties")
                    .eq("junit-platform.properties"),
                ofFile("karate", "extra-icons/karate.svg", "Karate: karate-config.js")
                    .eq("karate-config.js"),
                ofFile("karma_js", "extra-icons/karma.svg", "Karma: karma.conf.js")
                    .eq("karma.conf.js"),
                ofFile("karma_ts", "extra-icons/karma.svg", "Karma: karma.conf.ts")
                    .eq("karma.conf.ts"),
                ofFile("kibana", "extra-icons/kibana.png", "Kibana: kibana(.xml,.yml,...)")
                    .eq("kibana").mayEnd(YML),
                ofFile("kibana1", "extra-icons/kibana.png", "Kibana: start by 'kibana' and end by '.xml,.yml,...'")
                    .start("kibana").end(YML),
                ofFile("knownissues", "extra-icons/bug.svg", "Known issues: known_issues(.md,.txt,.adoc,.rst)")
                    .start("known_issues").mayEnd(TXT),
                ofFile("kubernetes", "extra-icons/kubernetes.svg", "Kubernetes: kubernetes(.xml,.yml,...)")
                    .eq("kubernetes").mayEnd(YML)
                    .altIcons("extra-icons/kubernetes_alt.svg", "extra-icons/kubernetes_alt2.svg")
                    .tags(ModelTag.KUBERNETES),
                ofFile("kubernetes1", "extra-icons/kubernetes.svg", "Kubernetes: start by 'kubernetes' and end by '.xml,.yml,...'")
                    .start("kubernetes").end(YML)
                    .altIcons("extra-icons/kubernetes_alt.svg", "extra-icons/kubernetes_alt2.svg")
                    .tags(ModelTag.KUBERNETES),
                ofFile("lerna", "extra-icons/lerna.png", "Lerna: lerna.json")
                    .eq("lerna.json"),
                ofFile("lgtm", "extra-icons/lgtm.svg", "LGTM: .lgtm.yml, lgtm.yml")
                    .eq("lgtm", ".lgtm").end(YML)
                    .altIcons("extra-icons/lgtm_alt.svg"),
                ofFile("license", "extra-icons/license.svg", "License: license(.md,.txt,.adoc,.rst), copying, license_info, additional_license_info")
                    .eq("license", "copying", "license_info", "additional_license_info").mayEnd(TXT)
                    .altIcons("extra-icons/license_alt.png"),
                ofFile("lighttpd", "extra-icons/lighttpd.svg", "Lighttpd: lighttpd.conf")
                    .eq("lighttpd.conf"),
                ofFile("log4j", "extra-icons/log4j.png", "Log4j: log4j(.xml,.yml,...), log4j-test")
                    .eq("log4j", "log4j-test").mayEnd(CFG),
                ofFile("logback", "extra-icons/logback.png", "Logback: logback(.xml,.yml,...)")
                    .eq("logback").mayEnd(CFG),
                ofFile("logback1", "extra-icons/logback.png", "Logback: start by 'logback-' and end by '.xml,.yml,...'")
                    .start("logback-").mayEnd(CFG),
                ofFile("logstash", "extra-icons/logstash.png", "Logstash: logstash(.cfg,.conf,.yml,.yaml)")
                    .eq("logstash").mayEnd(".cfg", ".conf", ".yml", ".yaml"),
                ofFile("logstash1", "extra-icons/logstash.png", "Logstash: start by 'logstash' and end by '.cfg,.conf,.yml,.yaml'")
                    .start("logstash").end(".cfg", ".conf", ".yml", ".yaml"),
                ofFile("logstash2", "extra-icons/logstash.png", "Logstash: start by 'logstash-' and end by '.cfg,.conf,.yml,.yaml,.txt'")
                    .start("logstash-").end(".cfg", ".conf", ".yml", ".yaml", ".txt"),
                ofFile("lombok", "extra-icons/lombok.svg", "Lombok: lombok.config")
                    .eq("lombok.config"),
                ofFile("maven_shade_plugin", "extra-icons/generated_pom.svg", "Maven Shade Plugin: dependency-reduced-pom.xml")
                    .eq("dependency-reduced-pom.xml")
                    .altIcons("extra-icons/generated_pom_alt.svg"),
                ofFile("metro", "extra-icons/metro.svg", "Metro config: metro.config.js")
                    .eq("metro.config.js"),
                ofFile("mkdocs", "extra-icons/mkdocs.svg", "MkDocs: mkdocs.yml")
                    .eq("mkdocs.yml"),
                ofFile("moduleinfojava", "extra-icons/moduleinfo.svg", "Java module: module-info.java")
                    .eq("module-info.java")
                    .altIcons("extra-icons/moduleinfo_alt.svg"),
                ofFile("muse", "extra-icons/muse.png", "Muse: .muse.toml")
                    .eq(".muse.toml"),
                ofFile("mysql", "extra-icons/my.png", "MySQL: my.ini")
                    .eq("my.ini"),
                ofFile("netlify", "extra-icons/netlify.svg", "Netlify: netlify.toml")
                    .eq("netlify.toml"),
                ofFile("nginx", "extra-icons/nginx.svg", "Nginx: nginx(.conf)")
                    .eq("nginx").mayEnd(".conf"),
                ofFile("nginx1", "extra-icons/nginx.svg", "Nginx: start by 'nginx' and end by '.conf'")
                    .start("nginx").end(".conf"),
                ofFile("npmrc", "extra-icons/npm.svg", "NPM: .npmrc")
                    .eq(".npmrc"),
                ofFile("npmignore", "extra-icons/npmignore.svg", "NPM: .npmignore")
                    .eq(".npmignore"),
                ofFile("notice", "extra-icons/notice.svg", "Notice: notice(.md,.txt,.adoc,.rst)")
                    .eq("notice").mayEnd(TXT),
                ofFile("nox", "extra-icons/nox.png", "Nox: noxfile.py")
                    .eq("noxfile.py"),
                ofFile("nx", "extra-icons/nx.svg", "Nx: nx.json")
                    .eq("nx.json")
                    .altIcons("extra-icons/nx_alt.svg"),
                ofFile("openissues", "extra-icons/bug.svg", "Open issues: open_issues(.md,.txt,.adoc,.rst)")
                    .start("open_issues").mayEnd(TXT),
                ofFile("packageinfojava", "extra-icons/packageinfojava.svg", "Java package info: package-info.java")
                    .eq("package-info.java"),
                ofFile("packagejson", "extra-icons/npm.svg", "NPM: package.json")
                    .eq("package.json"),
                ofFile("packagejsonlock", "extra-icons/packagejsonlock.png", "NPM: package-lock.json")
                    .eq("package-lock.json"),
                ofFile("pdd", "extra-icons/pdd.svg", "Puzzle Driven Development: .pdd")
                    .eq(".pdd"),
                ofFile("pdd_yml", "extra-icons/pdd.svg", "Puzzle Driven Development: .0pdd.yml")
                    .eq(".0pdd.yml"),
                ofFile("pnpmlock", "extra-icons/pnpm.svg", "PNPM: pnpm-lock.yaml")
                    .eq("pnpm-lock.yaml"),
                ofFile("prettier", "extra-icons/prettier.svg", "Prettier: .prettierrc")
                    .eq(".prettierrc")
                    .tags(ModelTag.PRETTIER)
                    .altIcons("extra-icons/prettier_alt.svg"),
                ofFile("prettier-yml", "extra-icons/prettier.svg", "Prettier: .prettierrc.yml, .prettierrc.yaml")
                    .eq(".prettierrc.yml", ".prettierrc.yaml")
                    .tags(ModelTag.PRETTIER)
                    .altIcons("extra-icons/prettier_alt.svg"),
                ofFile("prettier-json", "extra-icons/prettier.svg", "Prettier: .prettierrc.json, .prettierrc.json5")
                    .eq(".prettierrc.json", ".prettierrc.json5")
                    .tags(ModelTag.PRETTIER)
                    .altIcons("extra-icons/prettier_alt.svg"),
                ofFile("prettier-js", "extra-icons/prettier.svg", "Prettier: .prettierrc.js, prettier.config.js")
                    .eq(".prettierrc.js", "prettier.config.js")
                    .tags(ModelTag.PRETTIER)
                    .altIcons("extra-icons/prettier_alt.svg"),
                ofFile("prettier-cjs", "extra-icons/prettier.svg", "Prettier: .prettierrc.cjs, prettier.config.cjs")
                    .eq(".prettierrc.cjs", "prettier.config.cjs")
                    .tags(ModelTag.PRETTIER)
                    .altIcons("extra-icons/prettier_alt.svg"),
                ofFile("prettier-toml", "extra-icons/prettier.svg", "Prettier: .prettierrc.toml")
                    .eq(".prettierrc.toml")
                    .tags(ModelTag.PRETTIER)
                    .altIcons("extra-icons/prettier_alt.svg"),
                ofFile("prettierignore", "extra-icons/prettierignore.svg", "Prettier: .prettierignore")
                    .eq(".prettierignore")
                    .tags(ModelTag.PRETTIER),
                ofFile("privacy", "extra-icons/privacy.svg", "Privacy: privacy(.md,.txt,.adoc,.rst)")
                    .eq("privacy").mayEnd(TXT),
                ofFile("proguard", "extra-icons/proguard.svg", "Proguard: proguard-rules.pro")
                    .eq("proguard-rules.pro"),
                ofFile("prometheus", "extra-icons/prometheus.svg", "Prometheus: prometheus(.yml,.yaml)")
                    .eq("prometheus").end(YML),
                ofFile("puppet", "extra-icons/puppet.svg", "Puppet: puppet(.conf)")
                    .eq("puppet").mayEnd(".conf"),
                ofFile("puppet1", "extra-icons/puppet.svg", "Puppet: start by 'puppet' and end by '.conf'")
                    .start("puppet").end(".conf"),
                ofFile("readme", "extra-icons/readme.svg", "Readme: readme(.md,.txt,.adoc,.rst), lisezmoi")
                    .eq("readme", "lisezmoi").mayEnd(TXT)
                    .altIcons("extra-icons/readme_alt.svg", "extra-icons/readme_alt2.svg"),
                ofFile("redis", "extra-icons/redis.svg", "Redis: redis(.conf)")
                    .eq("redis").mayEnd(".conf"),
                ofFile("redis1", "extra-icons/redis.svg", "Redis: start by 'redis' and end by '.conf'")
                    .start("redis").end(".conf"),
                ofFile("renovate", "extra-icons/renovate.svg", "Renovate: renovate.json, renovate.json5, .renovaterc.json, .renovaterc")
                    .eq("renovate.json", "renovate.json5", ".renovaterc.json", ".renovaterc"),
                ofFile("requirementstxt_python", "extra-icons/requirementstxt_python.svg", "Python: requirements.txt (Python facet only)")
                    .eq("requirements.txt")
                    .facets("python")
                    .altIcons("extra-icons/requirementstxt_python_alt.svg"),
                ofFile("restyled", "extra-icons/restyled.svg", "Restyled: .restyled.yaml")
                    .eq(".restyled.yaml")
                    .altIcons("extra-icons/restyled_alt.svg", "extra-icons/restyled_alt2.svg"),
                ofFile("roadmap", "extra-icons/roadmap.png", "Roadmap: roadmap(.md,.txt,.adoc,.rst)")
                    .eq("roadmap").mayEnd(TXT),
                ofFile("rollup", "extra-icons/rollup.svg", "Rollup: rollup.config.js")
                    .eq("rollup.config.js"),
                ofFile("rultor", "extra-icons/rultor.svg", "Rultor: .rultor.yml")
                    .eq(".rultor.yml"),
                ofFile("scalingo", "extra-icons/scalingo.svg", "Scalingo: scalingo.json")
                    .eq("scalingo.json"),
                ofFile("security", "extra-icons/security.svg", "Security: security(.md,.txt,.adoc,.rst)")
                    .eq("security").mayEnd(TXT),
                ofFile("snapcraft", "extra-icons/snap.svg", "Snapcraft: snapcraft.yaml")
                    .eq("snapcraft.yaml"),
                ofFile("screwdriver", "extra-icons/screwdriver.svg", "Screwdriver: screwdriver.yaml")
                    .eq("screwdriver.yaml"),
                ofFile("stacksmith", "extra-icons/stacksmith.svg", "Bitnami Stacksmith: stackerfile.yml")
                    .eq("stackerfile.yml"),
                ofFile("svgo", "extra-icons/svgo.svg", "SVGO: svgo(.yml,.yaml)")
                    .eq("svgo").end(YML),
                ofFile("svgo2", "extra-icons/svgo.svg", "SVGO: svgo.config.js")
                    .eq("svgo.config.js"),
                ofFile("swaggerconfig", "extra-icons/swagger.svg", "Swagger: swagger-config.yaml")
                    .eq("swagger-config.yaml")
                    .altIcons("extra-icons/swagger_alt.svg"),
                ofFile("swaggerignore", "extra-icons/swaggerignore.svg", "Swagger: .swagger-codegen-ignore")
                    .eq(".swagger-codegen-ignore"),
                ofFile("tinylogproperties", "extra-icons/tinylog_properties.svg", "Tinylog: tinylog.properties")
                    .eq("tinylog.properties"),
                ofFile("testing", "extra-icons/testing.svg", "Test: test(.md,.txt,.adoc,.rst), testing")
                    .eq("test", "testing").mayEnd(TXT),
                ofFile("todo", "extra-icons/todo.png", "To-Do: todo(.md,.txt,.adoc,.rst)")
                    .eq("todo").mayEnd(TXT)
                    .altIcons("extra-icons/todo_alt.svg", "extra-icons/todo_alt2.svg"),
                ofFile("tox", "extra-icons/tox.png", "Tox: tox.ini")
                    .eq("tox.ini")
                    .altIcons("extra-icons/tox_alt.svg"),
                ofFile("travis", "extra-icons/travis.svg", "Travis CI: .travis.yml")
                    .eq(".travis.yml")
                    .altIcons("extra-icons/travis_alt.svg", "extra-icons/travis_alt02.svg",
                        "extra-icons/travis_alt03.svg", "extra-icons/travis_alt04.svg",
                        "extra-icons/travis_alt05.svg", "extra-icons/travis_alt06.svg",
                        "extra-icons/travis_alt07.svg", "extra-icons/travis_alt08.svg",
                        "extra-icons/travis_alt09.svg", "extra-icons/travis_alt10.svg",
                        "extra-icons/travis_alt11.svg", "extra-icons/travis_alt12.svg",
                        "extra-icons/travis_alt13.svg", "extra-icons/travis_alt14.svg")
                    .tags(ModelTag.TRAVIS),
                ofFile("vagrant", "extra-icons/vagrant.svg", "Vagrant: vagrantfile")
                    .eq("vagrantfile"),
                ofFile("version", "extra-icons/version.png", "Version: (.)version(s)(.md,.txt,.adoc,.rst)")
                    .eq("version", ".version", "versions", ".versions").mayEnd(TXT),
                ofFile("version_json", "extra-icons/version.png", "Version: (.)version(s).json")
                    .eq("version", ".version", "versions", ".versions").end(".json"),
                ofFile("version_properties", "extra-icons/version.png", "Version: (.)version(s).properties")
                    .eq("version", ".version", "versions", ".versions").end(".properties"),
                ofFile("version_yml", "extra-icons/version.png", "Version: (.)version(s).y(a)ml")
                    .eq("version", ".version", "versions", ".versions").end(YML),
                ofFile("version_xml", "extra-icons/version.png", "Version: (.)version(s).xml")
                    .eq("version", ".version", "versions", ".versions").end(".xml"),
                ofFile("vite", "extra-icons/vite.svg", "Vite: ^vite\\.config\\.[jt]s$")
                    .start("vite.config.").end(".js", ".ts")
                    .altIcons("extra-icons/vite_alt.svg"),
                ofFile("weblate", "extra-icons/weblate.svg", "Weblate: .weblate")
                    .eq(".weblate"),
                ofFile("webpack", "extra-icons/webpack.svg", "Webpack: webpack.conf.js")
                    .start("webpack.config.").end(".js", ".ts")
                    .altIcons("extra-icons/webpack_alt.svg"),
                ofFile("werf", "extra-icons/werf.svg", "Werf: werf.yaml")
                    .eq("werf.yaml"),
                ofFile("zalando", "extra-icons/zalando.png", "Zalando Zappr: .zappr.yaml")
                    .eq(".zappr.yaml"),

                //
                // extension only
                //
                ofFile("ext_aac", "extra-icons/aac.svg", "Advanced Audio Coding: *.aac")
                    .end(".aac"),
                ofFile("ext_archive", "extra-icons/archive.svg", "Archive: *.zip, *.7z, *.tar, *.gz, *.bz2, *.xz, *.tgz")
                    .end(".zip", ".7z", ".tar", ".gz", ".bz2", ".xz", ".tgz"),
                ofFile("ext_adoc", "extra-icons/asciidoc.svg", "Asciidoc: *.adoc, *.asciidoc")
                    .end(".adoc", ".asciidoc")
                    .altIcons("extra-icons/asciidoc_alt.png"),
                ofFile("ext_monkeys-audio", "extra-icons/monkey's_audio.svg", "Monkey's Audio: *.ape")
                    .end(".ape"),
                ofFile("ext_apk", "extra-icons/apk.svg", "Android application package (APK): *.apk, *.xapk")
                    .end(".apk", ".xapk"),
                ofFile("ext_apple-audio", "extra-icons/apple.svg", "Apple Audio: *.aiff, *.aifc, *.caf")
                    .end(".aiff", ".aifc", ".caf"),
                ofFile("ext_avro_avsc", "extra-icons/avro.svg", "Avro: *.avsc")
                    .end(".avsc"),
                ofFile("ext_bin", "extra-icons/binary.svg", "Binary: *.bin")
                    .end(".bin")
                    .altIcons("extra-icons/binary_alt.svg", "extra-icons/binary_alt2.svg"),
                ofFile("ext_back", "extra-icons/backup.png", "Backup: *.versionbackup, *.versionsbackup, *.bak, *.back, *.backup, *.old, *.prev, *.revert, *.releaseBackup")
                    .end(".versionbackup", ".versionsbackup", ".bak", ".back", ".backup", ".old", ".prev", ".revert", ".releasebackup"),
                ofFile("ext_sh", "extra-icons/bash.svg", "Bash: *.sh")
                    .end(".sh"),
                ofFile("ext_cert", "extra-icons/certificate.svg", "Certificate: *.jks, *.pem, *.crt, *.cert, *.ca-bundle, *.cer, ...")
                    .end(".jks", ".pem", ".crt", ".cert", ".ca-bundle", ".cer", ".jceks", ".p12", ".p7b", ".p7s", ".pfx", ".pubkey")
                    .altIcons("extra-icons/certificate_alt.png"),
                ofFile("ext_cmd", "extra-icons/cmd.svg", "Windows script: *.cmd, *.bat, *.ps1")
                    .end(".cmd", ".bat", ".ps1"),
                ofFile("ext_csv", "extra-icons/csv.png", "CSV: *.csv")
                    .end(".csv"),
                ofFile("ext_dar", "extra-icons/jar.png", "Diffusion Archive / XL Deploy Archive: *.dar")
                    .end(".dar"),
                ofFile("ext_deb", "extra-icons/deb.png", "Debian package: *.deb")
                    .end(".deb"),
                ofFile("ext_dll", "extra-icons/binary.svg", "Windows DLL: *.dll")
                    .end(".dll")
                    .altIcons("extra-icons/binary_alt.svg", "extra-icons/binary_alt2.svg"),
                ofFile("ext_drawio", "extra-icons/drawio.svg", "Draw.io: *.drawio, *.dio")
                    .end(".drawio", ".dio")
                    .altIcons("extra-icons/drawio_alt.png"),
                ofFile("ext_epub", "extra-icons/epub.svg", "Epub: *.epub, *.mobi, *.azw, *.azw3")
                    .end(".epub", ".mobi", ".azw", ".azw3"),
                ofFile("ext_exe", "extra-icons/msexe.png", "Windows executable: *.exe")
                    .end(".exe"),
                ofFile("ext_form", "extra-icons/form.svg", "Form (e.g. IntelliJ Swing xml form): *.form")
                    .end(".form"),
                ofFile("ext_graphqls", "extra-icons/graphql.svg", "GraphQL: *.graphqls")
                    .end(".graphqls"),
                ofFile("ext_haxe", "extra-icons/haxe.png", "Haxe: *.hx")
                    .end(".hx"),
                ofFile("ext_haxexml", "extra-icons/haxehxml.png", "Haxe: *.hxml")
                    .end(".hxml"),
                ofFile("ext_http", "extra-icons/http.png", "HTTP (e.g. IntelliJ HTTP Client queries file): *.http")
                    .end(".http"),
                ofFile("ext_iml", "extra-icons/jetbrains.svg", "IntelliJ project: *.iml")
                    .end(".iml"),
                ofFile("ext_jfr", "extra-icons/jfr.svg", "JFR snapshot: *.jfr")
                    .end(".jfr"),
                ofFile("ext_jinja", "extra-icons/jinja.svg", "Jinja: *.jinja, *.jinja2")
                    .end(".jinja", ".jinja2"),
                ofFile("ext_cfg_ini", "extra-icons/config.svg", "Configuration: *.ini")
                    .end(".ini"),
                ofFile("ext_cfg_cfg", "extra-icons/config.svg", "Configuration: *.cfg")
                    .end(".cfg"),
                ofFile("ext_cfg_conf", "extra-icons/config.svg", "Configuration: *.conf")
                    .end(".conf"),
                ofFile("ext_cfg_config", "extra-icons/config.svg", "Configuration: *.config")
                    .end(".config"),
                ofFile("ext_jar", "extra-icons/jar.png", "Java archive: *.jar")
                    .end(".jar"),
                ofFile("ext_jaroriginal", "extra-icons/jar.png", "Java archive (copy): *.jar.original")
                    .end(".jar.original"),
                ofFile("ext_jsmap", "extra-icons/javascript-map.svg", "Javascript Source Maps: *.js.map")
                    .end(".js.map"),
                ofFile("ext_cssmap", "extra-icons/css-map.svg", "Css Source Maps: *.css.map")
                    .end(".css.map"),
                ofFile("ext_css", "extra-icons/css.svg", "Css Files: *.css")
                    .end(".css"),
                ofFile("ext_js", "extra-icons/javascript.svg", "Javascript: *.js")
                    .end(".js")
                    .altIcons("extra-icons/javascript_alt.svg"),
                ofFile("ext_js_react", "extra-icons/jsx.svg", "JSX (Javascript + React): *.jsx")
                    .end(".jsx"),
                ofFile("ext_kdbx", "extra-icons/keepass.svg", "KeePass: *.kdbx")
                    .end(".kdbx")
                    .altIcons("extra-icons/keepass_alt.png"),
                ofFile("ext_matroska", "extra-icons/matroska.svg", "Matroska: *.mkv, *.mka")
                    .end(".mkv", ".mka"),
                ofFile("ext_md", "extra-icons/markdown.svg", "Markdown: *.md")
                    .end(".md")
                    .altIcons("extra-icons/markdown_alt.svg", "extra-icons/markdown_alt2.svg"),
                ofFile("ext_mdx", "extra-icons/mdx.svg", "MDX: *.mdx")
                    .end(".mdx")
                    .altIcons("extra-icons/mdx_alt.svg", "extra-icons/mdx_alt2.svg"),
                ofFile("ext_mmd", "extra-icons/mindmap.svg", "IDEA MindMap plugin: *.mmd")
                    .end(".mmd"),
                ofFile("ext_mpeg", "extra-icons/mpeg.svg", "MPEG-1/2 Audio: *.mp3, *.mp1, *.m1a, *.mp2, *.m2a, *.mpa")
                    .end(".mp3", ".mp1", ".m1a", ".mp2", ".m2a", ".mpa"),
                ofFile("ext_mwb", "extra-icons/mysqlworkbench.png", "MySQL Workbench: *.mwb")
                    .end(".mwb"),
                ofFile("ext_nsi", "extra-icons/nsis.svg", "NSIS Nullsoft Scriptable Install System: *.nsi")
                    .end(".nsi"),
                ofFile("ext_pdf", "extra-icons/pdf.png", "PDF: *.pdf")
                    .end(".pdf")
                    .altIcons("extra-icons/pdf_alt.svg", "extra-icons/pdf_alt2.svg",
                        "extra-icons/pdf_alt3.svg"),
                ofFile("ext_pid", "extra-icons/pid.svg", "PID: *.pid")
                    .end(".pid"),
                ofFile("ext_postmanconfig", "extra-icons/postman.svg", "Postman config: *.postman.json")
                    .end(".postman.json"),
                ofFile("ext_puml", "extra-icons/uml.svg", "PlantUML: *.puml")
                    .end(".puml"),
                ofFile("ext_postmancollection", "extra-icons/postman_col.svg", "Postman collection: *postman_collection.json")
                    .end("postman_collection.json")
                    .altIcons("extra-icons/postman.svg"),
                ofFile("ext_postmanenvironment", "extra-icons/postman_env.svg", "Postman environment: *postman_environment.json")
                    .end("postman_environment.json")
                    .altIcons("extra-icons/postman.svg"),
                ofFile("ext_rpm", "extra-icons/rpm.svg", "Red Hat package: *.rpm")
                    .end(".rpm"),
                ofFile("ext_robotframework", "extra-icons/robotframework.svg", "Robot Framework: *.robot")
                    .end(".robot")
                    .altIcons("extra-icons/robotframework_alt.svg"),
                ofFile("sass", "extra-icons/sass.svg", "SASS: *.sass")
                    .end(".sass"),
                ofFile("scss", "extra-icons/scss.svg", "SASS: *.scss")
                    .end(".scss"),
                ofFile("less", "extra-icons/less.svg", "LESS CSS: *.less")
                    .end(".less"),
                ofFile("ext_stylus-browserext", "extra-icons/stylus-browserext.png", "Stylus (browser extension): *.styl")
                    .end(".styl"),
                ofFile("ext_svg", "extra-icons/svg.svg", "SVG: *.svg")
                    .end(".svg")
                    .altIcons("extra-icons/svg_alt.svg"),
                ofFile("ext_tf", "extra-icons/terraform.svg", "Terraform: *.hcl, *.tf, *.tf.json")
                    .end(".hcl", ".tf", ".tf.json"),
                ofFile("ext_toml", "extra-icons/toml.svg", "TOML: *.toml")
                    .end(".toml"),
                ofFile("typescript", "extra-icons/test-ts.svg", "Typescript: *.spec.ts")
                    .end(".spec.ts")
                    .altIcons("extra-icons/test-ts_alt.svg"),
                ofFile("typescript-ts", "extra-icons/typescript.svg", "Typescript: *.ts")
                    .end(".ts"),
                ofFile("typescript-react", "extra-icons/tsx.svg", "TSX (Typescript + React): *.tsx")
                    .end(".tsx"),
                ofFile("ext_video", "extra-icons/video.svg", "Video: *.3gp, *.avi, *.flv, *.mkv, *.mp4, *.mpeg, *.ogm, *.webm, ...")
                    .end(".3g2", ".3gp", ".avi", ".divx", ".f4a", ".f4b", ".f4p", ".f4v", ".flv", ".m4p",
                        ".m4v", ".mp4", ".mpg", ".mpeg", ".mov", ".ogm", ".ogv", ".vob", ".webm", ".wmv", ".xvid")
                    .altIcons("extra-icons/video_alt.svg", "extra-icons/video_alt2.svg",
                        "extra-icons/video_alt3.svg"),
                ofFile("ext_velocity", "extra-icons/velocity.svg", "Velocity: *.vtl")
                    .end(".vtl"),
                ofFile("ext_vscode-workspace", "extra-icons/vscode.svg", "Visual Studio Code: : *.code-workspace")
                    .end(".code-workspace"),
                ofFile("ext_war", "extra-icons/tomcat.svg", "Java WAR: *.war")
                    .end(".war"),
                ofFile("ext_wma", "extra-icons/wma.svg", "Windows Media Audio: *.wma")
                    .end(".wma")
                    .altIcons("extra-icons/video_alt3.svg"),
                ofFile("ext_xiph", "extra-icons/xiph.svg", "Xiph audio: *.flac, *.oga, *.ogg, *.opus, *.spx")
                    .end(".flac", ".oga", ".ogg", ".opus", ".spx"),

                ofFile("ext_libreoffice_calc", "extra-icons/officedocs/localc.png", "LibreOffice Calc: *.ods")
                    .end(".ods")
                    .tags(ModelTag.LIBRE_OFFICE),
                ofFile("ext_libreoffice_draw", "extra-icons/officedocs/lodraw.png", "LibreOffice Draw: *.odg")
                    .end(".odg")
                    .tags(ModelTag.LIBRE_OFFICE),
                ofFile("ext_libreoffice_impress", "extra-icons/officedocs/loimpress.png", "LibreOffice Impress: *.odp")
                    .end(".odp")
                    .tags(ModelTag.LIBRE_OFFICE),
                ofFile("ext_libreoffice_math", "extra-icons/officedocs/lomath.png", "LibreOffice Math: *.odf")
                    .end(".odf")
                    .tags(ModelTag.LIBRE_OFFICE),
                ofFile("ext_libreoffice_writer", "extra-icons/officedocs/lowriter.png", "LibreOffice Writer: *.odt")
                    .end(".odt")
                    .tags(ModelTag.LIBRE_OFFICE),
                ofFile("ext_msoffice_excel", "extra-icons/officedocs/msexcel-2019.svg", "MSOffice Excel: *.xls, *.xlsx")
                    .end(".xls", ".xlsx")
                    .altIcons("extra-icons/officedocs/msexcel.png")
                    .tags(ModelTag.MS_OFFICE),
                ofFile("ext_msoffice_onenote", "extra-icons/officedocs/msonenote-2019.svg", "MSOffice OneNote: *.one, *.onetoc2")
                    .end(".one", ".onetoc2")
                    .altIcons("extra-icons/officedocs/msonenote.png")
                    .tags(ModelTag.MS_OFFICE),
                ofFile("ext_msoffice_powerpoint", "extra-icons/officedocs/mspowerpoint-2019.svg", "MSOffice Powerpoint: *.ppt, *.pptx")
                    .end(".ppt", ".pptx")
                    .altIcons("extra-icons/officedocs/mspowerpoint.png")
                    .tags(ModelTag.MS_OFFICE),
                ofFile("ext_msoffice_project", "extra-icons/officedocs/msproject-2019.svg", "MSOffice Project: *.mpd, *.mpp, *.mpt")
                    .end(".mpd", ".mpp", ".mpt")
                    .altIcons("extra-icons/officedocs/msproject.png")
                    .tags(ModelTag.MS_OFFICE),
                ofFile("ext_msoffice_visio", "extra-icons/officedocs/msvisio-2019.svg", "MSOffice Visio: *.vsd, *.vsdx, *.vss, *.vssx, *.vst, *.vstx")
                    .end(".vsd", ".vsdx", ".vss", ".vssx", ".vst", ".vstx")
                    .altIcons("extra-icons/officedocs/msvisio.png")
                    .tags(ModelTag.MS_OFFICE),
                ofFile("ext_msoffice_word", "extra-icons/officedocs/msword-2019.svg", "MSOffice Word: *.doc, *.docx")
                    .end(".doc", ".docx")
                    .altIcons("extra-icons/officedocs/msword.png")
                    .tags(ModelTag.MS_OFFICE),

                //
                // generic
                //
                ofFile("docker_generic", "extra-icons/docker.svg", "Docker (generic): start by 'dockerfile'")
                    .start("dockerfile")
                    .altIcons("extra-icons/docker_alt.png", "extra-icons/docker_alt2.svg")
                    .tags(ModelTag.DOCKER),
                ofFile("docker_generic_2", "extra-icons/docker.svg", "Docker (generic): end with 'dockerfile'")
                    .end("dockerfile")
                    .altIcons("extra-icons/docker_alt.png", "extra-icons/docker_alt2.svg")
                    .tags(ModelTag.DOCKER)
            )
            .flatMap(ExtraIconProvider::modelList)
            .collect(Collectors.toList());
    }

    public ExtraIconProvider() {
        super();
    }

    @Override
    protected List<Model> getAllModels() {
        return allModels();
    }
}
