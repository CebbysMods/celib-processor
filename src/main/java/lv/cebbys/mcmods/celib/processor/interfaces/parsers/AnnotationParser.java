package lv.cebbys.mcmods.celib.processor.interfaces.parsers;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.function.Consumer;

public interface AnnotationParser {
    default void parseAnnotations(Set<? extends TypeElement> annotations,
                                                    RoundEnvironment roundEnv,
                                                    Consumer<Element> consumer) {
        annotations.forEach(annotation -> {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            elements.forEach(consumer);
        });
    }
}
