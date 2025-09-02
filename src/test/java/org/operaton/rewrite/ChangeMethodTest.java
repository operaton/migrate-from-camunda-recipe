package org.operaton.rewrite;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ChangeMethodTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources(
          "org.operaton.rewrite.ChangeMethod",
          "org.operaton.rewrite.ChangeType",
          "org.operaton.rewrite.ChangePackage");
    }

    @DocumentExample
    @Test
    void migrateBpmnModelInstance() {
        rewriteRun(
          java(
              """
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
        rewriteRun(java(
            """
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
            """,
            """
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

    @Test
    void migrateOverridenMethod() {
        rewriteRun(java(
            """
            import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
            import org.camunda.bpm.model.cmmn.impl.instance.camunda.CamundaVariableListenerImpl;
            import org.camunda.bpm.model.cmmn.instance.camunda.CamundaScript;
            import org.camunda.bpm.model.cmmn.instance.camunda.CamundaVariableListener;
            
            public class CustomVariableListener extends CamundaVariableListenerImpl implements CamundaVariableListener {
            
              public CustomVariableListener(ModelTypeInstanceContext instanceContext) {
                super(instanceContext);
              }
            
              @Override
              public void setCamundaScript(CamundaScript script) {
                System.out.println("test");
                super.setCamundaScript(script);
              }
            }
            """,
            """
            import org.operaton.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
            import org.operaton.bpm.model.cmmn.impl.instance.operaton.OperatonVariableListenerImpl;
            import org.operaton.bpm.model.cmmn.instance.operaton.OperatonScript;
            import org.operaton.bpm.model.cmmn.instance.operaton.OperatonVariableListener;
            
            public class CustomVariableListener extends OperatonVariableListenerImpl implements OperatonVariableListener {
            
              public CustomVariableListener(ModelTypeInstanceContext instanceContext) {
                super(instanceContext);
              }
            
              @Override
              public void setOperatonScript(OperatonScript script) {
                System.out.println("test");
                super.setOperatonScript(script);
              }
            }
            """));
    }
}
