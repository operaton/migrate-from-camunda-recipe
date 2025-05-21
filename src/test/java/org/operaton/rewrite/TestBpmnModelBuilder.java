package org.operaton.rewrite;

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

    void testOperatonBpmnModelBuilder() {
        org.operaton.bpm.model.bpmn.BpmnModelInstance orderProcess = org.operaton.bpm.model.bpmn.Bpmn.createProcess()
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
