package lv.cebbys.mcmods.celib.processor.builders;

import lv.cebbys.mcmods.celib.processor.api.ArrayResourceElement;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ResourceFieldParser {
    public static void parse(PrintWriter out, Element field) {
        if (field.getKind() == ElementKind.FIELD) {
            Set<AnnotationMirror> annotations = field.getAnnotationMirrors().stream()
                    .map(a -> (AnnotationMirror) a).collect(Collectors.toSet());
            parseArrayField(out, field, annotations);
        }
    }

    public static void parseArrayField(PrintWriter out, Element field, Set<AnnotationMirror> annotations) {
        Optional<AnnotationMirror> optional = annotations.stream()
                .filter(a -> ArrayResourceElement.class.getName().equals(a.getAnnotationType().toString()))
                .findFirst();
        if(optional.isPresent()) {
            AnnotationMirror annotation = optional.get();
            Map<String, Object> options = annotation.getElementValues().keySet().stream()
                    .collect(Collectors.toMap(
                            (key) -> key.toString().replaceAll("\\(\\)", ""),
                            (key) -> annotation.getElementValues().get(key)));
            if(options.get("split") instanceof Boolean b && b) {
                if(options.get("elements") instanceof String[] arr) {
                    out.print("\tprivate " + field.asType() + " " + field.getSimpleName() + ";\n\n");
                    out.print("\tpublic void " + field.getSimpleName() + "(");
                }
            }
            System.out.println(options);
        }
    }


//                            System.out.println("-------------------------");
//                            System.out.println(field.asType());
//                            System.out.println(field.getKind());
//                            System.out.println(field.getSimpleName());
//                            System.out.println(field.getModifiers());
//                            System.out.println(field.getAnnotationMirrors());
//                            if(field.getKind() == ElementKind.FIELD) {
//                                out.print("\tprivate " + field.asType().toString() + " " + field.getSimpleName() + ";\n\n");
//
//                                out.print("\tpublic " + className + " " + field.getSimpleName());
//                                out.print("(" + field.asType().toString() + " v) {\n");
//                                out.print("\t\tthis." + field.getSimpleName() + " = v;\n");
//                                out.print("\t\treturn this;");
//                                out.print("\t}");
//                            }
}
