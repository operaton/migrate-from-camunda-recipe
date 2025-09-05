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
package org.operaton.rewrite.quarkus;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.mavenProject;
import static org.openrewrite.java.Assertions.srcMainResources;
import static org.openrewrite.properties.Assertions.properties;
import static org.openrewrite.yaml.Assertions.yaml;

class MigrateQuarkusApplicationTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.quarkus.MigrateQuarkusApplication");
    }

    @Test
    void migrateQuarkusApplication() {
        rewriteRun(mavenProject("project",
          //language=properties
          srcMainResources(properties("""
              quarkus.camunda.generic-config.cmmn-enabled=false
              quarkus.camunda.generic-config.dmn-enabled=false
              quarkus.camunda.generic-config.history=none
              quarkus.camunda.generic-config.initialize-telemetry=false
              """, """
              quarkus.operaton.generic-config.cmmn-enabled=false
              quarkus.operaton.generic-config.dmn-enabled=false
              quarkus.operaton.generic-config.history=none
              quarkus.operaton.generic-config.initialize-telemetry=false
              """, spec -> spec.path("application.properties"))),
            //language=yaml
            yaml(
              """
                quarkus:
                  camunda:
                    generic-config:
                      history: none
                """,
              """
                quarkus:
                  operaton:
                    generic-config:
                      history: none
                """, spec -> spec.path("application.yml")
            )
          )
        );
    }
}
