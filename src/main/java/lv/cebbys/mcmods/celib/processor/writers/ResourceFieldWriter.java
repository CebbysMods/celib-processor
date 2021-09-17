package lv.cebbys.mcmods.celib.processor.writers;

import com.google.gson.internal.LinkedTreeMap;
import lv.cebbys.mcmods.celib.processor.api.elements.ArrayResourceElement;
import lv.cebbys.mcmods.celib.processor.api.elements.MapResourceElement;
import lv.cebbys.mcmods.celib.processor.parsers.MapResourceElementParser;
import lv.cebbys.mcmods.celib.processor.interfaces.parsers.FieldParser;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.ContainerProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.parsers.ArrayResourceElementParser;
import lv.cebbys.mcmods.celib.processor.structures.customized.clazz.ClassProfileParser;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.ConsumerClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.GenericClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.LooseArrayFieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.method.BasicMethodProfile;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ResourceFieldWriter implements FieldParser {

    public void write(ContainerProfile container,
                      List<? extends Element> fields,
                      Map<String, String> namedMethods) {
        parseFields(fields, (element, annotations) -> {
            parseArrayField(container, element, annotations);
            parseMapField(container, element, annotations, namedMethods);
        });
    }

    private void parseArrayField(ContainerProfile container, Element field, Set<AnnotationMirror> annotations) {
        Optional<AnnotationMirror> optional = annotations.stream()
                .filter(a -> ArrayResourceElement.class.getName().equals(a.getAnnotationType().toString()))
                .findFirst();

        if (optional.isPresent()) {
            ArrayResourceElementParser parser = new ArrayResourceElementParser();
            parser.parse(optional.get());
//            container.addMethod(new ResourceArrayMethodsAndFields(field, container.getProfile(), parser.getParameterNames()));
        }
    }

    private void parseMapField(ContainerProfile container, Element field, Set<AnnotationMirror> annotations, Map<String, String> namedMethods) {
        Optional<AnnotationMirror> optional = annotations.stream()
                .filter(a -> MapResourceElement.class.getName().equals(a.getAnnotationType().toString()))
                .findFirst();
        if (optional.isPresent()) {

            MapResourceElementParser parser = new MapResourceElementParser();
            try {
                namedMethods.putAll(parser.extractMethodNames(optional.get()));
            } catch (Exception e) {
                System.out.println(e);
            }

            GenericClassProfile map = (GenericClassProfile) ClassProfileParser.of(field);
            ClassProfile<?> originalResource = map.getGenerics().get(1);

            ClassProfile<?> resource = originalResource.clone();
            resource.setClassName(originalResource.getClassName() + "Resource");

            ClassProfile<?> named = originalResource.clone();
            named.setClassName(originalResource.getClassName() + "Named");

            map.setGenerics(list -> {
                list.add(map.getGenerics().get(0));
                list.add(resource);
            });
            container.addField(BasicFieldProfile.of()
                    .setModifiers("public")
                    .setType(map)
                    .setName(field.getSimpleName().toString())
            );

            container.addMethod(BasicMethodProfile.of()
                    .addModifiers("public")
                    .setReturnType(container.getProfile())
                    .setMethodName(field.getSimpleName().toString())
                    .setParameters(list -> {
                        list.add(LooseArrayFieldProfile.of()
                                .setName("consumers")
                                .setType(ConsumerClassProfile.of().setConsumer(named))
                        );
                    })
                    .addLine("this.%s = new LinkedTree%s();", field.getSimpleName(), map.parameterString())
                    .addLine("Arrays.stream(consumers).forEachOrdered(consumer -> {")
                    .addLine("\t%s named = new %s();", named.parameterString(), named.getClassName())
                    .addLine("\tconsumer.accept(named);")
                    .addLine("\tparseResource(named);")
                    .addLine("});")
                    .addLine("return this;")
                    .addAdditionalImports(BasicClassProfile.of(Arrays.class), BasicClassProfile.of(LinkedTreeMap.class))
            );
        }
    }
}
