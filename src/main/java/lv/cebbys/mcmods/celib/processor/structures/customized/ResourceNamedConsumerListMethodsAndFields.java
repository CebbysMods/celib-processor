package lv.cebbys.mcmods.celib.processor.structures.customized;

import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import lv.cebbys.mcmods.celib.processor.interfaces.MethodsAndFieldsProvider;
import lv.cebbys.mcmods.celib.processor.structures.customized.clazz.ClassProfileParser;
import lv.cebbys.mcmods.celib.processor.structures.customized.method.ResourceParseResourceMethods;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.FieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.GenericClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.LooseArrayFieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.method.BasicMethodProfile;

import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class ResourceNamedConsumerListMethodsAndFields implements MethodsAndFieldsProvider {

    private final LinkedHashSet<MethodProfile<?>> methods = new LinkedHashSet<>();
    private final LinkedHashSet<FieldProfile<?>> fields = new LinkedHashSet<>();

    private ResourceNamedConsumerListMethodsAndFields() {
    }


    public static ResourceNamedConsumerListMethodsAndFields of(Element field,
                                                     ClassProfile<?> returnType,
                                                     ClassProfile<?> named,
                                                     ClassProfile<?> resource) {
        ResourceNamedConsumerListMethodsAndFields profile = new ResourceNamedConsumerListMethodsAndFields();

        String fieldName = field.getSimpleName().toString();

        GenericClassProfile map = (GenericClassProfile) ClassProfileParser.of(field);
        map.setGenerics(map.getGenerics().get(0), resource);

        profile.fields.add(BasicFieldProfile.of("private", map, fieldName));

        profile.methods.add(BasicMethodProfile.of()
                .addModifiers("public")
                .setReturnType(returnType)
                .setMethodName(fieldName)
                .setParameters(LooseArrayFieldProfile.ofConsumers(named))
                .addLine("this.%s = new LinkedTree%s();", field.getSimpleName(), map.parameterString())
                .addLine("Arrays.stream(consumers).forEachOrdered(consumer -> {")
                .addLine("\t%s named = new %s();", named.parameterString(), named.getClassName())
                .addLine("\tconsumer.accept(named);")
                .addLine("\tString resourceName = getResourceName(named);")
                .addLine("\t%s resourceData = getResourceData(named);", resource.parameterString())
                .addLine("\tthis.%s.put(resourceName, resourceData);", field.getSimpleName())
                .addLine("});")
                .addLine("return this;")
                .addAdditionalImports(Arrays.class, LinkedTreeMap.class)
        );
        profile.methods.addAll(ResourceParseResourceMethods.of(resource, named).getMethods());
        return profile;
    }

    public static ResourceNamedConsumerListMethodsAndFields jsonMapper(Element field,
                                                                       ClassProfile<?> returnType,
                                                                       ClassProfile<?> inputType) {
        ResourceNamedConsumerListMethodsAndFields profile = new ResourceNamedConsumerListMethodsAndFields();
        String fieldName = field.getSimpleName().toString();
        ClassProfile<?> resourceProfile = inputType.clone();
        resourceProfile.setClassName(resourceProfile.getClassName() + "Resource");
        ClassProfile<?> namedProfile = inputType.clone();
        namedProfile.setClassName(namedProfile.getClassName() + "Named");

        GenericClassProfile map = (GenericClassProfile) ClassProfileParser.of(field);
        map.setGenerics(map.getGenerics().get(0), BasicClassProfile.of(JsonObject.class));

        profile.fields.add(BasicFieldProfile.of("private", map, fieldName));

        profile.methods.add(BasicMethodProfile.of()
                .addModifiers("public")
                .setReturnType(returnType)
                .setMethodName(fieldName)
                .setParameters(LooseArrayFieldProfile.ofConsumers(namedProfile))
                .addLine("this.%s = new LinkedTree%s();", field.getSimpleName(), map.parameterString())
                .addLine("Arrays.stream(consumers).forEachOrdered(consumer -> {")
                .addLine("\t%s named = new %s();", namedProfile.parameterString(), namedProfile.getClassName())
                .addLine("\tconsumer.accept(named);")
                .addLine("\tString resourceName = getResourceName(named);")
                .addLine("\tJsonObject resourceData = resourceToJson(getResourceData(named), %s.class);", resourceProfile.getClassName())
                .addLine("\tthis.%s.put(resourceName, resourceData);", field.getSimpleName())
                .addLine("});")
                .addLine("return this;")
                .addAdditionalImports(Arrays.class, LinkedTreeMap.class)
        );
        profile.methods.addAll(ResourceParseResourceMethods.of(resourceProfile, namedProfile).getMethods());
        return profile;
    }

    @Override
    public LinkedHashSet<FieldProfile<?>> getFields() {
        return new LinkedHashSet<>(fields);
    }

    @Override
    public LinkedHashSet<MethodProfile<?>> getMethods() {
        return new LinkedHashSet<>(methods);
    }
}
