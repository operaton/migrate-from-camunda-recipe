package org.operaton.rewrite;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.mavenProject;
import static org.openrewrite.maven.Assertions.pomXml;

class ResolveDependencyVersionPropertyTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new ResolveDependencyVersionProperty("org.openrewrite", "rewrite-yaml"));
    }

    @Nested
    class ResolveVersionProperty {

        @Test
        @DocumentExample
        void fromSamePom() {
            rewriteRun(
              mavenProject("project", pomXml(
                //language=xml
                """
                  <project>
                      <groupId>org.operaton.test</groupId>
                      <artifactId>test-app</artifactId>
                      <version>1</version>
                      <properties>
                          <rewrite-yaml.version>8.41.1</rewrite-yaml.version>
                          <rewrite-maven.version>8.40.0</rewrite-maven.version>
                      </properties>
                      <dependencies>
                        <dependency>
                          <groupId>org.openrewrite</groupId>
                          <artifactId>rewrite-yaml</artifactId>
                          <version>${rewrite-yaml.version}</version>
                        </dependency>
                      </dependencies>
                  </project>
                  """,
                //language=xml
                """
                  <project>
                      <groupId>org.operaton.test</groupId>
                      <artifactId>test-app</artifactId>
                      <version>1</version>
                      <properties>
                          <rewrite-yaml.version>8.41.1</rewrite-yaml.version>
                          <rewrite-maven.version>8.40.0</rewrite-maven.version>
                      </properties>
                      <dependencies>
                        <dependency>
                          <groupId>org.openrewrite</groupId>
                          <artifactId>rewrite-yaml</artifactId>
                          <version>8.41.1</version>
                        </dependency>
                      </dependencies>
                  </project>
                  """)));
        }

        @Test
        void fromParent() {
            rewriteRun(
              mavenProject("parent",
                pomXml(
                  //language=xml
                  """
                    <project>
                        <groupId>org.operaton.test</groupId>
                        <artifactId>test-app-parent</artifactId>
                        <version>1</version>
                        <properties>
                            <rewrite-yaml.version>8.41.1</rewrite-yaml.version>
                            <rewrite-java.version>8.40.0</rewrite-java.version>
                        </properties>
                    </project>
                    """)),
              mavenProject("project",
                pomXml(
                  //language=xml
                  """
                    <project>
                        <parent>
                            <groupId>org.operaton.test</groupId>
                            <artifactId>test-app-parent</artifactId>
                            <version>1</version>
                        </parent>
                        <artifactId>test-app</artifactId>
                        <properties>
                            <rewrite-maven.version>8.40.0</rewrite-maven.version>
                        </properties>
                        <dependencies>
                          <dependency>
                            <groupId>org.openrewrite</groupId>
                            <artifactId>rewrite-yaml</artifactId>
                            <version>${rewrite-yaml.version}</version>
                          </dependency>
                        </dependencies>
                    </project>
                    """,
                  //language=xml
                  """
                    <project>
                        <parent>
                            <groupId>org.operaton.test</groupId>
                            <artifactId>test-app-parent</artifactId>
                            <version>1</version>
                        </parent>
                        <artifactId>test-app</artifactId>
                        <properties>
                            <rewrite-maven.version>8.40.0</rewrite-maven.version>
                        </properties>
                        <dependencies>
                          <dependency>
                            <groupId>org.openrewrite</groupId>
                            <artifactId>rewrite-yaml</artifactId>
                            <version>8.41.1</version>
                          </dependency>
                        </dependencies>
                    </project>
                    """)));
        }
    }

    @Nested
    class SkipOnNotMatching {
        @Test
        void groupId() {
            rewriteRun(
              recipeSpec -> recipeSpec.recipe(new ResolveDependencyVersionProperty("org.openrewrite.other", "rewrite-yaml")),
              mavenProject("project", pomXml(
                //language=xml
                """
                   <project>
                      <groupId>org.operaton.test</groupId>
                      <artifactId>test-app</artifactId>
                      <version>1</version>
                      <properties>
                          <rewrite-yaml.version>8.41.1</rewrite-yaml.version>
                          <rewrite-java.version>8.40.0</rewrite-java.version>
                      </properties>
                      <dependencies>
                        <dependency>
                          <groupId>org.openrewrite</groupId>
                          <artifactId>rewrite-yaml</artifactId>
                          <version>${rewrite-yaml.version}</version>
                        </dependency>
                      </dependencies>
                  </project>
                  """)));
        }

        @Test
        void artifactId() {
            rewriteRun(
              recipeSpec -> recipeSpec.recipe(new ResolveDependencyVersionProperty("org.openrewrite", "rewrite-java")),
              mavenProject("project", pomXml(
                //language=xml
                """
                   <project>
                      <groupId>org.operaton.test</groupId>
                      <artifactId>test-app</artifactId>
                      <version>1</version>
                      <properties>
                          <rewrite-yaml.version>8.41.1</rewrite-yaml.version>
                          <rewrite-java.version>8.40.0</rewrite-java.version>
                      </properties>
                      <dependencies>
                        <dependency>
                          <groupId>org.openrewrite</groupId>
                          <artifactId>rewrite-yaml</artifactId>
                          <version>${rewrite-yaml.version}</version>
                        </dependency>
                      </dependencies>
                  </project>
                  """)));
        }
    }
}
