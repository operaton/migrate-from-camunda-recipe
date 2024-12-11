package org.operaton.rewrite;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

class ResolveDeprecationsTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.deprecation.ReplaceGetExecutionContext");
    }

    @Test
    void resolve_Context_getExecutionContext() {
        rewriteRun(
          spec -> spec.recipeFromResources("org.operaton.rewrite.deprecation.ReplaceGetExecutionContext"),
          java(
            """
              package foo;
              
              import org.camunda.bpm.engine.impl.context.Context;
              
              class Test {
                  void test() {
                      var ctx = Context.getExecutionContext();
                  }
              }
              """,
              """
              package foo;
              
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
