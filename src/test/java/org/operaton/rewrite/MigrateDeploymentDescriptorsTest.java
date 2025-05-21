package org.operaton.rewrite;

import static org.openrewrite.java.Assertions.mavenProject;
import static org.openrewrite.java.Assertions.srcMainResources;
import static org.openrewrite.xml.Assertions.xml;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class MigrateDeploymentDescriptorsTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.MigrateDeploymentDescriptors");
    }

    @Test
    void migrateDeploymentDescriptors() {
        rewriteRun(spec -> spec.recipeFromResources("org.operaton.rewrite.MigrateDeploymentDescriptors"),
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
                """))));
    }

}
