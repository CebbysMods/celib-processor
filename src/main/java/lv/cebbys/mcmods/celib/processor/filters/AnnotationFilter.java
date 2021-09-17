package lv.cebbys.mcmods.celib.processor.filters;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.function.Predicate;

public class AnnotationFilter implements Predicate<AnnotationMirror> {

    private final Class<? extends Annotation> annotation;
    public AnnotationFilter(Class<? extends Annotation> a) {
        annotation = a;
    }

    @Override
    public boolean test(AnnotationMirror a) {
        return annotation.getName().equals(a.getAnnotationType().toString());
    }
}
