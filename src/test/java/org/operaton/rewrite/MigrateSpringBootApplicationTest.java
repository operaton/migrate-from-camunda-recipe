package org.operaton.rewrite;

import static org.openrewrite.java.Assertions.mavenProject;
import static org.openrewrite.java.Assertions.srcMainResources;
import static org.openrewrite.properties.Assertions.properties;
import static org.openrewrite.yaml.Assertions.yaml;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class MigrateSpringBootApplicationTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.MigrateSpringBootApplication");
    }

    @Test
    void migrateSpringBootApplication() {
        rewriteRun(spec -> spec.recipeFromResources("org.operaton.rewrite.MigrateSpringBootApplication"),
          mavenProject("",
            //language=properties
            srcMainResources(
              properties("""
              camunda.bpm.history-ttl=5s
              camunda.bpm.application.deployment-aware=true
              camunda.bpm.application.scan-for-process-definitions=true
              camunda.bpm.license-file=/etc/c7.txt
              """, """
              operaton.bpm.history-ttl=5s
              operaton.bpm.application.deployment-aware=true
              operaton.bpm.application.scan-for-process-definitions=true
              """, spec -> spec.path("application.properties")),
            //language=yaml
            yaml("""
                  camunda:
                    bpm:
                      application:
                        scan-for-process-definitions: true
                        deployment-aware: true
                """, """
              operaton.bpm:
                application:
                  scan-for-process-definitions: true
                  deployment-aware: true
              """, spec -> spec.path("application.yml"))))
        );
    }
}

