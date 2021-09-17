package lv.cebbys.mcmods.celib.processor.structures.customized.method;

import com.google.gson.JsonObject;
import lv.cebbys.mcmods.celib.processor.interfaces.MethodsProvider;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.GenericClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.method.GenericMethodProfile;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;

public class ResourceToJsonMethods implements MethodsProvider {

    private final LinkedHashSet<MethodProfile<?>> methods;

    public ResourceToJsonMethods() {
        methods = new LinkedHashSet<>();
        GenericMethodProfile toResource = GenericMethodProfile.of()
                .setModifiers("private")
                .setVarargs("T")
                .setReturnType(BasicClassProfile.of(JsonObject.class))
                .setMethodName("resourceToJson");
        toResource.setParameters(
                BasicFieldProfile.of().setParameterIndex(0).setName("r").setType(toResource.getVararg("T")),
                BasicFieldProfile.of().setParameterIndex(1).setName("type").setType(
                        GenericClassProfile.of()
                                .setClassName(Class.class.getSimpleName())
                                .setPackageName(Class.class.getPackageName())
                                .setGenerics(toResource.getVararg("T"))))
                .addLine("JsonObject json = new JsonObject();")
                .addLine("for (Field field : type.getDeclaredFields()) {")
                .addLine("\ttry {")
                .addLine("\t\tfield.setAccessible(true);")
                .addLine("\t\tString fieldName = field.getName();")
                .addLine("\t\tObject fieldData = field.get(r);")
                .addLine("\t\taddToJsonObject(json, fieldName, fieldData);")
                .addLine("\t} catch (Exception e) {")
                .addLine("\t\tthrow new RuntimeException(e);")
                .addLine("\t}")
                .addLine("}")
                .addLine("return json;")
                .addAdditionalImports(
                        BasicClassProfile.of(String.class),
                        BasicClassProfile.of(Object.class),
                        BasicClassProfile.of(Exception.class),
                        BasicClassProfile.of(Field.class),
                        BasicClassProfile.of(RuntimeException.class));
        methods.add(toResource);
        methods.add(new AddToJsonObjectMethod().getMethod());
        methods.add(new AddToJsonArrayMethod().getMethod());
    }

    @Override
    public LinkedHashSet<MethodProfile<?>> getMethods() {
        return new LinkedHashSet<>(methods);
    }
}
