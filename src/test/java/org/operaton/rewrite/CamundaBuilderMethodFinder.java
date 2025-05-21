package org.operaton.rewrite;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This test class is used to find all methods starting with "camunda" in the Camunda builder implementations.
 * It uses reflection to find all methods in the org.camunda.bpm.model.bpmn.builder package.
 */
class CamundaBuilderMethodFinder {

    @Test
    void findCamundaMethods() {
        // Get all classes in the org.camunda.bpm.model.bpmn.builder package
        Package pkg = org.camunda.bpm.model.bpmn.builder.AbstractBaseElementBuilder.class.getPackage();
        System.out.println("[DEBUG_LOG] Package: " + pkg.getName());

        // Find all interfaces in the package
        Set<Class<?>> interfaces = new HashSet<>();
        findInterfaces(org.camunda.bpm.model.bpmn.builder.AbstractBaseElementBuilder.class, interfaces);

        // Find all methods starting with "camunda"
        Set<String> camundaMethods = new HashSet<>();
        for (Class<?> iface : interfaces) {
            System.out.println("[DEBUG_LOG] Interface: " + iface.getName());
            for (Method method : iface.getMethods()) {
                if (method.getName().startsWith("camunda")) {
                    String signature = method.getName() + Arrays.toString(method.getParameterTypes());
                    camundaMethods.add(signature);
                    System.out.println("[DEBUG_LOG]   Method: " + method.getName() + " - Parameters: " + 
                                      Arrays.stream(method.getParameterTypes())
                                            .map(Class::getSimpleName)
                                            .collect(Collectors.joining(", ")));
                }
            }
        }

        System.out.println("[DEBUG_LOG] Total camunda methods found: " + camundaMethods.size());
    }

    private void findInterfaces(Class<?> clazz, Set<Class<?>> interfaces) {
        if (clazz == null) return;

        // Add all interfaces implemented by this class
        for (Class<?> iface : clazz.getInterfaces()) {
            if (iface.getName().startsWith("org.camunda.bpm.model.bpmn.builder")) {
                interfaces.add(iface);
                // Also add interfaces extended by this interface
                findInterfaces(iface, interfaces);
            }
        }

        // Also check superclass
        findInterfaces(clazz.getSuperclass(), interfaces);
    }
}
