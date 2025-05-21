/*
 * Copyright 2025 the Operaton contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
