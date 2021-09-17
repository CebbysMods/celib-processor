package lv.cebbys.mcmods.celib.processor.structures.customized.method;

import lv.cebbys.mcmods.celib.processor.interfaces.MethodsProvider;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.GenericClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.method.BasicMethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.method.GenericMethodProfile;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class ResourceParseResourceMethods implements MethodsProvider {

    private final LinkedHashSet<MethodProfile<?>> methods = new LinkedHashSet<>();

    private ResourceParseResourceMethods() {}

    public static ResourceParseResourceMethods of(ClassProfile<?> resource, ClassProfile<?> named) {
        GenericMethodProfile extractField = GenericMethodProfile.of("R")
                .setModifiers("private").setMethodName("extractField");
        extractField
                .setReturnType(extractField.getVararg("R"))
                .setParameters(
                        BasicFieldProfile.of(named, "named"),
                        BasicFieldProfile.of(String.class, "fieldName"),
                        BasicFieldProfile.of(
                                GenericClassProfile.of(Class.class, extractField.getVararg("R")),
                                "resultType"))
                .addLine("try {")
                .addLine("\tField field = Arrays.stream(%s.class.getDeclaredFields()).filter(f -> {", named.getClassName())
                .addLine("\t\treturn fieldName.equals(f.getName());")
                .addLine("\t}).findFirst().orElse(null);")
                .addLine("\tfield.setAccessible(true);")
                .addLine("\treturn resultType.cast(field.get(named));")
                .addLine("} catch (Exception e) {")
                .addLine("\te.printStackTrace();")
                .addLine("}")
                .addLine("return null;")
                .addAdditionalImports(Field.class, Exception.class, Arrays.class)
                .addAdditionalImports(resource);

        BasicMethodProfile getResourceData = BasicMethodProfile.of()
                .setModifiers("private")
                .setReturnType(resource)
                .setMethodName("getResourceData")
                .setParameters(BasicFieldProfile.of(named, "named"))
                .addLine("return extractField(named, \"resource\", %s.class);",
                        resource.parameterString(),
                        resource.getClassName());

        BasicMethodProfile getResourceName = BasicMethodProfile.of()
                .setModifiers("private")
                .setReturnType(String.class)
                .setMethodName("getResourceName")
                .setParameters(BasicFieldProfile.of(named, "named"))
                .addLine("return extractField(named, \"name\", String.class);",
                        resource.parameterString())
                .addAdditionalImports(String.class);

        ResourceParseResourceMethods m = new ResourceParseResourceMethods();
        m.methods.add(getResourceName);
        m.methods.add(getResourceData);
        m.methods.addAll(new ResourceToJsonMethods().getMethods());
        m.methods.add(extractField);
        return m;
    }

    @Override
    public LinkedHashSet<MethodProfile<?>> getMethods() {
        return new LinkedHashSet<>(methods);
    }
}
