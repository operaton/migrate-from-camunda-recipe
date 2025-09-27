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

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Tree;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.TypeMatcher;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.VariableDeclarations;
import org.openrewrite.java.tree.Space;
import org.openrewrite.marker.Markers;

import static java.util.Collections.emptyList;

public class MakeProcessEngineStatic extends Recipe {
    @Override
    public String getDisplayName() {
        return "Make ProcessEngineExtension field static and update references";
    }

    @Override
    public String getDescription() {
        return "Migrates ProcessEngineExtension field to static and updates references from 'this.extension' to 'ClassName.extension'.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaVisitor<ExecutionContext>() {
            final TypeMatcher processEngineExtensionType = new TypeMatcher("org.operaton.bpm.engine.ProcessEngineExtension");

            @Override
            public VariableDeclarations visitVariableDeclarations(VariableDeclarations multiVariable, ExecutionContext p) {
                // skip if not correct type
                if (!processEngineExtensionType.matches(multiVariable.getType())) {
                    return multiVariable;
                }

                // skip if already static
                if (multiVariable.hasModifier(J.Modifier.Type.Static)) {
                    return multiVariable;
                }

                // add static to field
                return multiVariable.withModifiers(ListUtils.concat(
                        multiVariable.getModifiers(),
                        new J.Modifier(Tree.randomId(), Space.SINGLE_SPACE, Markers.EMPTY, null, J.Modifier.Type.Static, emptyList())));
            }
        };
    }
}