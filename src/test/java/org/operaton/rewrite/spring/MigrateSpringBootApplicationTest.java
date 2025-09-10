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
package org.operaton.rewrite.spring;

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
        spec.recipeFromResources("org.operaton.rewrite.spring.MigrateSpringBootApplication");
    }

    @Test
    void migrateSpringBootApplication() {
        rewriteRun(
          mavenProject(
            "project",
            //language=properties
            srcMainResources(
              properties(
                """
                  camunda.bpm.history-ttl=5s
                  camunda.bpm.application.deployment-aware=true
                  camunda.bpm.application.scan-for-process-definitions=true
                  camunda.bpm.license-file=/etc/c7.txt
                  """,
                """
                  operaton.bpm.history-ttl=5s
                  operaton.bpm.application.deployment-aware=true
                  operaton.bpm.application.scan-for-process-definitions=true
                  """,
                spec -> spec.path("application.properties")),
              //language=yaml
              yaml(
                """
                  camunda:
                    bpm:
                      application:
                        scan-for-process-definitions: true
                        deployment-aware: true
                  """,
                """
                  operaton.bpm:
                    application:
                      scan-for-process-definitions: true
                      deployment-aware: true
                  """, spec -> spec.path("application.yml")
              )
            )
          )
        );
    }
}
