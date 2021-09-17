package lv.cebbys.mcmods.celib.processor.structures.customized.method;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lv.cebbys.mcmods.celib.processor.api.Named;
import lv.cebbys.mcmods.celib.processor.api.elements.MapResourceElement;
import lv.cebbys.mcmods.celib.processor.exception.ValidationException;
import lv.cebbys.mcmods.celib.processor.interfaces.MethodsProvider;
import lv.cebbys.mcmods.celib.processor.parsers.DefinedNamesParser;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.utility.DefinedName;

import java.util.LinkedHashSet;

public class NamedExtendedMethods implements MethodsProvider {
    private static final String DEFINED = Named.class.getDeclaredMethods()[0].getName();
    private static final String DEFINED_JSON = DefinedName.class.getDeclaredMethods()[1].getName();
    private static final String DEFINED_METHOD = DefinedName.class.getDeclaredMethods()[0].getName();

    private final LinkedHashSet<MethodProfile<?>> methods = new LinkedHashSet<>();
    private final DefinedNamesParser definedNamesParser = new DefinedNamesParser();


    public NamedExtendedMethods(ClassProfile<?> resourceProfile, JsonObject namedAnnotation) {
        JsonArray array = namedAnnotation.get(DEFINED).getAsJsonArray();
        array.forEach(jsonElement -> {
            JsonObject element = jsonElement.getAsJsonObject();
            String json = element.getAsJsonObject(DEFINED_JSON).get("value").getAsString();
            JsonObject methodObject = element.getAsJsonObject(DEFINED_METHOD);
            String method;
            if (methodObject != null) {
                method = methodObject.get("value").getAsString();
            } else {
                try {
                    method = definedNamesParser.parse(json);
                } catch (ValidationException e) {
                    throw new RuntimeException(e);
                }
            }
            methods.add(new NamedGetMethod(resourceProfile, method, json).getMethod());
        });
    }

    @Override
    public LinkedHashSet<MethodProfile<?>> getMethods() {
        return new LinkedHashSet<>(methods);
    }
}
