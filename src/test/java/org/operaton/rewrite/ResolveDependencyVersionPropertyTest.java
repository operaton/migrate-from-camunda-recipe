package org.operaton.rewrite;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
                     </properties>
                 </project>
                 """)),
              mavenProject("project",
                pomXml(
                  //language=xml
                  """
                 <project>
                     <parent>
                         <artifactId>test-app-parent</artifactId>
                         <groupId>org.operaton.test</groupId>
                         <version>1</version>
                     </parent>
                     <artifactId>test-app</artifactId>
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
        @Disabled("Unable to find a suitable dependency 🥲")
        void groupId() {
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
              recipeSpec -> recipeSpec.recipe(null /* new with unmatching artifacId*/),
              mavenProject("project", pomXml(
                //language=xml
                """
                <project>
                   <groupId>org.operaton.test</groupId>
                   <artifactId>test-app</artifactId>
                   <version>1</version>
                   <properties>
                       <rewrite-yaml.version>8.41.1</rewrite-yaml.version>
                   </properties>
                   <dependencies>
                     <dependency>
                       <groupId>org.openrewrite</groupId>
                       <artifactId>rewrite-java</artifactId>
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
                   </properties>
                   <dependencies>
                     <dependency>
                       <groupId>org.openrewrite</groupId>
                       <artifactId>rewrite-java</artifactId>
                       <version>${rewrite-yaml.version}</version>
                     </dependency>
                   </dependencies>
               </project>
               """)));
        }
    }
}
