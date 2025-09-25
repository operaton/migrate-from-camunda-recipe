package org.operaton.rewrite;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ChangeConstantTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.ChangeConstant");
    }

    @DocumentExample
    @Test
    void migrateBpmnModelInstance() {
        rewriteRun(
          java(
            """
              import static org.camunda.bpm.engine.impl.bpmn.parser.BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS;
              
              public class Example {
              
                  public void useConstant() {
                      System.out.println(CAMUNDA_BPMN_EXTENSIONS_NS.getNamespaceUri());
                  }
              }
              """,
            """
              import static org.operaton.bpm.engine.impl.bpmn.parser.BpmnParse.OPERATON_BPMN_EXTENSIONS_NS;
              
              public class Example {
              
                  public void useConstant() {
                      System.out.println(OPERATON_BPMN_EXTENSIONS_NS.getNamespaceUri());
                  }
              }
              """
          )
        );
    }
}
