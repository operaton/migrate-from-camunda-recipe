# Migrate from Camunda to Operaton Recipes

This repository contains OpenRewrite recipes to help migrate your applications from Camunda to Operaton.

## Available Recipes

The following recipes are available to assist with migration:

1. **MigrateSpringBootApplication** - A comprehensive recipe that applies all the recipes below to migrate a Spring Boot application from Camunda to Operaton.
2. **ChangePackage** - Changes Java package names from `org.camunda.*` to `org.operaton.*`.
3. **ReplaceCamundaDependencies** - Replaces Camunda Maven dependencies with their Operaton equivalents.
4. **MigrateDeploymentDescriptors** - Updates XML namespace declarations in deployment descriptor files.
5. **ResolveDeprecations** - Resolves deprecated API usages.

## How to Use in a Maven Project

To use these recipes in your Maven-based Spring Boot project, add the following to your `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.openrewrite.maven</groupId>
            <artifactId>rewrite-maven-plugin</artifactId>
            <version>7.0.4</version>
            <configuration>
                <activeRecipes>
                    <recipe>org.operaton.rewrite.spring.MigrateSpringBootApplication</recipe>
                </activeRecipes>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>org.operaton</groupId>
                    <artifactId>migrate-camunda-recipe</artifactId>
                    <version>1.0.0-alpha-2-SNAPSHOT</version>
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
    id("org.openrewrite.rewrite") version("7.0.4")
}

repositories {
    mavenCentral()
}

dependencies {
    rewrite("org.operaton:migrate-camunda-recipe:1.0.0-alpha-2-SNAPSHOT")
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
mvn org.openrewrite.maven:rewrite-maven-plugin:6.9.0:run \
  -Drewrite.recipeArtifactCoordinates=org.operaton:migrate-camunda-recipe:1.0.0-alpha-2-SNAPSHOT \
  -Drewrite.activeRecipes=org.operaton.rewrite.spring.MigrateSpringBootApplication
```

### Gradle

[Create a `init.gradle` file](https://docs.openrewrite.org/running-recipes/running-rewrite-on-a-gradle-project-without-modifying-the-build).

```bash
./gradlew rewriteRun \
  --rewrite.recipeArtifactCoordinates=org.operaton:migrate-camunda-recipe:1.0.0-alpha-2-SNAPSHOT \
  --rewrite.activeRecipes=org.operaton.rewrite.spring.MigrateSpringBootApplication
```

This approach allows you to apply the migration without adding the plugin to your build configuration.

## Mapping Documentation

For more details on the specific changes made by these recipes, see the [Camunda to Operaton Artifact Mapping](camunda-to-operaton-mapping.md) document.
