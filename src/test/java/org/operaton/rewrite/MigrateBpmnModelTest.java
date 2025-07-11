package org.operaton.rewrite;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class MigrateBpmnModelTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.MigrateBpmnModel");
    }

    @Test
    void migrateBpmnModelInstance() {
        rewriteRun(
          java(
            """
              package com.example.rewrite.test;
              import org.camunda.bpm.model.bpmn.Bpmn;
              import org.camunda.bpm.model.bpmn.BpmnModelInstance;

              class TestBpmnModelBuilder {
                  void testCamundaBpmnModelBuilder() {
                      BpmnModelInstance orderProcess = Bpmn.createProcess()
                        .camundaVersionTag("1")
                        .id("order-process")
                        .name("Order Process")
                        .executable()
                        .startEvent("order-process-start-event")
                        .name("order-process-start-event")
                        .camundaAsyncBefore()
                        .serviceTask("store-order-task")
                        .name("store-order-task")
                        .camundaDelegateExpression("${storeOrderTask}")
                        .camundaAsyncBefore()
                        .endEvent("process-order-end-event")
                        .done();
                  }
              }
              """,
              """
              package com.example.rewrite.test;
              import org.operaton.bpm.model.bpmn.Bpmn;
              import org.operaton.bpm.model.bpmn.BpmnModelInstance;

              class TestBpmnModelBuilder {
                  void testCamundaBpmnModelBuilder() {
                      BpmnModelInstance orderProcess = Bpmn.createProcess()
                        .operatonVersionTag("1")
                        .id("order-process")
                        .name("Order Process")
                        .executable()
                        .startEvent("order-process-start-event")
                        .name("order-process-start-event")
                        .operatonAsyncBefore()
                        .serviceTask("store-order-task")
                        .name("store-order-task")
                        .operatonDelegateExpression("${storeOrderTask}")
                        .operatonAsyncBefore()
                        .endEvent("process-order-end-event")
                        .done();
                  }
              }
              """
          )
        );
    }

    @Test
    void migrateBpmnProcessInstance() {
        rewriteRun(java("""
            package org.operaton.rewrite;
            
            import org.camunda.bpm.model.bpmn.BpmnModelInstance;
            import org.camunda.bpm.model.bpmn.instance.Process;
            
            public class TestProcessApi {
            
                private final BpmnModelInstance processDefinitionModel;
            
                public TestProcessApi(BpmnModelInstance processDefinitionModel) {
                    this.processDefinitionModel = processDefinitionModel;
                }
            
                void printVersionTag() {
                    Process process = processDefinitionModel.getModelElementById("order-process");
                    System.out.println(process.getCamundaVersionTag());
                    System.out.println(process.getCamundaJobPriority());
                }
            }
            """, """
            package org.operaton.rewrite;
            
            import org.operaton.bpm.model.bpmn.BpmnModelInstance;
            import org.operaton.bpm.model.bpmn.instance.Process;
            
            public class TestProcessApi {
            
                private final BpmnModelInstance processDefinitionModel;
            
                public TestProcessApi(BpmnModelInstance processDefinitionModel) {
                    this.processDefinitionModel = processDefinitionModel;
                }
            
                void printVersionTag() {
                    Process process = processDefinitionModel.getModelElementById("order-process");
                    System.out.println(process.getOperatonVersionTag());
                    System.out.println(process.getOperatonJobPriority());
                }
            }
            """));
    }
}
