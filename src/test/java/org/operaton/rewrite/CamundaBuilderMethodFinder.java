package org.operaton.rewrite;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

/**
 * This test class is used to find all methods starting with "camunda" in the Camunda builder implementations
 * and methods starting with "getCamunda" in the org.camunda.bpm.model.bpmn.instance package.
 * It uses reflection to find all methods in these packages.
 * 
 * The class prints all found methods to the console and also stores them in static fields
 * that can be accessed by other classes if needed.
 */
class CamundaBuilderMethodFinder {

    // Static fields to store the list of methods found
    public static List<String> camundaMethodsFound = new java.util.ArrayList<>();
    public static List<String> getCamundaMethodsFound = new java.util.ArrayList<>();

    @Test
    void findCamundaMethods() {
        // Part 1: Find camunda methods in builder package
        findCamundaBuilderMethods();

        // Part 2: Find getCamunda methods in instance package
        findGetCamundaInstanceMethods();
    }

    private void findCamundaBuilderMethods() {
        // Get all classes in the org.camunda.bpm.model.bpmn.builder package
        Package pkg = org.camunda.bpm.model.bpmn.builder.AbstractBaseElementBuilder.class.getPackage();
        System.out.println("[DEBUG_LOG] Package: " + pkg.getName());

        // Find all interfaces in the package
        Set<Class<?>> interfaces = new HashSet<>();
        findInterfaces(org.camunda.bpm.model.bpmn.builder.AbstractBaseElementBuilder.class, interfaces);

        System.out.println("[DEBUG_LOG] Found " + interfaces.size() + " interfaces");

        // Also directly add known builder interfaces
        addKnownInterface(interfaces, "org.camunda.bpm.model.bpmn.builder.ProcessBuilder");
        addKnownInterface(interfaces, "org.camunda.bpm.model.bpmn.builder.StartEventBuilder");
        addKnownInterface(interfaces, "org.camunda.bpm.model.bpmn.builder.ServiceTaskBuilder");
        addKnownInterface(interfaces, "org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder");
        addKnownInterface(interfaces, "org.camunda.bpm.model.bpmn.builder.AbstractActivityBuilder");
        addKnownInterface(interfaces, "org.camunda.bpm.model.bpmn.builder.AbstractTaskBuilder");

        // Find all methods starting with "camunda"
        Set<String> camundaMethods = new HashSet<>();
        List<String> methodDetails = new java.util.ArrayList<>();

        for (Class<?> iface : interfaces) {
            System.out.println("[DEBUG_LOG] Interface: " + iface.getName());
            for (Method method : iface.getMethods()) {
                if (method.getName().startsWith("camunda")) {
                    String signature = method.getName() + Arrays.toString(method.getParameterTypes());
                    if (camundaMethods.add(signature)) {  // Only add if it's a new method
                        String methodDetail = method.getName() + " - Parameters: " + 
                                          Arrays.stream(method.getParameterTypes())
                                                .map(Class::getSimpleName)
                                                .collect(Collectors.joining(", "));
                        methodDetails.add(methodDetail);
                        System.out.println("[DEBUG_LOG]   Method: " + methodDetail);
                    }
                }
            }
        }

        // Print all found methods in alphabetical order
        System.out.println("[DEBUG_LOG] ========== All Camunda Methods ==========");
        methodDetails.stream()
            .sorted()
            .forEach(method -> System.out.println("[DEBUG_LOG] " + method));
        System.out.println("[DEBUG_LOG] ========================================");

        // Print a YAML-formatted list for easy copying to bpmn-model.yml
        System.out.println("[DEBUG_LOG] ========== YAML Format for bpmn-model.yml (camunda methods) ==========");
        methodDetails.stream()
            .sorted()
            .forEach(method -> {
                try {
                    String methodName = method.split(" -")[0];
                    String params = "";
                    if (method.contains("Parameters:")) {
                        String[] parts = method.split("Parameters: ");
                        if (parts.length > 1) {
                            params = parts[1];
                        }
                    }
                    String paramTypes = Arrays.stream(params.split(", "))
                        .filter(p -> !p.isEmpty())
                        .map(p -> p.trim())
                        .collect(Collectors.joining(", "));

                    System.out.println("[DEBUG_LOG]   - org.openrewrite.java.ChangeMethodName:");
                    System.out.println("[DEBUG_LOG]       methodPattern: 'org.operaton.bpm.model.bpmn.builder.* " + methodName + "(" + paramTypes + ")'");
                    System.out.println("[DEBUG_LOG]       newMethodName: 'operaton" + methodName.substring(7) + "'");
                    System.out.println("[DEBUG_LOG]");
                } catch (Exception e) {
                    System.out.println("[DEBUG_LOG] Error processing method: " + method + " - " + e.getMessage());
                }
            });
        System.out.println("[DEBUG_LOG] ====================================================");

        System.out.println("[DEBUG_LOG] Total camunda methods found: " + camundaMethods.size());

        // Create a summary string that will be included in the assertion message
        String methodSummary = "Found " + camundaMethods.size() + " camunda methods:\n" +
            methodDetails.stream()
                .sorted()
                .collect(Collectors.joining("\n"));

        // Store the sorted method details in the static field
        camundaMethodsFound.clear();
        methodDetails.stream()
            .sorted()
            .forEach(camundaMethodsFound::add);

        // Print a message to indicate that the methods have been stored
        System.out.println("[DEBUG_LOG] Stored " + camundaMethodsFound.size() + " methods in camundaMethodsFound");

        // Use the summary in the assertion message
        org.assertj.core.api.Assertions.assertThat(camundaMethods)
            .as(methodSummary)
            .isNotEmpty();

        // Also print a message to standard error, which might be more visible in the test output
        System.err.println("CamundaBuilderMethodFinder found " + camundaMethods.size() + " methods starting with 'camunda'");
        System.err.println("First few methods: " + 
            camundaMethodsFound.stream().limit(5).collect(Collectors.joining(", ")));
    }

    private void addKnownInterface(Set<Class<?>> interfaces, String className) {
        try {
            Class<?> interfaceClass = Class.forName(className);
            interfaces.add(interfaceClass);
            System.out.println("[DEBUG_LOG] Added " + className);
        } catch (ClassNotFoundException e) {
            System.out.println("[DEBUG_LOG] " + className + " not found: " + e.getMessage());
        }
    }

    private void findInterfaces(Class<?> clazz, Set<Class<?>> interfaces) {
        if (clazz == null) return;

        // Add all interfaces implemented by this class
        for (Class<?> iface : clazz.getInterfaces()) {
            if (iface.getName().startsWith("org.camunda.bpm.model.bpmn.builder")) {
                interfaces.add(iface);
                System.out.println("[DEBUG_LOG] Found interface: " + iface.getName());
                // Also add interfaces extended by this interface
                findInterfaces(iface, interfaces);
            }
        }

        // Also check superclass
        findInterfaces(clazz.getSuperclass(), interfaces);
    }

    private void findGetCamundaInstanceMethods() {
        System.out.println("[DEBUG_LOG] ========== Finding getCamunda methods in instance package ==========");

        // Get all classes in the org.camunda.bpm.model.bpmn.instance package
        Set<Class<?>> instanceInterfaces = new HashSet<>();

        // Add known instance interfaces
        addKnownInterface(instanceInterfaces, "org.camunda.bpm.model.bpmn.instance.BaseElement");
        addKnownInterface(instanceInterfaces, "org.camunda.bpm.model.bpmn.instance.FlowElement");
        addKnownInterface(instanceInterfaces, "org.camunda.bpm.model.bpmn.instance.FlowNode");
        addKnownInterface(instanceInterfaces, "org.camunda.bpm.model.bpmn.instance.Activity");
        addKnownInterface(instanceInterfaces, "org.camunda.bpm.model.bpmn.instance.Task");
        addKnownInterface(instanceInterfaces, "org.camunda.bpm.model.bpmn.instance.ServiceTask");
        addKnownInterface(instanceInterfaces, "org.camunda.bpm.model.bpmn.instance.Process");
        addKnownInterface(instanceInterfaces, "org.camunda.bpm.model.bpmn.instance.Event");
        addKnownInterface(instanceInterfaces, "org.camunda.bpm.model.bpmn.instance.StartEvent");
        addKnownInterface(instanceInterfaces, "org.camunda.bpm.model.bpmn.instance.EndEvent");

        System.out.println("[DEBUG_LOG] Found " + instanceInterfaces.size() + " instance interfaces");

        // Find all methods starting with "getCamunda"
        Set<String> getCamundaMethods = new HashSet<>();
        List<String> methodDetails = new java.util.ArrayList<>();

        for (Class<?> iface : instanceInterfaces) {
            System.out.println("[DEBUG_LOG] Instance Interface: " + iface.getName());
            for (Method method : iface.getMethods()) {
                if (method.getName().startsWith("getCamunda")) {
                    String signature = method.getName() + Arrays.toString(method.getParameterTypes());
                    if (getCamundaMethods.add(signature)) {  // Only add if it's a new method
                        String methodDetail = method.getName() + " - Parameters: " + 
                                          Arrays.stream(method.getParameterTypes())
                                                .map(Class::getSimpleName)
                                                .collect(Collectors.joining(", "));
                        methodDetails.add(methodDetail);
                        System.out.println("[DEBUG_LOG]   Method: " + methodDetail);
                    }
                }
            }
        }

        // Print all found methods in alphabetical order
        System.out.println("[DEBUG_LOG] ========== All getCamunda Methods ==========");
        methodDetails.stream()
            .sorted()
            .forEach(method -> System.out.println("[DEBUG_LOG] " + method));
        System.out.println("[DEBUG_LOG] ========================================");

        // Print a YAML-formatted list for easy copying to bpmn-model.yml
        System.out.println("[DEBUG_LOG] ========== YAML Format for bpmn-model.yml (getCamunda methods) ==========");
        methodDetails.stream()
            .sorted()
            .forEach(method -> {
                try {
                    String methodName = method.split(" -")[0];
                    String params = "";
                    if (method.contains("Parameters:")) {
                        String[] parts = method.split("Parameters: ");
                        if (parts.length > 1) {
                            params = parts[1];
                        }
                    }
                    String paramTypes = Arrays.stream(params.split(", "))
                        .filter(p -> !p.isEmpty())
                        .map(p -> p.trim())
                        .collect(Collectors.joining(", "));

                    // For getCamundaXXX methods, we replace with getOperatonXXX
                    String newMethodName = "getOperaton" + methodName.substring(10);

                    System.out.println("[DEBUG_LOG]   - org.openrewrite.java.ChangeMethodName:");
                    System.out.println("[DEBUG_LOG]       methodPattern: 'org.operaton.bpm.model.bpmn.instance.* " + methodName + "(" + paramTypes + ")'");
                    System.out.println("[DEBUG_LOG]       newMethodName: '" + newMethodName + "'");
                    System.out.println("[DEBUG_LOG]");
                } catch (Exception e) {
                    System.out.println("[DEBUG_LOG] Error processing method: " + method + " - " + e.getMessage());
                }
            });
        System.out.println("[DEBUG_LOG] ====================================================");

        System.out.println("[DEBUG_LOG] Total getCamunda methods found: " + getCamundaMethods.size());

        // Store the sorted method details in the static field
        getCamundaMethodsFound.clear();
        methodDetails.stream()
            .sorted()
            .forEach(getCamundaMethodsFound::add);

        // Print a message to indicate that the methods have been stored
        System.out.println("[DEBUG_LOG] Stored " + getCamundaMethodsFound.size() + " methods in getCamundaMethodsFound");
    }
}
