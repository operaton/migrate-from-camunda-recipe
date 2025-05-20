# Change Gradle or Maven Dependencies

<https://docs.openrewrite.org/recipes/java/dependencies/changedependency>

## Camunda Platform 7

Migrate Dependencies managed by **camunda-bom** and
**camunda-only-bom**.

camunda

    org.camunda.bpm:camunda-bom:camunda-bom:7.23.0
    org.camunda.bpm:camunda-bom:camunda-only-bom:7.23.0

operaton

    org.operaton.bpm:operaton-bom:1.0.0
    org.operaton.bpm:operaton-only-bom:1.0.0

### Dependencies with changed artifactIds.

| Camunda | Operaton | Note |
|----|----|----|
| camunda-engine-cdi | \- | Java EE is no longer supported |
| camunda-engine-cdi-jakarta | operaton-engine-cdi | JakartaEE 10 onwards |
| camunda-engine-spring | \- | Spring 5 is no longer supported |
| camunda-engine-spring-6 | operaton-engine-spring | Spring 6 |
| camunda-engine-rest-core | \- | Java EE is no longer supported |
| camunda-engine-rest-core-jakarta | operaton-engine-rest-core | JakartaEE 10 onwards |
| camunda-ejb-client | \- | Java EE is no longer supported |
| camunda-ejb-client-jakarta | operaton-ejb-client | JakartaEE 10 onwards |
| camunda-engine-rest | \- | Java EE is no longer supported |
| camunda-engine-rest-jakarta | operaton-engine-rest | JakartaEE 10 onwards |
| camunda-webapp | \- | Java EE is no longer supported |
| camunda-webapp-jakarta | operaton-webapp | JakartaEE 10 onwards |
| camunda-webapp-webjar-ee | \- | Operaton has no enterprise edition |
| camunda-bpm-spring-boot-starter-webapp-ee | \- | Operaton has no enterprise edition |


## Camunda Community Hub

### Keycloak Identity Provider Plugin

camunda

    org.camunda.bpm.extension:camunda-platform-7-keycloak:7.23.0

operaton
    
    org.operaton.bpm.extension:operaton-keycloak:1.0.0


### Mockito Plugin

…

# Java Migrations

## Rename package name

Rename **org.camunda.bpm**. to **org.operaton.bpm.** package name in
package statements, imports, and fully-qualified types.

<https://docs.openrewrite.org/recipes/java/changepackage>
<https://docs.openrewrite.org/recipes/java/changepackageinstringliteral>

### Imports

camunda

    import org.camunda.bpm.

operaton

    import org.operaton.bpm.

### Package

> [!NOTE]
> it will actually move source files.

camunda

    package org.camunda.bpm.extension

operaton

    package org.operaton.bpm.extension

## Bpmn Java Api

The `Bpmn.createProcess()` Api has many Methods names prefixed with
`camunda`.

<div class="formalpara">

<div class="title">

example

</div>

    Bpmn.createProcess().camundaVersionTag(String.valueOf(pdk_version))
        .id(SimProcessEnum.STD_NC_STANDARD.key())
        .name(SimProcessEnum.STD_NC_STANDARD.key())
        .executable()
        .startEvent(PE_START).name(PE_START).camundaAsyncBefore()
        .serviceTask(PT_STORE_RESOURCE_ORDER).name(PT_STORE_RESOURCE_ORDER)
        .camundaDelegateExpression(getExpression(DELEGATE_SEND_TASKS)).camundaAsyncBefore

</div>

# Spring Boot

<https://docs.openrewrite.org/recipes/java/spring/changespringpropertykey>

##  Migrate Spring Boot application properties

camunda

    camunda.bpm.application.deployment-aware=true
    camunda.bpm.application.scan-for-process-definitions=true

operaton

    operaton.bpm.application.deployment-aware=true
    operaton.bpm.application.scan-for-process-definitions=true

## Remove unsupported Properties

The following properties are removed

camunda

    camunda.bpm.license-file=/etc/c7.txt


# Migrate XML Deployment Descriptor Files

## Process Application

<https://docs.openrewrite.org/recipes/xml/changenamespacevalue>

camunda

    <process-application xmlns="http://www.camunda.org/schema/1.0/ProcessApplication">
      <process-archive>
      </process-archive>
    </process-application>

operaton

    <process-application xmlns="http://www.operaton.org/schema/1.0/ProcessApplication">
      <process-archive>
      </process-archive>
    </process-application>
