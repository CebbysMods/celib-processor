package lv.cebbys.mcmods.celib.processor.interfaces.parsers;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public interface FieldParser {
    default void parseFields(List<? extends Element> fields, BiConsumer<Element, LinkedHashSet<AnnotationMirror>> consumer) {
        fields.forEach(field -> {
            consumer.accept(field, new LinkedHashSet<>(field.getAnnotationMirrors()));
        });
    }
}
