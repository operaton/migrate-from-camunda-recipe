package org.operaton.rewrite;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ChangeTypeTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.ChangeType", "org.operaton.rewrite.ChangePackage");
    }

    @Test
    void testChangeInterface() {
        rewriteRun(
          java("""
            import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
            import org.camunda.bpm.spring.boot.starter.configuration.CamundaProcessEngineConfiguration;
            
            public class Test implements CamundaProcessEngineConfiguration {
            
                @Override
                public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
                    System.out.println("test pre-init");
                }
            }""", """
            import org.operaton.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
            import org.operaton.bpm.spring.boot.starter.configuration.OperatonProcessEngineConfiguration;
            
            public class Test implements OperatonProcessEngineConfiguration {
            
                @Override
                public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
                    System.out.println("test pre-init");
                }
            }"""
          )
        );
    }

}
