package org.operaton.rewrite;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.openrewrite.*;
import org.openrewrite.maven.MavenIsoVisitor;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.tree.Xml;

import java.util.*;

@Value
@EqualsAndHashCode(callSuper = false)
public class ResolveDependencyVersionProperty extends ScanningRecipe<Map<String, Set<String>>> {

    @Option
    String groupId;

    @Option
    String artifactId;

    @Override
    public @NlsRewrite.DisplayName String getDisplayName() {
        return "Resolve dependency version property for dependency";
    }

    @Override
    public @NlsRewrite.Description String getDescription() {
        return "Resolve the dependency version property for a specific dependency.";
    }

    @Override
    public Map<String, Set<String>> getInitialValue(ExecutionContext ctx) {
        return new HashMap<>();
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getScanner(Map<String, Set<String>> acc) {
        return new MavenIsoVisitor<>() {

            private final XPathMatcher propertyDefiningTag = new XPathMatcher("properties/*");

            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
                tag = super.visitTag(tag, executionContext);
                if (propertyDefiningTag.matches(getCursor())) {
                    String propertyName = tag.getName();
                    tag.getValue().ifPresent(
                            s -> acc.computeIfAbsent(propertyName, k -> new HashSet<>())
                                    .add(s));
                }
                return tag;
            }
        };
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor(Map<String, Set<String>> acc) {
        return new MavenIsoVisitor<>() {
            private final XPathMatcher dependencyVersionTag = new XPathMatcher("dependency/version");

            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
                tag = super.visitTag(tag, executionContext);

                if (isDependencyVersionTag() && isDefinedInDesiredDependencyTag()) {
                    Set<String> propertyValues = tag.getValue()
                            .filter(s -> s.startsWith("${"))
                            .filter(s -> s.endsWith("}"))
                            .map(s -> s.substring(2, s.length() - 1))
                            .map(acc::get)
                            .orElseGet(Set::of);

                    if (propertyValues.size() == 1) { // we found exactly one possible value => resolve
                        tag = tag.withValue(propertyValues.iterator().next());
                    }
                }

                return tag;
            }

            private boolean isDependencyVersionTag() {
                return dependencyVersionTag.matches(getCursor());
            }

            private boolean isDefinedInDesiredDependencyTag() {
                Cursor parent = getCursor().getParent();
                if (parent == null || !(parent.getValue() instanceof Xml.Tag)) {
                    return false;
                }
                Xml.Tag parentTag = parent.getValue();

                boolean groupIdMatched = parentTag.getChildren("groupId")
                        .stream()
                        .map(Xml.Tag::getValue)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .anyMatch(groupId::equals);

                boolean artifactIdMatched = parentTag.getChildren("artifactId")
                        .stream()
                        .map(Xml.Tag::getValue)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .anyMatch(artifactId::equals);

                return groupIdMatched && artifactIdMatched;
            }
        };
    }
}
