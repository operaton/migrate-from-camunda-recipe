# Camunda to Operaton Artifact Mapping

This page documents the mapping between Camunda and Operaton artifacts based on the comparison of `camunda-only-bom.xml` and `operaton-only-bom.xml`.

## Key Changes

1. The base groupId has changed from `org.camunda.bpm` to `org.operaton.bpm`
2. All artifact IDs have been renamed from `camunda-*` to `operaton-*`
3. Camunda artifacts with `-jakarta` suffix are mapped to the same Operaton artifacts as their non-Jakarta counterparts
4. The `javaee` package in Camunda has been renamed to `jakartaee` in Operaton
5. Operaton has added `junit4` and `junit5` classifiers for the engine artifact
6. All Operaton dependencies use version `1.0.0-beta-4` as the replacement version

## Mapping Table

| Camunda GroupId | Camunda ArtifactId | Operaton GroupId | Operaton ArtifactId | Replacement Version |
|-----------------|-------------------|------------------|---------------------|-------------------|
| org.camunda.bpm | camunda-bom-root | org.operaton.bpm | operaton-bom-root | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-bpm-assert | org.operaton.bpm | operaton-bpm-assert | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-bpm-junit5 | org.operaton.bpm | operaton-bpm-junit5 | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine | org.operaton.bpm | operaton-engine | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine-cdi | org.operaton.bpm | operaton-engine-cdi | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine-cdi-jakarta | org.operaton.bpm | operaton-engine-cdi | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine-plugin-connect | org.operaton.bpm | operaton-engine-plugin-connect | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine-plugin-spin | org.operaton.bpm | operaton-engine-plugin-spin | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine-rest | org.operaton.bpm | operaton-engine-rest | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine-rest-core | org.operaton.bpm | operaton-engine-rest-core | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine-rest-core-jakarta | org.operaton.bpm | operaton-engine-rest-core | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine-rest-jakarta | org.operaton.bpm | operaton-engine-rest | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine-spring | org.operaton.bpm | operaton-engine-spring | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-engine-spring-6 | org.operaton.bpm | operaton-engine-spring | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-external-task-client | org.operaton.bpm | operaton-external-task-client | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-external-task-client-spring | org.operaton.bpm | operaton-external-task-client-spring | 1.0.0-beta-4 |
| org.camunda.bpm | camunda-only-bom | org.operaton.bpm | operaton-only-bom | 1.0.0-beta-4 |
| org.camunda.bpm.dmn | camunda-engine-dmn | org.operaton.bpm.dmn | operaton-engine-dmn | 1.0.0-beta-4 |
| org.camunda.bpm.dmn | camunda-engine-feel-api | org.operaton.bpm.dmn | operaton-engine-feel-api | 1.0.0-beta-4 |
| org.camunda.bpm.dmn | camunda-engine-feel-juel | org.operaton.bpm.dmn | operaton-engine-feel-juel | 1.0.0-beta-4 |
| org.camunda.bpm.dmn | camunda-engine-feel-scala | org.operaton.bpm.dmn | operaton-engine-feel-scala | 1.0.0-beta-4 |
| org.camunda.bpm.identity | camunda-identity-ldap | org.operaton.bpm.identity | operaton-identity-ldap | 1.0.0-beta-4 |
| org.camunda.bpm.javaee | camunda-ejb-client | org.operaton.bpm.jakartaee | operaton-ejb-client | 1.0.0-beta-4 |
| org.camunda.bpm.javaee | camunda-ejb-client-jakarta | org.operaton.bpm.jakartaee | operaton-ejb-client | 1.0.0-beta-4 |
| org.camunda.bpm.juel | camunda-juel | org.operaton.bpm.juel | operaton-juel | 1.0.0-beta-4 |
| org.camunda.bpm.model | camunda-bpmn-model | org.operaton.bpm.model | operaton-bpmn-model | 1.0.0-beta-4 |
| org.camunda.bpm.model | camunda-cmmn-model | org.operaton.bpm.model | operaton-cmmn-model | 1.0.0-beta-4 |
| org.camunda.bpm.model | camunda-dmn-model | org.operaton.bpm.model | operaton-dmn-model | 1.0.0-beta-4 |
| org.camunda.bpm.model | camunda-xml-model | org.operaton.bpm.model | operaton-xml-model | 1.0.0-beta-4 |
| org.camunda.bpm.quarkus | camunda-bpm-quarkus-engine | org.operaton.bpm.quarkus | operaton-bpm-quarkus-engine | 1.0.0-beta-4 |
| org.camunda.bpm.springboot | camunda-bpm-spring-boot-starter | org.operaton.bpm.springboot | operaton-bpm-spring-boot-starter | 1.0.0-beta-4 |
| org.camunda.bpm.springboot | camunda-bpm-spring-boot-starter-external-task-client | org.operaton.bpm.springboot | operaton-bpm-spring-boot-starter-external-task-client | 1.0.0-beta-4 |
| org.camunda.bpm.springboot | camunda-bpm-spring-boot-starter-rest | org.operaton.bpm.springboot | operaton-bpm-spring-boot-starter-rest | 1.0.0-beta-4 |
| org.camunda.bpm.springboot | camunda-bpm-spring-boot-starter-security | org.operaton.bpm.springboot | operaton-bpm-spring-boot-starter-security | 1.0.0-beta-4 |
| org.camunda.bpm.springboot | camunda-bpm-spring-boot-starter-test | org.operaton.bpm.springboot | operaton-bpm-spring-boot-starter-test | 1.0.0-beta-4 |
| org.camunda.bpm.springboot | camunda-bpm-spring-boot-starter-webapp | org.operaton.bpm.springboot | operaton-bpm-spring-boot-starter-webapp | 1.0.0-beta-4 |
| org.camunda.bpm.springboot | camunda-bpm-spring-boot-starter-webapp-ee | org.operaton.bpm.springboot | operaton-bpm-spring-boot-starter-webapp | 1.0.0-beta-4 |
| org.camunda.bpm.webapp | camunda-webapp | org.operaton.bpm.webapp | operaton-webapp | 1.0.0-beta-4 |
| org.camunda.bpm.webapp | camunda-webapp-jakarta | org.operaton.bpm.webapp | operaton-webapp | 1.0.0-beta-4 |
| org.camunda.bpm.webapp | camunda-webapp-webjar | org.operaton.bpm.webapp | operaton-webapp-webjar | 1.0.0-beta-4 |
| org.camunda.bpm.webapp | camunda-webapp-webjar-ee | org.operaton.bpm.webapp | operaton-webapp-webjar | 1.0.0-beta-4 |
| org.camunda.commons | camunda-commons-bom | org.operaton.commons | operaton-commons-bom | 1.0.0-beta-4 |
| org.camunda.commons | camunda-commons-logging | org.operaton.commons | operaton-commons-logging | 1.0.0-beta-4 |
| org.camunda.commons | camunda-commons-testing | org.operaton.commons | operaton-commons-testing | 1.0.0-beta-4 |
| org.camunda.commons | camunda-commons-typed-values | org.operaton.commons | operaton-commons-typed-values | 1.0.0-beta-4 |
| org.camunda.commons | camunda-commons-utils | org.operaton.commons | operaton-commons-utils | 1.0.0-beta-4 |
| org.camunda.connect | camunda-connect-bom | org.operaton.connect | operaton-connect-bom | 1.0.0-beta-4 |
| org.camunda.connect | camunda-connect-connectors-all | org.operaton.connect | operaton-connect-connectors-all | 1.0.0-beta-4 |
| org.camunda.connect | camunda-connect-core | org.operaton.connect | operaton-connect-core | 1.0.0-beta-4 |
| org.camunda.connect | camunda-connect-http-client | org.operaton.connect | operaton-connect-http-client | 1.0.0-beta-4 |
| org.camunda.connect | camunda-connect-soap-http-client | org.operaton.connect | operaton-connect-soap-http-client | 1.0.0-beta-4 |
| org.camunda.spin | camunda-spin-bom | org.operaton.spin | operaton-spin-bom | 1.0.0-beta-4 |
| org.camunda.spin | camunda-spin-core | org.operaton.spin | operaton-spin-core | 1.0.0-beta-4 |
| org.camunda.spin | camunda-spin-dataformat-all | org.operaton.spin | operaton-spin-dataformat-all | 1.0.0-beta-4 |
| org.camunda.spin | camunda-spin-dataformat-json-jackson | org.operaton.spin | operaton-spin-dataformat-json-jackson | 1.0.0-beta-4 |
| org.camunda.spin | camunda-spin-dataformat-xml-dom | org.operaton.spin | operaton-spin-dataformat-xml-dom | 1.0.0-beta-4 |
| org.camunda.spin | camunda-spin-dataformat-xml-dom-jakarta | org.operaton.spin | operaton-spin-dataformat-xml-dom | 1.0.0-beta-4 |
| org.camunda.template-engines | camunda-template-engines-freemarker | org.operaton.template-engines | operaton-template-engines-freemarker | 1.0.0-beta-4 |
