package org.operaton.rewrite;

import org.checkerframework.errorprone.dataflow.expression.FieldAccess;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Tree;
import org.openrewrite.TreeVisitor;
import org.openrewrite.analysis.trait.variable.VariableUtil;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.TypeMatcher;
import org.openrewrite.java.trait.VariableAccess;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Space;
import org.openrewrite.java.tree.J.VariableDeclarations;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.Markers;

import java.lang.ProcessBuilder.Redirect.Type;
import java.util.List;
import java.util.stream.Collectors;

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

                // skip if not private or already static
                if (!multiVariable.hasModifier(J.Modifier.Type.Private) || multiVariable.hasModifier(J.Modifier.Type.Static)) {
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