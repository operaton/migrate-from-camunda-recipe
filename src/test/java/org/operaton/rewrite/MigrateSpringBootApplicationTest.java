package org.operaton.rewrite;

import static org.openrewrite.java.Assertions.mavenProject;
import static org.openrewrite.java.Assertions.srcMainResources;
import static org.openrewrite.properties.Assertions.properties;
import static org.openrewrite.yaml.Assertions.yaml;
import static org.openrewrite.xml.Assertions.xml;

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
              //META-INF/processes.xml
              xml("""
                <process-application xmlns="http://www.camunda.org/schema/1.0/ProcessApplication"
                                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                  <process-archive>
                  </process-archive>
                </process-application>""", """
                <process-application xmlns="http://www.operaton.org/schema/1.0/ProcessApplication"
                                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                  <process-archive>
                  </process-archive>
                </process-application>
                """),
              //META-INF/processes.xml
              xml("""
                <process-application xmlns="http://www.camunda.org/schema/1.0/ProcessApplication">
                  <process-archive>
                  </process-archive>
                </process-application>
                """, """
                <process-application xmlns="http://www.operaton.org/schema/1.0/ProcessApplication">
                  <process-archive>
                  </process-archive>
                </process-application>
                """),
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
              """)))
        );

    }
}

