package org.operaton.rewrite;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

class MigrateBpmnModelExtendedTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.MigrateBpmnModel");
    }

    @Test
    void migrateExtendedBpmnModelMethods() {
        rewriteRun(
          java(
            """
              package org.operaton.rewrite;

              import org.camunda.bpm.model.bpmn.Bpmn;
              import org.camunda.bpm.model.bpmn.BpmnModelInstance;

              class TestExtendedBpmnModelBuilder {
                  void testCamundaBpmnModelBuilder() {
                      BpmnModelInstance process = Bpmn.createProcess()
                        .camundaVersionTag("1.0")
                        .id("process-id")
                        .executable()
                        .startEvent("start")
                        .camundaAsyncBefore()
                        .serviceTask("service-task")
                        .camundaDelegateExpression("${serviceTask}")
                        .camundaAsyncBefore()
                        .endEvent("end")
                        .done();
                  }
              }
              """,
              """
              package org.operaton.rewrite;

              import org.operaton.bpm.model.bpmn.Bpmn;
              import org.operaton.bpm.model.bpmn.BpmnModelInstance;

              class TestExtendedBpmnModelBuilder {
                  void testCamundaBpmnModelBuilder() {
                      BpmnModelInstance process = Bpmn.createProcess()
                        .operatonVersionTag("1.0")
                        .id("process-id")
                        .executable()
                        .startEvent("start")
                        .operatonAsyncBefore()
                        .serviceTask("service-task")
                        .operatonDelegateExpression("${serviceTask}")
                        .operatonAsyncBefore()
                        .endEvent("end")
                        .done();
                  }
              }
              """
          )
        );
    }
}
