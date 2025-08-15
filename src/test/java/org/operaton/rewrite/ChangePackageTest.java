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

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ChangePackageTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.ChangePackage");
    }

    @DocumentExample
    @Test
    void changePackageInImports() {
        rewriteRun(
          java(
            """
              import org.camunda.bpm.engine.ProcessEngine;
              import org.camunda.bpm.engine.RuntimeService;
              import java.util.List;
              
              class Test {
                  void test(ProcessEngine engine) {
                      RuntimeService runtimeService = engine.getRuntimeService();
                  }
              }
              """,
              """
              import org.operaton.bpm.engine.ProcessEngine;
              import org.operaton.bpm.engine.RuntimeService;
              import java.util.List;
              
              class Test {
                  void test(ProcessEngine engine) {
                      RuntimeService runtimeService = engine.getRuntimeService();
                  }
              }
              """
          )
        );
    }

    @Test
    void changePackageInFullyQualifiedNames() {
        rewriteRun(
          java(
            """
              class Test {
                  void test() {
                      org.camunda.bpm.engine.ProcessEngine engine = 
                          org.camunda.bpm.engine.ProcessEngines.getDefaultProcessEngine();
                  }
              }
              """,
              """
              class Test {
                  void test() {
                      org.operaton.bpm.engine.ProcessEngine engine = 
                          org.operaton.bpm.engine.ProcessEngines.getDefaultProcessEngine();
                  }
              }
              """
          )
        );
    }

    @Test
    void changePackageInPackageDeclaration() {
        rewriteRun(
          java(
            """
              package org.camunda.example;
              
              class Test {
                  void test() {
                      System.out.println("Hello, world!");
                  }
              }
              """,
              """
              package org.operaton.example;
              
              class Test {
                  void test() {
                      System.out.println("Hello, world!");
                  }
              }
              """
          )
        );
    }
}
