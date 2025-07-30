package org.operaton.rewrite;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ResolveDeprecationsTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.deprecation.ReplaceGetExecutionContext");
    }

    @DocumentExample
    @Test
    void resolve_Context_getExecutionContext() {
        rewriteRun(
          spec -> spec.recipeFromResources("org.operaton.rewrite.deprecation.ReplaceGetExecutionContext"),
          java(
            """
              import org.camunda.bpm.engine.impl.context.Context;
              
              class Test {
                  void test() {
                      var ctx = Context.getExecutionContext();
                  }
              }
              """,
              """
              import org.camunda.bpm.engine.impl.context.Context;
              
              class Test {
                  void test() {
                      var ctx = Context.getBpmnExecutionContext();
                  }
              }
              """
          )
        );
    }
}
