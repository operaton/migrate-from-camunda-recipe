package org.operaton.rewrite;

import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.openrewrite.*;
import org.openrewrite.marker.SearchResult;
import org.openrewrite.maven.MavenIsoVisitor;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.tree.Xml;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;

@Data
public class ResolveDependencyVersionProperty extends Recipe {

    @Option
    private final String groupId;

    @Option
    private final String artifactId;


    @Override
    public @NlsRewrite.DisplayName String getDisplayName() {
        return "Resolve dependency version property for dependency";
    }

    @Override
    public @NlsRewrite.Description String getDescription() {
        return "Resolve the dependency version property for a specific dependency.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {

        return new MavenIsoVisitor<>() {

            @Override
            public @Nullable Xml visit(@Nullable Tree tree, ExecutionContext executionContext, Cursor parent) {
                Xml xml = super.visit(tree, executionContext, parent);

                Map<String, Set<String>> propertyValues = FindPropertyValue.findValues(xml);

                return new ResolveVersionPropertiesForDependency(groupId, artifactId, propertyValues).visit(xml, executionContext);
            }
        };
    }

    class ResolveVersionPropertiesForDependency extends MavenIsoVisitor<ExecutionContext> {
        private final XPathMatcher versionMatcher = new XPathMatcher("dependency/version");

        private final String groupId;
        private final String artifactId;
        private final Map<String, Set<String>> propertyValues;

        public ResolveVersionPropertiesForDependency(String groupId, String artifactId, Map<String, Set<String>> propertyValues) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.propertyValues = propertyValues;
        }

        @Override
        public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
            tag = super.visitTag(tag, executionContext);

            if (isDependencyVersionTag()
                && hasSiblingWithValue("groupId", groupId)
                && hasSiblingWithValue("artifactId", artifactId)) {

                Optional<String> propertyKey = tag.getValue()
                        .filter(s -> s.startsWith("${"))
                        .filter(s -> s.endsWith("}"))
                        .map(s -> s.substring(2, s.length() - 1));

                if (propertyKey.isPresent()) {
                    Set<String> possibleValues = propertyValues.get(propertyKey.get());
                    if (possibleValues != null && possibleValues.size() == 1) { // we found exactly one possible value
                        tag = tag.withValue(possibleValues.iterator().next());
                    }
                }
            }

            return tag;
        }

        private boolean isDependencyVersionTag() {
            return versionMatcher.matches(getCursor());
        }

        private boolean hasSiblingWithValue(String tag, String value) {
            Cursor parent = getCursor().getParent();
            if (parent == null || !(parent.getValue() instanceof Xml.Tag)) {
                return false;
            }
            Xml.Tag parentTag = parent.getValue();

            return parentTag.getChildren(tag)
                    .stream()
                    .map(Xml.Tag::getValue)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .anyMatch(value::equals);
        }
    }

    class FindPropertyValue extends MavenIsoVisitor<ExecutionContext> {

        public static Map<String, Set<String>> findValues(Xml subtree) {

            MavenIsoVisitor<ExecutionContext> collectProperties = new MavenIsoVisitor<>() {
                @Override
                public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {
                    tag = super.visitTag(tag, executionContext);
                    boolean isPropertyDefiningTag = new XPathMatcher("//project/properties/*").matches(getCursor());
                    return isPropertyDefiningTag ? SearchResult.found(tag) : tag;
                }
            };

            ArrayList<Xml.Tag> foundStuff = TreeVisitor.collect(collectProperties, subtree, new ArrayList<>(), Xml.Tag.class, Function.identity());
            return foundStuff
                    .stream()
                    .filter(t -> t.getMarkers().findFirst(SearchResult.class).isPresent())
                    .filter(t -> t.getValue().isPresent())
                    .collect(groupingBy(Xml.Tag::getName, mapping(t -> t.getValue().get(), toSet())));
        }

    }

}
