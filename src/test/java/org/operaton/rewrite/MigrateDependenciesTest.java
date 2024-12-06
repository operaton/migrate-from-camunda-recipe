package org.operaton.rewrite;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.gradle.Assertions.buildGradle;
import static org.openrewrite.gradle.toolingapi.Assertions.withToolingApi;
import static org.openrewrite.java.Assertions.mavenProject;
import static org.openrewrite.maven.Assertions.pomXml;


class MigrateDependenciesTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.ReplaceCamundaDependencies");
    }

    @Test
    void migrateCamundaEngineMaven() {
        rewriteRun(
          mavenProject("project", pomXml(
            """
            <project>
               <groupId>org.operaton.test</groupId>
               <artifactId>test-app</artifactId>
               <version>1</version>
               <dependencies>
                 <dependency>
                   <groupId>org.camunda.bpm</groupId>
                   <artifactId>camunda-engine</artifactId>
                   <version>7.22.0</version>
                 </dependency>
               </dependencies>
           </project>
           """,

            """
             <project>
                <groupId>org.operaton.test</groupId>
                <artifactId>test-app</artifactId>
                <version>1</version>
                <dependencies>
                  <dependency>
                    <groupId>org.operaton.bpm</groupId>
                    <artifactId>operaton-engine</artifactId>
                    <version>1.0.0-beta-1</version>
                  </dependency>
                </dependencies>
            </project>
            """)));
    }

    @Test
    void migrateCamundaEngineMavenWithProperty() {
        rewriteRun(
          mavenProject("project", pomXml(
            """
            <project>
                <groupId>org.operaton.test</groupId>
                <artifactId>test-app</artifactId>
                <version>1</version>
                <properties>
                    <camunda-engine.version>7.22.0</camunda-engine.version>
                </properties>
                <dependencies>
                  <dependency>
                    <groupId>org.camunda.bpm</groupId>
                    <artifactId>camunda-engine</artifactId>
                    <version>${camunda-engine.version}</version>
                  </dependency>
                </dependencies>
            </project>
            """,
            """
            <project>
                <groupId>org.operaton.test</groupId>
                <artifactId>test-app</artifactId>
                <version>1</version>
                <properties>
                    <camunda-engine.version>7.22.0</camunda-engine.version>
                </properties>
                <dependencies>
                  <dependency>
                    <groupId>org.operaton.bpm</groupId>
                    <artifactId>operaton-engine</artifactId>
                    <version>1.0.0-beta-1</version>
                  </dependency>
                </dependencies>
            </project>
            """)));
    }

    @Test
    void migrateCamundaEngineWithManagedVersionMaven() {
        rewriteRun(
          mavenProject("project", pomXml(
            //language=xml
            """
            <project>
               <groupId>org.operaton.test</groupId>
               <artifactId>test-app</artifactId>
               <version>1</version>
               <dependencyManagement>
                 <dependencies>
                   <dependency>
                     <groupId>org.camunda.bpm</groupId>
                     <artifactId>camunda-bom</artifactId>
                     <version>7.22.0</version>
                     <scope>import</scope>
                     <type>pom</type>
                   </dependency>
                 </dependencies>
               </dependencyManagement>
               <dependencies>
                 <dependency>
                   <groupId>org.camunda.bpm</groupId>
                   <artifactId>camunda-engine</artifactId>
                 </dependency>
               </dependencies>
           </project>
           """,

            //language=xml
            """
            <project>
               <groupId>org.operaton.test</groupId>
               <artifactId>test-app</artifactId>
               <version>1</version>
               <dependencyManagement>
                 <dependencies>
                   <dependency>
                     <groupId>org.operaton.bpm</groupId>
                     <artifactId>operaton-bom</artifactId>
                     <version>1.0.0-beta-1</version>
                     <scope>import</scope>
                     <type>pom</type>
                   </dependency>
                 </dependencies>
               </dependencyManagement>
               <dependencies>
                 <dependency>
                   <groupId>org.operaton.bpm</groupId>
                   <artifactId>operaton-engine</artifactId>
                 </dependency>
               </dependencies>
           </project>
           """)));
    }

    @Test
    void migrateCamundaEngineGradle() {
        rewriteRun(
          spec -> spec.beforeRecipe(withToolingApi()),
          //language=groovy
          buildGradle(
                """
              plugins {
                id('java')
              }
              
              repositories {
                  mavenCentral()
              }
              
              dependencies {
                  implementation 'org.camunda.bpm:camunda-engine:7.22.0'
              }
              """,
            """
              plugins {
                id('java')
              }
              
              repositories {
                  mavenCentral()
              }
              
              dependencies {
                  implementation 'org.operaton.bpm:operaton-engine:1.0.0-beta-1'
              }
              """));
    }
}
