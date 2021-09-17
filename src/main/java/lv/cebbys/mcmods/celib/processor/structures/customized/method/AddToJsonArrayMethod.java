package lv.cebbys.mcmods.celib.processor.structures.customized.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lv.cebbys.mcmods.celib.processor.interfaces.MethodProvider;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.method.BasicMethodProfile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AddToJsonArrayMethod implements MethodProvider {

    private final MethodProfile<?> method;

    public AddToJsonArrayMethod() {
        method = BasicMethodProfile.of()
                .setModifiers("private")
                .setMethodName("addToJsonArray")
                .setParameters(
                        BasicFieldProfile.of().setParameterIndex(0).setName("json").setType(BasicClassProfile.of(JsonArray.class)),
                        BasicFieldProfile.of().setParameterIndex(1).setName("data").setType(BasicClassProfile.of(Object.class)))
                .addLine("if(data instanceof String s) {")
                .addLine("\tjson.add(s);")
                .addLine("} else if (data instanceof Number s) {")
                .addLine("\tjson.add(s);")
                .addLine("} else if (data instanceof Boolean s) {")
                .addLine("\tjson.add(s);")
                .addLine("} else if (data instanceof Character s) {")
                .addLine("\tjson.add(s);")
                .addLine("} else if(data instanceof List) {")
                .addLine("\tJsonArray array = new JsonArray();")
                .addLine("\t((List<Object>) data).forEach(element -> addToJsonArray(array, element));")
                .addLine("\tjson.add(array);")
                .addLine("} else if(data instanceof Map) {")
                .addLine("\tJsonObject object = new JsonObject();")
                .addLine("\t((Map<Object, Object>) data).forEach((key, value) -> {")
                .addLine("\t\taddToJsonObject(object, key.toString(), value);")
                .addLine("\t});")
                .addLine("\tjson.add(object);")
                .addLine("} else {")
                .addLine("\tJsonObject object = new JsonObject();")
                .addLine("\tArrays.asList(data.getClass().getDeclaredFields()).forEach(field -> {")
                .addLine("\t\ttry {")
                .addLine("\t\t\tfield.setAccessible(true);")
                .addLine("\t\t\tString fieldName = field.getName();")
                .addLine("\t\t\tObject fieldValue = field.get(data);")
                .addLine("\t\t\taddToJsonObject(object, fieldName, fieldValue);")
                .addLine("\t\t} catch (Exception e) {")
                .addLine("\t\t\tthrow new RuntimeException(e);")
                .addLine("\t\t}")
                .addLine("\t});")
                .addLine("\tjson.add(object);")
                .addLine("}")
                .addAdditionalImports(
                        BasicClassProfile.of(String.class),
                        BasicClassProfile.of(Number.class),
                        BasicClassProfile.of(Boolean.class),
                        BasicClassProfile.of(Character.class),
                        BasicClassProfile.of(List.class),
                        BasicClassProfile.of(Map.class),
                        BasicClassProfile.of(JsonObject.class),
                        BasicClassProfile.of(Exception.class),
                        BasicClassProfile.of(Arrays.class),
                        BasicClassProfile.of(RuntimeException.class));

    }

    @Override
    public MethodProfile<?> getMethod() {
        return method;
    }
}
