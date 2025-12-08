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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

class MakeProcessEngineStaticTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.parser(
            JavaParser.fromJavaVersion()
              .classpath("operaton-engine")
              .dependsOn(
                //language=java
                """
                  package org.operaton.bpm.engine;
                  
                  public class ProcessEngineExtension {
                      public static Builder builder() { return new Builder(); }
                      public static class Builder {
                          public Builder configurationResource(String resource) { return this; }
                          public ProcessEngineExtension build() { return new ProcessEngineExtension(); }
                      }
                  }
                  """
              )
          )
          .recipe(new org.operaton.rewrite.MakeProcessEngineStatic());
    }

    @Test
    @DocumentExample
    void updateThisToClassName() {
        rewriteRun(
          java(
            """
              import org.operaton.bpm.engine.ProcessEngineExtension;
              
              public class Example {
                  private ProcessEngineExtension extension = ProcessEngineExtension.builder()
                      .configurationResource("operaton.local.cfg.xml")
                      .build();
              }
              """,
            """
              import org.operaton.bpm.engine.ProcessEngineExtension;
              
              public class Example {
                  private static ProcessEngineExtension extension = ProcessEngineExtension.builder()
                      .configurationResource("operaton.local.cfg.xml")
                      .build();
              }
              """
          )
        );
    }

    @Nested
    class DoNothing {
        @Test
        void alreadyStatic() {
            rewriteRun(
              java(
                //language=java
                """
                  import org.operaton.bpm.engine.ProcessEngineExtension;
                  
                  public class Example {
                      private static ProcessEngineExtension extension = ProcessEngineExtension.builder()
                          .configurationResource("operaton.local.cfg.xml")
                          .build();
                  }
                  """
              )
            );
        }

        @Test
        void differentClass() {
            rewriteRun(
              java(
                """
                  public class OtherClass { }
                  """
              ),
              java(
                //language=java
                """
                  import org.operaton.bpm.engine.ProcessEngineExtension;
                  
                  public class Example {
                      private OtherClass extension = new OtherClass();
                  }
                  """
              )
            );
        }
    }
}
