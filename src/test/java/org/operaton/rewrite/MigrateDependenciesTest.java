package org.operaton.rewrite;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.mavenProject;
import static org.openrewrite.maven.Assertions.pomXml;

public class MigrateDependenciesTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.ReplaceCamundaDependencies");
    }

    @Test
    void migrateCamundaEngine() {
        rewriteRun(
          mavenProject("project", pomXml(
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

            """
             <project>
                <groupId>org.operaton.test</groupId>
                <artifactId>test-app</artifactId>
                <version>1</version>
                <dependencies>
                  <dependency>
                    <groupId>org.operaton.bpm</groupId>
                    <artifactId>operaton-engine</artifactId>
                    <version>1.0.0-beta-1</version>
                  </dependency>
                </dependencies>
            </project>
            """)));
    }

}
