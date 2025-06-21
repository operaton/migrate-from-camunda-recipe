package org.operaton.rewrite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openrewrite.maven.Assertions.pomXml;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class MigrateDependenciesTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.ReplaceCamundaDependencies");
    }

    @Test
    void migrateCamundaEngine() {
        rewriteRun(
          pomXml(
            //language=xml
            """
            <project>
               <groupId>org.operaton.test</groupId>
               <artifactId>test-app</artifactId>
               <version>1</version>
               <dependencies>
                 <dependency>
                   <groupId>org.camunda.bpm</groupId>
                   <artifactId>camunda-engine</artifactId>
                   <version>7.22.0</version>
                 </dependency>
               </dependencies>
           </project>
           """,
            spec -> spec.after(actual -> {
                System.out.println(actual);

                assertThat(Pattern.compile("<version>1\\.0\\.(.*)</version>").matcher(actual).results().toList()).hasSize(1);
                return actual;
            })));
    }

    @Test
    @Disabled("Solve in a separate issue #13")
    void migrateCamundaEngineWithVersionInProperty() {
        rewriteRun(
          pomXml(
            //language=xml
            """
            <project>
               <groupId>org.operaton.test</groupId>
               <artifactId>test-app</artifactId>
               <version>1</version>
               <properties>
                 <camunda.version>7.22.0</camunda.version>
               </properties>
               <dependencies>
                 <dependency>
                   <groupId>org.camunda.bpm</groupId>
                   <artifactId>camunda-engine</artifactId>
                   <version>${camunda.version}</version>
                 </dependency>
               </dependencies>
           </project>
           """,
            spec -> spec.after(actual -> {
                assertThat(Pattern.compile("<version>1\\.0\\.(.*)</version>").matcher(actual).results().toList()).hasSize(1);
                return actual;
            })));
    }

    @Test
    void migrateCamundaEngineWithManagedVersion() {
        rewriteRun(
          pomXml(
            //language=xml
            """
            <project>
               <groupId>org.operaton.test</groupId>
               <artifactId>test-app</artifactId>
               <version>1</version>
               <dependencyManagement>
                 <dependencies>
                   <dependency>
                     <groupId>org.camunda.bpm</groupId>
                     <artifactId>camunda-bom</artifactId>
                     <version>7.22.0</version>
                     <scope>import</scope>
                     <type>pom</type>
                   </dependency>
                 </dependencies>
               </dependencyManagement>
               <dependencies>
                 <dependency>
                   <groupId>org.camunda.bpm</groupId>
                   <artifactId>camunda-engine</artifactId>
                 </dependency>
               </dependencies>
           </project>
           """,
            spec -> spec.after(actual -> {
                System.out.println(actual);
                assertThat(Pattern.compile("<version>1\\.0\\.(.*)</version>").matcher(actual).results().toList()).hasSize(1);
                return actual;
            })));
    }
}
