package lv.cebbys.mcmods.celib.processor;


import lv.cebbys.mcmods.celib.processor.builders.ResourceFieldParser;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes({"lv.cebbys.mcmods.celib.processor.api.Resource"})
@SupportedSourceVersion(SourceVersion.RELEASE_16)
public class ResourceProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(annotation -> {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            elements.forEach(element -> {

                String packageName = element.asType().toString();
                String className = packageName.substring(packageName.lastIndexOf(".") + 1) + "Resource";
                packageName = packageName.substring(0, packageName.lastIndexOf("."));

                try {
                    JavaFileObject file = processingEnv.getFiler().createSourceFile(packageName + "." + className);
                    try (PrintWriter out = new PrintWriter(file.openWriter())) {
                        out.print("package " + packageName + ";\n\n");
                        out.print("public class " + className + " {\n\n");

                        out.print("\tpublic static " + className + " resource() {\n");
                        out.print("\t\treturn new " + className + "();\n");
                        out.print("\t}\n");

                        element.getEnclosedElements().forEach((e) -> ResourceFieldParser.parse(out, e));

                        out.print("}");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        return false;
    }


    private void writeResourceFile(Class<?> c) {
        String packageName = c.getPackageName();
        String className = c.getSimpleName() + "ResourceBuilder";
        try {
            JavaFileObject file = processingEnv.getFiler().createSourceFile(packageName + "." + className);
            try (PrintWriter out = new PrintWriter(file.openWriter())) {
                out.print("package " + packageName + ";\n\n");
                out.print("public class " + className + " {");

                out.print("\tpublic static " + className + " builder() {");
                out.print("\treturn new " + className + "();");
                out.print("\t}");

                out.print("}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
