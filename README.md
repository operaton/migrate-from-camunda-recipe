# Migrate from Camunda to Operaton Recipes

This repository contains OpenRewrite recipes to help migrate your applications from Camunda to Operaton.

## Available Recipes

The following recipes are available to assist with migration:

1. **MigrateSpringBootApplication** - A comprehensive recipe that applies all the recipes below to migrate a Spring Boot application from Camunda to Operaton.
2. **ChangePackage** - Renames selected Camunda Java packages to their Operaton counterparts.
3. **ChangeType** - Replaces usages of Camunda types with their Operaton equivalents when simple class names changed (e.g., `CamundaX` -> `OperatonX`). Complements ChangePackage.
4. **ReplaceCamundaDependencies** - Replaces Camunda Maven dependencies with their Operaton equivalents.
5. **MigrateDeploymentDescriptors** - Updates XML namespace declarations in deployment descriptor files.
6. **ResolveDeprecations** - Resolves deprecated API usages.

## How to Use in a Maven Project

To use these recipes in your Maven-based Spring Boot project, add the following to your `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.openrewrite.maven</groupId>
            <artifactId>rewrite-maven-plugin</artifactId>
            <version>6.17.0</version>
            <configuration>
                <activeRecipes>
                    <recipe>org.operaton.rewrite.spring.MigrateSpringBootApplication</recipe>
                </activeRecipes>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>org.operaton</groupId>
                    <artifactId>migrate-camunda-recipe</artifactId>
                    <version>1.0.0-beta-2</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

Then run:

```bash
mvn rewrite:run
```

## How to Use in a Gradle Project

For Gradle projects, add the following to your `build.gradle`:

```groovy
plugins {
    id("java")
    id("org.openrewrite.rewrite") version("7.14.1")
}

repositories {
    // Include mavenLocal so recipe SNAPSHOTs published locally can be resolved
    mavenLocal()
    mavenCentral()
}

dependencies {
    rewrite("org.operaton:migrate-camunda-recipe:1.0.0-beta-2")
}

rewrite {
    activeRecipe("org.operaton.rewrite.spring.MigrateSpringBootApplication")
}
```

Then run:

```bash
./gradlew rewriteRun
```

## Using Individual Recipes

If you want to apply only specific recipes, you can specify them individually:

```xml
<activeRecipes>
    <recipe>org.operaton.rewrite.ChangePackage</recipe>
    <recipe>org.operaton.rewrite.ReplaceCamundaDependencies</recipe>
</activeRecipes>
```

Or in Gradle:

```groovy
rewrite {
    activeRecipe("org.operaton.rewrite.ChangePackage")
    activeRecipe("org.operaton.rewrite.ReplaceCamundaDependencies")
}
```

## Running Without Modifying the Build

You can also run the MigrateSpringBootApplication recipe without modifying your build files by using the Maven or Gradle command line:

### Maven

```bash
mvn org.openrewrite.maven:rewrite-maven-plugin:6.17.0:run \
  -Drewrite.recipeArtifactCoordinates=org.operaton:migrate-camunda-recipe:1.0.0-beta-2 \
  -Drewrite.activeRecipes=org.operaton.rewrite.spring.MigrateSpringBootApplication
```

### Gradle

[Create a init.gradle file](https://docs.openrewrite.org/running-recipes/running-rewrite-on-a-gradle-project-without-modifying-the-build). It does not need to be in the project directory itself (although it will make it easier for this guide). Copy the below init script to your file:

```groovy
initscript {
  repositories {
    maven { url "https://plugins.gradle.org/m2" }
  }
  dependencies {
    classpath("org.openrewrite:plugin:7.14.1")
  }
}

rootProject {
  plugins.apply(org.openrewrite.gradle.RewritePlugin)
  dependencies {
    rewrite("org.operaton:migrate-camunda-recipe:1.0.0-beta-2")
  }

  afterEvaluate {
    // Include mavenLocal so recipe SNAPSHOTs published locally can be resolved
    if (repositories.isEmpty()) {
      repositories {
        mavenLocal()
        mavenCentral()
      }
    } else {
      repositories {
        mavenLocal()
      }
    }
  }
}
```

```bash
./gradlew --init-script init.gradle rewriteRun -Drewrite.activeRecipe=org.operaton.rewrite.spring.MigrateSpringBootApplication
```

This approach allows you to apply the migration without adding the plugin to your build configuration.

## Mapping Documentation

For more details on the specific changes made by these recipes, see the [Camunda to Operaton Artifact Mapping](camunda-to-operaton-mapping.md) document.
