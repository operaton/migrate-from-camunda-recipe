/*
 * Copyright 2025 the Operaton contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.operaton.rewrite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openrewrite.test.SourceSpecs.text;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class RenameServiceLoaderTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("org.operaton.rewrite.RenameServiceLoader");
    }

    @DocumentExample
    @Test
    void hasFileMatch() {
        // HEADS-UP: fqdn in a service loader file would not be renamed.
        rewriteRun(text("org.camunda.bpm.Foo", "org.camunda.bpm.Foo",
          spec -> spec.path("src/main/resources/META-INF/services/org.camunda.spin.spi.DataFormatConfigurator")
            .afterRecipe(pt -> assertThat(pt.getSourcePath()).isEqualTo(
              Path.of("src/main/resources/META-INF/services/org.operaton.spin.spi.DataFormatConfigurator")))));
    }

}
