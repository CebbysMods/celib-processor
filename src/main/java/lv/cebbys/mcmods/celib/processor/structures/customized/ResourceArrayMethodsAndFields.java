package lv.cebbys.mcmods.celib.processor.structures.customized;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lv.cebbys.mcmods.celib.processor.interfaces.MethodsAndFieldsProvider;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.FieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.ArrayClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.customized.clazz.ClassProfileParser;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.method.BasicMethodProfile;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class ResourceArrayMethodsAndFields implements MethodsAndFieldsProvider {

    protected final FieldProfile<?> field;
    protected final MethodProfile<?> method;

    public ResourceArrayMethodsAndFields(Element element,
                                         ClassProfile<?> profile,
                                         LinkedHashSet<JsonElement> annotation) {
        field = BasicFieldProfile.of(element).setModifiers("private");
        ArrayClassProfile def = (ArrayClassProfile) ClassProfileParser.of(element);

        method = BasicMethodProfile.of()
                .addModifiers("public")
                .setReturnType(profile)
                .setMethodName(field.getName());

        JsonArray array = new ArrayList<>(annotation).get(0).getAsJsonObject().getAsJsonArray("elements");
        array.forEach(nameJson -> method.addParameters(BasicFieldProfile.of()
                .setName(nameJson.getAsJsonObject().get("value").getAsString()).setType(BasicClassProfile.of(def)))
        );

        method.addLine(
                "this.%s = new %s[%d];", field.getName(), def.getClassName(), array.size()
        );
        for (int i = 0; i < array.size(); i++) {
            method.addLine(
                    "this.%s[%d] = %s;", field.getName(), i, array.get(i).getAsJsonObject().get("value").getAsString()
            );
        }
        method.addLine("return this;");

    }

    public FieldProfile<?> getField() {
        return field;
    }

    public MethodProfile<?> getMethod() {
        return method;
    }

    @Override
    public LinkedHashSet<FieldProfile<?>> getFields() {
        return new LinkedHashSet<>(Collections.singleton(field));
    }

    @Override
    public LinkedHashSet<MethodProfile<?>> getMethods() {
        return new LinkedHashSet<>(Collections.singleton(method));
    }
}
